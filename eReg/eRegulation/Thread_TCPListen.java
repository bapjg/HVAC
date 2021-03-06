package eRegulation;
 
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.net.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketTimeoutException;
//import java.util.ArrayList;

import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Thread_TCPListen 									implements 					Runnable
{
	ServerSocket												UI_Server;
	Socket														UI_Socket;
	
	public void run()
    {
		LogIt.info("Thread_TCPListen", "Run", "Starting");            
 
		Msg__Abstract											message_in 					= null;
		Msg__Abstract											message_out 				= null;
		try
		{
			UI_Server																		= new ServerSocket(8889);
			UI_Server.setSoTimeout(10 * 1000);
		
			while (!Global.stopNow)
			{
				try
				{
					UI_Socket																= UI_Server.accept();
					
			        ObjectInputStream 	input 												= new ObjectInputStream(UI_Socket.getInputStream());
			        // This previous line results in an EOFException
			        
			        message_in 																= (Msg__Abstract) input.readObject();

			        if (message_in == null)
			        {
						LogIt.info("Thread_TCPListen", "Run", "Null received from client");            
			            message_out 														= new Msg__Abstract().new Nack();
			        } 
			    	else
			    	{
			    		if 		(message_in instanceof Ctrl_Temperatures.Request) 		message_out = process_Ctrl_Temperatures_Request		((Ctrl_Temperatures.Request) message_in);
			    		
			    		else if (message_in instanceof Ctrl_Immediate.Request)			message_out	= process_Ctrl_Immediate_Request		((Ctrl_Immediate.Request) message_in);
			    		else if (message_in instanceof Ctrl_Immediate.Execute)			message_out	= process_Ctrl_Immediate_Execute		((Ctrl_Immediate.Execute) message_in); 
			    		
			    		else if (message_in instanceof Ctrl_Configuration.Request)		message_out	= process_Ctrl_Configuration_Request	();
			    		else if (message_in instanceof Ctrl_Configuration.Update) 		message_out	= process_Ctrl_Configuration_Update		((Ctrl_Configuration.Update) message_in);

			    		else if (message_in instanceof Ctrl_Weather.Request)			message_out	= process_Ctrl_Weather_Request			();
			    		
			    		else if (message_in instanceof Ctrl_Actions_Relays.Request)		message_out	= process_Ctrl_Actions_Relays_Request	();
			    		else if (message_in instanceof Ctrl_Actions_Relays.Execute)		message_out	= process_Ctrl_Actions_Relays_Execute	((Ctrl_Actions_Relays.Execute) message_in);
			    		
			    		else if (message_in instanceof Ctrl_Actions_Test_Mail.Execute)	message_out	= process_Ctrl_Actions_Test_Mail_Execute();
			    		
			    		else if (message_in instanceof Ctrl_Actions_Stop.Execute)		message_out	= process_Ctrl_Actions_Stop_Execute		((Ctrl_Actions_Stop.Execute) message_in);

			    		else if (message_in instanceof Ctrl_Fuel_Consumption.Update)	message_out	= process_Ctrl_Fuel_Consumption_Update	((Ctrl_Fuel_Consumption.Update) message_in);
			    		else if (message_in instanceof Ctrl_Thermo_List.Request)		message_out	= process_Ctrl_Thermo_List_Request		();
			        } 
			        
			        ObjectOutputStream 							output						= null;
					
					output 																	= new ObjectOutputStream(UI_Socket.getOutputStream());
					output.writeObject(message_out);
			        output.flush();
			        output.close();
			        
			        if (Global.stopNow)
			        {
			        	Global.waitSeconds(2);												// If Ctrl_Actions_Stop received, allow Ack to go
			        }
				}
		        catch (ClassNotFoundException eCNF)		{	LogIt.info("Thread_TCPListen", "Run", "Caught CNF");          					}
				catch (EOFException eEOF)				{	LogIt.info("Thread_TCPListen", "Run", "Caught EOF");       						}
				catch (StreamCorruptedException eSCE)	{	/* Do nothing as this is a hack attempt										*/	}
				catch (SocketTimeoutException eTO)		{	/* Do nothing we will loop and do another 10s wait unless stopNow activated	*/	}
				catch (SocketException eSO)				{	/* Probably a hack attempt log shows "connection reset" or "Broken pipe" 	*/	}
				catch (IOException eIO)					{	LogIt.info("Thread_TCPListen", "Run", "Caught IO " + eIO);   					}
				catch (Exception e)						{	LogIt.info("Thread_TCPListen", "Run", "Caught other" + e); 						}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			UI_Server.close();
		}
		catch (IOException e)
		{
		}
 		LogIt.info("Thread_TCPListen", "Run", "Stopping");             
	}
	
	private Ctrl_Temperatures.Data		process_Ctrl_Temperatures_Request		(Ctrl_Temperatures.Request message_in)
	{
        Ctrl_Temperatures.Data message_return												= new Ctrl_Temperatures().new Data();
        message_return.dateTime 															= System.currentTimeMillis();

        message_return.tempBoiler	 														= Global.thermoBoiler.reading;
        message_return.tempBoilerIn	 														= Global.thermoBoilerIn.reading;
        message_return.tempBoilerOut	 													= Global.thermoBoilerOut.reading;
		
        message_return.tempFloorIn	 														= Global.thermoFloorIn.reading;
        message_return.tempFloorOut	 														= Global.thermoFloorOut.reading;
		
        message_return.tempRadiatorIn	 													= Global.thermoRadiatorIn.reading;
        message_return.tempRadiatorOut	 													= Global.thermoRadiatorOut.reading;

        message_return.tempHotWater	 														= Global.thermoHotWater.reading;
        message_return.tempOutside	 														= Global.thermoOutside.reading;
        message_return.tempLivingRoom	 													= Global.thermoLivingRoom.reading;
        
        return message_return;
	}
	private	Ctrl_Immediate.Data			process_Ctrl_Immediate_Request			(Ctrl_Immediate.Request message_in)
	{
		Ctrl_Immediate.Data 									message_return				= new Ctrl_Immediate().new Data();
		
		String 													circuitName					= message_in.circuitName;
		
		Circuit_Abstract 										circuit						= Global.circuits.fetchCircuit(circuitName);
		
		if (circuit == null)	LogIt.display("Thread_TCP", "process_Ctrl_Immediate_Request", "circuitName '" + message_in.circuitName + "' not found");
		
		message_return.circuitName															= circuitName;

		Long 													now							= Global.Time.now();
		Long													midnight					= 24L * 60 * 60 * 1000;
		Long													nextStart					= midnight;
		Integer													tempObjective				= 0;
		CircuitTask												selectedTask				= null;

		if ((circuit.taskActive == null) || (circuit.state == HVAC_STATES.Circuit.Optimising))
		{
			for (CircuitTask aTask : circuit.circuitTaskList)			// Check to ensure there are no active tasks
			{
				if (	(aTask.days.contains(Global.Date.getDayOfWeek(0))) 
//				&& 		(! aTask.active)
				&&     	(aTask.timeStart > now )
				&&     	(aTask.timeStart < nextStart ))
				{
					nextStart																= aTask.timeStart;
					selectedTask															= aTask;
				}
			}
			
            if (nextStart < midnight)	// Task currently inactive but planned
            {
            	message_return.executionActive												= false;
            	message_return.executionPlanned												= true;
            	message_return.timeStart	 												= nextStart;
            	message_return.timeEnd		 												= selectedTask.timeEnd;
            	message_return.tempObjective 												= new Cmn_Temperature(selectedTask.tempObjective);
            	message_return.stopOnObjective												= selectedTask.stopOnObjective;
            }
            else						// Task currently inactive and not even planned
            {
            	message_return.executionActive												= false;
            	message_return.executionPlanned												= false;
            	message_return.timeStart													= 0L;
            	message_return.timeEnd														= 0L;
            	message_return.tempObjective												= new Cmn_Temperature(0);
            	message_return.stopOnObjective												= false;
            }
		}
		else		// Task currently active
		{
			message_return.executionActive													= true;
			message_return.executionPlanned													= false;
			message_return.timeStart														= circuit.taskActive.timeStart;
			message_return.timeEnd															= circuit.taskActive.timeEnd;
			message_return.tempObjective													= new Cmn_Temperature(circuit.taskActive.tempObjective);
			message_return.stopOnObjective													= circuit.taskActive.stopOnObjective;
		}
		return message_return;
	}
	private Msg__Abstract				process_Ctrl_Immediate_Execute			(Ctrl_Immediate.Execute message_in)		// Process an immediate action to start
	{
		Long													now							= Global.Time.now();
		
		String 													circuitName					= message_in.circuitName;
		Circuit_Abstract 										circuit						= Global.circuits.fetchCircuit(circuitName);
		Msg__Abstract 											message_return				= new Msg__Abstract().new Ack();;
		
		if (message_in.action == Ctrl_Immediate.ACTION_Start)
		{
			// This seems to ignore start/end times in message
			CircuitTask 										circuitTask					= null;
			
			if (circuit.name.equalsIgnoreCase("Hot_Water"))
			{
				circuitTask									= new CircuitTask(	now, 						// Time Start
																	now + 30 * 60 * 1000, 					// TimeEnd
																	message_in.tempObjective.milliDegrees,	// TempObjective in millidesrees
																	true,									// StopOnObjective
																	HVAC_TYPES.CircuitTask.Immediate);

			}
			else
			{
				circuitTask									= new CircuitTask(	now, 						// Time Start
																	message_in.timeEnd.milliSeconds, 		// TimeEnd
																	message_in.tempObjective.milliDegrees,	// TempObjective in millidesrees
																	false,									// StopOnObjective
																	HVAC_TYPES.CircuitTask.Immediate);
			}
			circuit.taskActivate(circuitTask);
		}
		else if (message_in.action == message_in.ACTION_Stop)
		{
			circuit.requestOptimisation();
		}
		else
		{
			message_return																	= new Msg__Abstract().new Nack();
		}
		return message_return;
	}
  	private Ctrl_Configuration.Data 	process_Ctrl_Configuration_Request		()
	{
		// Returns the current configuration in operation
  		// It is timestamped now(). It should be timestamped with date/time recovered either from server or local file
  		
  		Ctrl_Configuration.Data 								message_return				= new Ctrl_Configuration.Data();
		
		// TODO
  		// This timestamp needs to be looked at in grater detail
		
		message_return.dateTime																= Global.DateTime.now();
		
		for (Thermometer 			globalThermometer : Global.thermometers.thermometerList)
		{
			Ctrl_Configuration.Thermometer 						paramThermometer			= new Ctrl_Configuration().new Thermometer();
			paramThermometer.name															= globalThermometer.name;
// TODO what is this for
//			paramThermometer.address						= globalThermometer.address;
			message_return.thermometerList.add(paramThermometer);
		}

		for (Relay 					globalRelay : Global.relays.relayList)
		{
			Ctrl_Configuration.Relay 							paramRelay					= new Ctrl_Configuration().new Relay();
			paramRelay.name																	= globalRelay.name;
			paramRelay.relayBank															= globalRelay.relayBank;
			paramRelay.relayNumber															= globalRelay.relayNumber;
			message_return.relayList.add(paramRelay);
		}
		
		for (Pump 					globalPump : Global.pumps.pumpList)
		{
			Ctrl_Configuration.Pump 							paramPump					= new Ctrl_Configuration().new Pump();
			paramPump.name																	= globalPump.name;
			paramPump.relay																	= globalPump.relay.name;
			message_return.pumpList.add(paramPump);
		}

		for (Circuit_Abstract 		globalCircuit : Global.circuits.circuitList)
		{
			Ctrl_Configuration.Circuit							paramCircuit				= new Ctrl_Configuration().new Circuit();
			paramCircuit.name																= globalCircuit.name;
			paramCircuit.pump																= globalCircuit.circuitPump.name;
			paramCircuit.thermometer														= globalCircuit.circuitThermo.name;
			paramCircuit.type																= globalCircuit.circuitType;
			message_return.circuitList.add(paramCircuit);
		}
		message_return.burner.relay															= Global.burnerPower.name;
		message_return.boiler.tempNeverExceed												= new Cmn_Temperature(Global.boiler.tempNeverExceed);
		message_return.boiler.tempOverShoot													= new Cmn_Temperature(Global.boiler.tempOvershoot);
		
		
		return message_return;
	}
 	private Ctrl_Configuration.Data 	process_Ctrl_Configuration_Update		(Ctrl_Configuration.Update message_in)
 	{
 		// Do something with message_in
 		return process_Ctrl_Configuration_Request();
	}
	private Ctrl_Weather.Data 			process_Ctrl_Weather_Request			()
	{
		// Returns the current configuration in operation
  		// It is timestamped now(). It should be timestamped with date/time recovered either from server or local file
  		
		Ctrl_Weather.Data 										message_return				= new Ctrl_Weather().new Data();			
		if (Global.weatherData == null)
		{
			try			// Getting new data
			{
				Global.weatherData															= new Ctrl_WeatherData();
			}
			catch (Exception e)
			{
				LogIt.info("Thread_TCPListen", "process_Ctrl_Weather_Request", "exception in trying to get new : " + e.toString());
			}
		}
		if (Global.weatherData == null)		// still no data
		{
			message_return																	= (Ctrl_Weather.Data) new Ctrl_Weather().new Data();
			message_return.weatherData														= null;
		}
		else
		{
			message_return																	= (Ctrl_Weather.Data) new Ctrl_Weather().new Data();
			message_return.weatherData														= Global.weatherData;
		}
		return message_return;
	}
	private Ctrl_Actions_Relays.Data	process_Ctrl_Actions_Relays_Request		()
	{
		Ctrl_Actions_Relays.Data 								message_return				= new Ctrl_Actions_Relays().new Data();
		message_return.burner 																= Global.burnerPower.isOn;
		message_return.pumpHotWater	 														= Global.pumps.fetchPump("Pump_Water").relay.isOn;
		message_return.pumpFloor	 														= Global.pumps.fetchPump("Pump_Floor").relay.isOn;
		message_return.pumpRadiator	 														= Global.pumps.fetchPump("Pump_Radiator").relay.isOn;
        return message_return;
	}
	private Ctrl_Actions_Relays.Data 	process_Ctrl_Actions_Relays_Execute		(Ctrl_Actions_Relays.Execute message_in)
	{
		// Action relays except for burner relay where prefer to use burner object
		// to have fuel flow measured and fuel supply controlled
		Relay													relay						= null;
		Burner													burner						= null;
		
		if (message_in.relayName.equalsIgnoreCase("Burner"))			burner				= Global.boiler.burner;
		else if (message_in.relayName.equalsIgnoreCase("HotWater"))		relay				= Global.pumps.fetchPump("Pump_Water").relay;
		else if (message_in.relayName.equalsIgnoreCase("Floor"))		relay				= Global.pumps.fetchPump("Pump_Floor").relay;
		else if (message_in.relayName.equalsIgnoreCase("Radiator"))		relay				= Global.pumps.fetchPump("Pump_Radiator").relay;

		if (relay != null)
		{
			if (message_in.relayAction == Ctrl_Actions_Relays.RELAY_On)
			{
				relay.on();
			}
			else if (message_in.relayAction == Ctrl_Actions_Relays.RELAY_Off)
			{
				relay.off();
			}
		}
		else if (burner != null)
		{
			if (message_in.relayAction == Ctrl_Actions_Relays.RELAY_On)
			{
				burner.powerOn();
			}
			else if (message_in.relayAction == Ctrl_Actions_Relays.RELAY_Off)
			{
				burner.powerOff();
			}
		}
		
		Ctrl_Actions_Relays.Data message_return												= new Ctrl_Actions_Relays().new Data();
		message_return.burner 																= Global.burnerPower.isOn;
		message_return.pumpHotWater	 														= Global.pumps.fetchPump("Pump_Water").relay.isOn;
		message_return.pumpFloor	 														= Global.pumps.fetchPump("Pump_Floor").relay.isOn;
		message_return.pumpRadiator	 														= Global.pumps.fetchPump("Pump_Radiator").relay.isOn;
		return message_return;
	}
	private Ctrl_Actions_Test_Mail.Ack	process_Ctrl_Actions_Test_Mail_Execute	()
	{
		Global.eMailMessage("Test", "This is a test mail");
		return new Ctrl_Actions_Test_Mail().new Ack();
	}
	private Msg__Abstract			process_Ctrl_Actions_Stop_Execute		(Ctrl_Actions_Stop.Execute message_in)
	{
		if ( (message_in.actionRequest == Ctrl_Actions_Stop.ACTION_Stop   		)
		||	 (message_in.actionRequest == Ctrl_Actions_Stop.ACTION_Reboot 		)
		||	 (message_in.actionRequest == Ctrl_Actions_Stop.ACTION_Restart		) 
		||	 (message_in.actionRequest == Ctrl_Actions_Stop.ACTION_ShutDown		) 
		||	 (message_in.actionRequest == Ctrl_Actions_Stop.ACTION_Debug_Wait	) 
		||	 (message_in.actionRequest == Ctrl_Actions_Stop.ACTION_Debug_NoWait	) )
		{
			Global.stopNow																	= true;
			Global.exitStatus																= message_in.actionRequest;	// 0 = stop app, 1 = restart app, 2 = reboot
			return	new Msg__Abstract().new Ack();
		}
		else if (message_in.actionRequest == Ctrl_Actions_Stop.ACTION_Reload_Configuration)
		{
			Global.stopNow																	= true;
			Global.exitStatus																= Ctrl_Actions_Stop.ACTION_Restart;	// 0 = stop app, 1 = restart app, 2 = reboot
			return	new Msg__Abstract().new Ack();
		}
		else if (message_in.actionRequest == Ctrl_Actions_Stop.ACTION_Reload_Calendars)
		{
			LogIt.display("TCP_Listener", "process_Ctrl_Actions_Stop_Execute", "Stopping all circuits");
			for (Circuit_Abstract circuit : Global.circuits.circuitList)
			{
				for (CircuitTask task : circuit.circuitTaskList)
				{
					task																	= null;
				}
				circuit.circuitTaskList														= null;
				circuit.requestShutDown();
			}
			// Now wait for each circuit to stop
			for (Circuit_Abstract circuit : Global.circuits.circuitList)
			{
				while (circuit.taskActive != null)
				{
					Global.waitSeconds(2);
					LogIt.display("TCP_Listener", "process_Ctrl_Actions_Stop_Execute", "Wait for circuit to stop : " + circuit.name);
				}
			}
			// TODO : Should we not stop the Background thread
			Global.tasksBackGround															= null;
			Global.awayList																	= null;
			try
			{
				Calendars										calendars 					= new Calendars();
			}
			catch (Exception e)
			{
				LogIt.display("TCP_Listener", "process_Ctrl_Actions_Stop_Execute", "Couldn't reload calendars " + e);
			}
			// TODO : Should we not restart the Background thread
			return	new Msg__Abstract().new Ack();
		}
		return new Msg__Abstract().new Nack();
    } 
	private Msg__Abstract		process_Ctrl_Fuel_Consumption_Update	(Ctrl_Fuel_Consumption.Update message_in)
	{
		if (Global.burner.isFuelFlowing())
		{
			return	new Msg__Abstract().new Nack("Fuel is currently Flowing"); 
		}
		
		Long													fuelConsumed				= Global.burner.fuelflow.consumption;
		
		try
		{
			LogIt.fuelData(Global.burner.fuelflow.consumption);
			Global.burner.fuelflow.consumption												= message_in.fuelConsumed;		// Set the value (usually zero)
			Global.burner.fuelflow.saveFuelFlow();						// Saves the current value to disk
			LogIt.fuelData(Global.burner.fuelflow.consumption);			// Saves the current value to server
		}
		catch (Exception ex)
		{
			// Something went wrong set thing back the way they were
			Global.burner.fuelflow.consumption												= fuelConsumed;
			LogIt.fuelData(Global.burner.fuelflow.consumption);
			Global.burner.fuelflow.saveFuelFlow();						// Saves the current entry
			return	new Ctrl_Fuel_Consumption().new Nack("A file or network error ocurred, command cancelled"); 
		}
		return	new Ctrl_Fuel_Consumption().new Ack();		// All Ok so Ack
	}
	

	private Ctrl_Thermo_List.Data		process_Ctrl_Thermo_List_Request		()	// Get a list of all thermometers either connected or not, in config file or not
	{
		Ctrl_Thermo_List.Data 										message_return				= new Ctrl_Thermo_List().new Data();			
		
		File 														mnt1Wire 					= new File("/mnt/1wire");
		File[] 														mnt1WireFiles	 			= mnt1Wire.listFiles();
		
		
		// Get list of probes connected to the system
		for (File mnt1WireFile : mnt1WireFiles)// (int i = 0; i < listOfFiles.length; i++) 
		{
			String 													fileName 					= mnt1WireFile.getName().replace("/mnt/1wire", "");
			if ((mnt1WireFile.isDirectory()) && (fileName.startsWith("28")))
			{
				Ctrl_Thermo_List.Thermo 							thermo 						= message_return.new Thermo();
				thermo.address 																	= fileName;
				message_return.thermos.add(thermo);
				
				// Now see if it is the list
				for (Thermometer thermometer : Global.thermometers.thermometerList)
				{
					for (Thermometer.Probe probe : thermometer.probes)
					{
						if (probe.address.replace(" ", "").equalsIgnoreCase(thermo.address))
						{
							thermo.name = thermometer.name;
							thermo.isLost = false;
							thermo.isNew = false;	
						}
					}
				}
			}
		}
		// For each configured probe, find it in the list of found probes. If it isn't, add it to the list
		for (Thermometer thermometer : Global.thermometers.thermometerList)
		{
			for (Thermometer.Probe probe : thermometer.probes)
			{
				Boolean found = false;
				for (Ctrl_Thermo_List.Thermo thermo : message_return.thermos)
				{
					if (probe.address.replace(" ", "").equalsIgnoreCase(thermo.address))
					{
						found = true;
					}
				}
				if (! found)				// Wasn't found so add it to the list
				{
					Ctrl_Thermo_List.Thermo 					thermo 						= message_return.new Thermo();
					thermo.name 															= thermometer.name;
					thermo.address 															= probe.address;
					thermo.isLost 															= true;
					message_return.thermos.add(thermo);
				}
			}
		}
		// All list members without a name are new (ie unconfigured)
		for (Ctrl_Thermo_List.Thermo thermo : message_return.thermos)
		{
			if (thermo.name == "") 								thermo.isNew 				= true;
		}
		int 													i;
		for (i = message_return.thermos.size() - 1; i >= 0; i--)
		{
			Ctrl_Thermo_List.Thermo 							thermo 						= message_return.thermos.get(i);
			if ((! thermo.isNew) && (! thermo.isLost))			message_return.thermos.remove(thermo);
			if (thermo.isNew)									thermo.temperature				= Global.thermometers.readNewProbe(thermo.address);
		}
		for (Ctrl_Thermo_List.Thermo thermo : message_return.thermos)
		{
	    	String 										part1								= thermo.address.substring(0, 7);
	    	String 										part2								= thermo.address.substring(7, 11);
	    	String 										part3								= thermo.address.substring(11,15);
	    	thermo.address																= part1 + " " + part2 + " " + part3;
		}
		return	message_return;		// All Ok so Ack
	}
}
 