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
			LogIt.info("Thread_TCPListen", "Run", "Server Created", true);            
		
			while (!Global.stopNow)
			{
				try
				{
					UI_Socket												= UI_Server.accept();
					
					LogIt.info("Thread_TCPListen", "Run", "Socket created, port " + UI_Socket.getPort() + ", AD " + UI_Socket.getRemoteSocketAddress(), true);            
			        
			        ObjectInputStream 	input 								= new ObjectInputStream(UI_Socket.getInputStream());
			        // This previous line results in an EOFException
			        
					LogIt.info("Thread_TCPListen", "Run", "input created and now readObject", true);            
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
						}
						else
						{
							nextStart										= -1L;
						}
			            message_ou.dateTime 								= now;
			            if (nextStart < midnight)
			            {
				            message_ou.timeStart	 						= nextStart;
				            message_ou.tempObjective	 					= tempObjective;
			            }
			            else
			            {
				            message_ou.timeStart	 						= 0L;
				            message_ou.tempObjective 						= 0;
			            }
			            
			            message_out											= message_ou;
			        } 
			    	else if (message_in instanceof Ctrl_Actions_HotWater.Update)
			        {
						LogIt.info("Thread_TCPListen", "Run", "HW.Upd Message received from client", true);            
						Ctrl_Abstract.Ack message_ou				= new Ctrl_Abstract().new Ack();
			            message_out											= message_ou;
			        } 
			        ObjectOutputStream 		output							= null;
					LogIt.info("Thread_TCPListen", "Run", "Will Respond", true);            
					
					output 													= new ObjectOutputStream(UI_Socket.getOutputStream());
					output.writeObject(message_out);
			        output.flush();
			        output.close();
					LogIt.info("Thread_TCPListen", "Run", "Responded", true);            
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
 