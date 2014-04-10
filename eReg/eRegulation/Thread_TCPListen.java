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
			LogIt.info("Thread_TCPListen", "Run", "Creating Objects", true);            
			UI_Server														= new ServerSocket(8889);
			UI_Server.setSoTimeout(10 * 1000);
		
			while (!Global.stopNow)
			{
				try
				{
					UI_Socket												= UI_Server.accept();
					
					LogIt.info("Thread_TCPListen", "Run", "Socket accepted, port " + UI_Socket.getPort() + ", AD " + UI_Socket.getRemoteSocketAddress(), true);            
			        
			        ObjectInputStream 	input 								= new ObjectInputStream(UI_Socket.getInputStream());
			        // This previous line results in an EOFException
			        
			        message_in 												= (Ctrl_Abstract) input.readObject();
					LogIt.info("Thread_TCPListen", "Run", "message_in read", true);            
			        
			    	if (message_in == null)
			        {
						LogIt.info("Thread_TCPListen", "Run", "Null received from client", true);            
			            message_out 										= new Ctrl_Abstract().new Nack();
			        } 
			    	else if (message_in instanceof Ctrl_Temperatures.Ping)
			        {
						LogIt.info("Thread_TCPListen", "Run", "Ping received from client", true);            
			            Ctrl_Abstract.Ack message_ou						= new Ctrl_Abstract().new Ack();
						LogIt.info("Thread_TCPListen", "Run", "Ack to Ping prepared", true);            
			            
			            message_out											= message_ou;
			        }
			    	else if (message_in instanceof Ctrl_Temperatures.Request)
			        {
						LogIt.info("Thread_TCPListen", "Run", "Temp.Req Message received from client", true);            
			            Ctrl_Temperatures.Data message_ou				= new Ctrl_Temperatures().new Data();
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
			    	else if (message_in instanceof Ctrl_Actions_HotWater.Request)
			        {
						LogIt.info("Thread_TCPListen", "Run", "HW.Req Message received from client", true);            
						Ctrl_Actions_HotWater.Data message_ou				= new Ctrl_Actions_HotWater().new Data();
						
						Circuit_HotWater data_hw							= Global.circuitHotWater;

						Long 					now							= Global.getTimeNowSinceMidnight();
						Long					midnight					= 24L * 60 * 60 * 1000;
						Long					nextStart					= midnight;
						Integer					tempObjective				= 0;

						if (data_hw.taskActive == null)
						{
							for (CircuitTask aTask : data_hw.circuitTaskList)			// Check to ensure there are no active tasks
							{
								if (	(aTask.days.contains(Global.getDayOfWeek(0))) 
								&& 		(! aTask.active)
								&&     	(aTask.timeStart > now )
								&&     	(aTask.timeStart < nextStart ))
								{
									nextStart								= aTask.timeStart;
									tempObjective							= aTask.tempObjective;
								}
							}
							
				            if (nextStart < midnight)	// Task currently inactive but planned
				            {
				            	message_ou.executionActive					= false;
								message_ou.executionPlanned					= true;
					            message_ou.timeStart	 					= nextStart;
					            message_ou.tempObjective	 				= tempObjective;
				            }
				            else						// Task currently inactive and not even planned
				            {
				            	message_ou.executionActive					= false;
								message_ou.executionPlanned					= false;
				            	message_ou.timeStart	 					= 0L;
					            message_ou.tempObjective 					= 0;
				            }
						}
						else		// Task currently active
						{
							message_ou.executionActive						= true;
							message_ou.executionPlanned						= false;
							message_ou.timeStart							= 0L;
							message_ou.tempObjective						= data_hw.taskActive.tempObjective;
						}
			            
			            message_out											= message_ou;
			        } 
			    	else if (message_in instanceof Ctrl_Actions_HotWater.Execute)
			        {
						LogIt.info("Thread_TCPListen", "Run", "HW.Execute Message received from client", true);            
						Long	now											= Global.getTimeNowSinceMidnight();
						
						Global.circuitHotWater.taskActive					= new CircuitTask(	now, 								// Time Start
																								now + 30 * 60 * 1000, 				// TimeEnd
																								((Ctrl_Actions_HotWater.Execute) message_in).tempObjective,		// TempObjective in millidesrees
																								true,								// StopOnObjective
																								"1, 2, 3, 4, 5, 6, 7");				// Days
						Global.circuitHotWater.start();
						Ctrl_Abstract.Ack message_ou						= new Ctrl_Abstract().new Ack();
						
			            message_out											= message_ou;
			        } 
			    	else if (message_in instanceof Ctrl_Immediate.Request)
			        {
						LogIt.info("Thread_TCPListen", "Run", "Immediate.Req Message received from client", true);            

						Ctrl_Immediate.Request	message_rcvd				= (Ctrl_Immediate.Request ) message_in;
						Ctrl_Immediate.Data 	message_ou					= new Ctrl_Immediate().new Data();
						
						String 					circuitName					= message_rcvd.circuitName;
						
						Circuit_Abstract 		circuit						= Global.circuits.fetchcircuit(circuitName);
						
						LogIt.info("Thread_TCPListen", "Run", "             and it's from " + circuitName, true);            
				
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
						LogIt.info("Thread_TCPListen", "Run", "HW.Execute Message received from client", true);            
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

			        ObjectOutputStream 		output							= null;
					
					output 													= new ObjectOutputStream(UI_Socket.getOutputStream());
					output.writeObject(message_out);
			        output.flush();
			        output.close();
					LogIt.info("Thread_TCPListen", "Run", "Responded", true);            
					LogIt.info("Thread_TCPListen", "Run", "=============================================", true);            
				}
		        catch (ClassNotFoundException eCNF)
		        {
					LogIt.info("Thread_TCPListen", "Run", "Caught CNF", true);            
		        	eCNF.printStackTrace();
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
					eIO.printStackTrace();
				}
				catch (Exception e)
				{
					LogIt.info("Thread_TCPListen", "Run", "Caught other", true);            
					e.printStackTrace();
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
 