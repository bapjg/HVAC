package eRegulation;
 
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import HVAC_Messages.*;


public class Thread_TCPListen <SendType> implements Runnable
{
	ServerSocket						UI_Server;
	Socket								UI_Socket;
	
	public void run()
    {
		LogIt.info("Thread_TCPListen", "Run", "Starting", true);            
 
		Ctrl_Abstract					message_in 							= null;
		Ctrl_Abstract					message_out 						= null;

		try
		{
			UI_Server														= new ServerSocket(8889);
			UI_Server.setSoTimeout(10 * 1000);
		
			while (!Global.stopNow)
			{
				try
				{
					UI_Socket												= UI_Server.accept();
					
			        ObjectInputStream 	input 								= new ObjectInputStream(UI_Socket.getInputStream());
			        // This previous line results in an EOFException
			        
			        message_in 												= (Ctrl_Abstract) input.readObject();
			        
			    	if (message_in == null)
			        {
						LogIt.info("Thread_TCPListen", "Run", "Null received from client", true);            
			            message_out 										= new Ctrl_Abstract().new Nack();
			        } 
			    	else if (message_in instanceof Ctrl_Temperatures.Request)
			        {
			            Ctrl_Temperatures.Data message_ou					= new Ctrl_Temperatures().new Data();
			            message_ou.dateTime 								= System.currentTimeMillis();

			            message_ou.tempBoiler	 							= Global.thermoBoiler.reading;
			            message_ou.tempBoilerIn	 							= Global.thermoBoilerIn.reading;
			            message_ou.tempBoilerOut	 						= Global.thermoBoilerOut.reading;
			    		
			            message_ou.tempFloorIn	 							= Global.thermoFloorIn.reading;
			            message_ou.tempFloorOut	 							= Global.thermoFloorOut.reading;
			    		
			            message_ou.tempRadiatorIn	 						= Global.thermoRadiatorIn.reading;
			            message_ou.tempRadiatorOut	 						= Global.thermoRadiatorOut.reading;

			            message_ou.tempHotWater	 							= Global.thermoHotWater.reading;
			            message_ou.tempOutside	 							= Global.thermoOutside.reading;
			            message_ou.tempLivingRoom	 						= Global.thermoLivingRoom.reading;
			            
			            message_out											= message_ou;
			        } 
			    	else if (message_in instanceof Ctrl_Immediate.Request)
			        {
						Ctrl_Immediate.Request	message_rcvd				= (Ctrl_Immediate.Request ) message_in;
						Ctrl_Immediate.Data 	message_ou					= new Ctrl_Immediate().new Data();
						
						String 					circuitName					= message_rcvd.circuitName;
						
						Circuit_Abstract 		circuit						= Global.circuits.fetchcircuit(circuitName);
						
						message_ou.circuitName								= circuitName;

						Long 					now							= Global.getTimeNowSinceMidnight();
						Long					midnight					= 24L * 60 * 60 * 1000;
						Long					nextStart					= midnight;
						Integer					tempObjective				= 0;
						CircuitTask				selectedTask				= null;

						if (circuit.taskActive == null)
						{
							for (CircuitTask aTask : circuit.circuitTaskList)			// Check to ensure there are no active tasks
							{
								if (	(aTask.days.contains(Global.getDayOfWeek(0))) 
								&& 		(! aTask.active)
								&&     	(aTask.timeStart > now )
								&&     	(aTask.timeStart < nextStart ))
								{
									nextStart								= aTask.timeStart;
									selectedTask							= aTask;
								}
							}
							
				            if (nextStart < midnight)	// Task currently inactive but planned
				            {
				            	message_ou.executionActive					= false;
								message_ou.executionPlanned					= true;
					            message_ou.timeStart	 					= nextStart;
					            message_ou.timeEnd		 					= selectedTask.timeEnd;
					            message_ou.tempObjective	 				= selectedTask.tempObjective;
								message_ou.stopOnObjective					= selectedTask.stopOnObjective;
				            }
				            else						// Task currently inactive and not even planned
				            {
				            	message_ou.executionActive					= false;
								message_ou.executionPlanned					= false;
								message_ou.timeStart						= 0L;
								message_ou.timeEnd							= 0L;
					            message_ou.tempObjective 					= 0;
					            message_ou.stopOnObjective					= false;
				            }
						}
						else		// Task currently active
						{
							message_ou.executionActive						= true;
							message_ou.executionPlanned						= false;
							message_ou.timeStart							= circuit.taskActive.timeStart;
							message_ou.timeEnd								= circuit.taskActive.timeEnd;
							message_ou.tempObjective						= circuit.taskActive.tempObjective;
							message_ou.stopOnObjective						= circuit.taskActive.stopOnObjective;
						}
			            
			            message_out											= message_ou;
			        } 
			    	else if (message_in instanceof Ctrl_Immediate.Execute)		//New New New
			        {
						Long	now											= Global.getTimeNowSinceMidnight();
						
						Ctrl_Immediate.Execute	message_rcvd				= (Ctrl_Immediate.Execute) message_in;
						String 					circuitName					= message_rcvd.circuitName;
						Circuit_Abstract 		circuit						= Global.circuits.fetchcircuit(circuitName);
						Ctrl_Abstract 			message_ou;
						
						if (message_rcvd.action == Ctrl_Immediate.ACTION_Start)
						{
							if (circuit.name.equalsIgnoreCase("Hot_Water"))
							{
								circuit.taskActive							= new CircuitTask(	now, 								// Time Start
																					now + 30 * 60 * 1000, 				// TimeEnd
																					message_rcvd.tempObjective,			// TempObjective in millidesrees
																					true,								// StopOnObjective
																					"1, 2, 3, 4, 5, 6, 7");				// Days
							}
							else
							{
								circuit.taskActive							= new CircuitTask(	now, 								// Time Start
																					message_rcvd.timeEnd, 				// TimeEnd
																					message_rcvd.tempObjective,			// TempObjective in millidesrees
																					false,								// StopOnObjective
																					"1, 2, 3, 4, 5, 6, 7");				// Days
							}
							circuit.start();
							message_ou										= new Ctrl_Abstract().new Ack();
						}
						else if (message_rcvd.action == message_rcvd.ACTION_Stop)
						{
							circuit.stop();
							message_ou										= new Ctrl_Abstract().new Ack();
						}
						else
						{
							message_ou										= new Ctrl_Abstract().new Nack();
						}
						
			            message_out											= message_ou;
			        } 
			    	else if (message_in instanceof Ctrl_Parameters.Request)
			        {
			    		Ctrl_Parameters.Data message_ou						= new Ctrl_Parameters().new Data();
			    		
			    		for (Thermometer globalThermometer : Global.thermometers.thermometerList)
			    		{
			    			Ctrl_Parameters.Thermometer paramThermometer	= new Ctrl_Parameters().new Thermometer();
			    			paramThermometer.name							= globalThermometer.name;
			    			paramThermometer.address						= globalThermometer.address;
			    			message_ou.thermometerList.add(paramThermometer);
			    		}

			    		for (Relay globalRelay : Global.relays.relayList)
			    		{
			    			Ctrl_Parameters.Relay 		paramRelay			= new Ctrl_Parameters().new Relay();
			    			paramRelay.name									= globalRelay.name;
			    			paramRelay.relayBank							= globalRelay.relayBank;
			    			paramRelay.relayNumber							= globalRelay.relayNumber;
			    			message_ou.relayList.add(paramRelay);
			    		}
			    		
			    		for (Pump globalPump : Global.pumps.pumpList)
			    		{
			    			Ctrl_Parameters.Pump 		paramPump			= new Ctrl_Parameters().new Pump();
			    			paramPump.name									= globalPump.name;
			    			message_ou.pumpList.add(paramPump);
			    		}

			    		for (Circuit_Abstract globalCircuit : Global.circuits.circuitList)
			    		{
			    			Ctrl_Parameters.Circuit		paramCircuit		= new Ctrl_Parameters().new Circuit();
			    			paramCircuit.name								= globalCircuit.name;
			    			paramCircuit.pump								= "pump"; //globalCircuit.relayBank;
			    			paramCircuit.thermometer						= "thermo"; //globalCircuit.relayNumber;
			    			paramCircuit.type								= globalCircuit.circuitType;
			    			message_ou.circuitList.add(paramCircuit);
			    		}
			    		message_out											= message_ou;
			        }
			    	else if (message_in instanceof Ctrl_Parameters.Update)
			        {
						Ctrl_Actions_Relays.Data message_ou					= new Ctrl_Actions_Relays().new Data();
			    		
			        }
			    	else if (message_in instanceof Ctrl_Actions_Relays.Request)
			        {
						Ctrl_Actions_Relays.Data message_ou					= new Ctrl_Actions_Relays().new Data();
			            message_ou.burner 									= Global.burnerPower.isOn();
			            message_ou.pumpHotWater	 							= Global.pumpWater.relay.isOn();
			            message_ou.pumpFloor	 							= Global.pumpFloor.relay.isOn();
			            message_ou.pumpRadiator	 							= Global.pumpRadiator.relay.isOn();
		            
			            message_out											= message_ou;
			        } 
			    	else if (message_in instanceof Ctrl_Actions_Relays.Execute)
			        {
						// Action relays except for burner relay where prefer to use burner object
			    		// to have fuel flow measured and fuel supply controlled
			    		Ctrl_Actions_Relays.Execute		relayAction			= (Ctrl_Actions_Relays.Execute) message_in;
						Relay							relay				= null;
						Burner							burner				= null;
						
						if (relayAction.relayName.equalsIgnoreCase("Burner"))
						{
							burner											= Global.boiler.burner;
						}
						else if (relayAction.relayName.equalsIgnoreCase("HotWater"))
						{
							relay											= Global.pumpWater.relay;
						}
						else if (relayAction.relayName.equalsIgnoreCase("Floor"))
						{
							relay											= Global.pumpFloor.relay;
						}
						else if (relayAction.relayName.equalsIgnoreCase("Radiator"))
						{
							relay											= Global.pumpRadiator.relay;
						}
						if (relay != null)
						{
							if (relayAction.relayAction == Ctrl_Actions_Relays.RELAY_On)
							{
								relay.on();
							}
							else if (relayAction.relayAction == Ctrl_Actions_Relays.RELAY_Off)
							{
								relay.off();
							}
						}
						else if (burner != null)
						{
							if (relayAction.relayAction == Ctrl_Actions_Relays.RELAY_On)
							{
								burner.powerOn();
							}
							else if (relayAction.relayAction == Ctrl_Actions_Relays.RELAY_Off)
							{
								burner.powerOff();
							}
						}
						
						Ctrl_Actions_Relays.Data message_ou					= new Ctrl_Actions_Relays().new Data();
			            message_ou.burner 									= Global.burnerPower.isOn();
			            message_ou.pumpHotWater	 							= Global.pumpWater.relay.isOn();
			            message_ou.pumpFloor	 							= Global.pumpFloor.relay.isOn();
			            message_ou.pumpRadiator	 							= Global.pumpRadiator.relay.isOn();
		            
			            message_out											= message_ou;
			        } 
			    	else if (message_in instanceof Ctrl_Actions_Test_Mail.Execute)
			        {
						Global.eMailMessage("Test", "This is a test mail");
						Ctrl_Actions_Test_Mail.Ack message_ou				= new Ctrl_Actions_Test_Mail().new Ack();
		            
			            message_out											= message_ou;
			        } 
			    	else if (message_in instanceof Ctrl_Actions_Stop.Execute)
			        {
						Global.stopNow										= true;
						Global.exitStatus									= ((Ctrl_Actions_Stop.Execute) message_in).exitStatus;
						Ctrl_Actions_Stop.Ack message_ou					= new Ctrl_Actions_Stop().new Ack();
		            
			            message_out											= message_ou;
			        } 

			        ObjectOutputStream 		output							= null;
					
					output 													= new ObjectOutputStream(UI_Socket.getOutputStream());
					output.writeObject(message_out);
			        output.flush();
			        output.close();
			        
			        if (Global.stopNow)
			        {
			        	Global.waitSeconds(2);								// If Ctrl_Actions_Stop received, allow Ack to go
			        }
				}
		        catch (ClassNotFoundException eCNF)
		        {
					LogIt.info("Thread_TCPListen", "Run", "Caught CNF", true);            
		            // message_out 							= new Message_Abstract().new Nack();
		        }
				catch (EOFException eEOF)
				{
					LogIt.info("Thread_TCPListen", "Run", "Caught EOF", true);            
					// Do nothing we will loop and do another 10s wait unless stopNow activated
				}
				catch (SocketTimeoutException eTO)
				{
					// Do nothing we will loop and do another 10s wait unless stopNow activated
				}
				catch (IOException eIO)
				{
					LogIt.info("Thread_TCPListen", "Run", "Caught IO", true);            
				}
				catch (Exception e)
				{
					LogIt.info("Thread_TCPListen", "Run", "Caught other", true);            
				}
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
 		LogIt.info("Thread_TCPListen", "Run", "Stopping", true);             
	}
}
 