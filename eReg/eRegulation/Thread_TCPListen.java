package eRegulation;
 
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import eRegulation.Ctrl_Temperatures.Request;
import eRegulation.Ctrl_Temperatures.Response;
 
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
			        message_in 												= (Ctrl_Abstract) input.readObject();
			        
			    	if (message_in == null)
			        {
			            System.out.println("Null received from client");
			            message_out 										= new Ctrl_Abstract().new Nack();
			        } 
			    	else if (message_in instanceof Ctrl_Temperatures.Request)
			        {
			    		Ctrl_Temperatures.Response message_ou				= new Ctrl_Temperatures().new Response();
			            message_ou.dateTime 								= 3L;
			            message_ou.date 									= "01/01/2001";
			            message_ou.time  									= "15:15:15";
			            message_ou.tempBoiler	 							= 10;
			            message_ou.tempBoilerIn	 							= 10;
			            message_ou.tempBoilerOut	 						= 10;
			    		
			            message_ou.tempFloorIn	 							= 10;
			            message_ou.tempFloorOut	 							= 10;
			    		
			            message_ou.tempRadiatorOut	 						= 10;
			            message_ou.tempRadiatorIn	 						= 10;
			            message_ou.tempHotWater	 							= 10;
			            message_ou.tempOutside	 							= 10;
			            message_ou.tempLivingRoom	 						= 10;
			            
			            message_out											= message_ou;
			        } 
			        ObjectOutputStream 		output							= null;;
					
					output 													= new ObjectOutputStream(UI_Socket.getOutputStream());
					output.writeObject(message_out);
			        output.flush();
			        output.close();
				}
		        catch (ClassNotFoundException eCNF)
		        {
		        	System.out.println("Caught CNF");
		        	eCNF.printStackTrace();
		            // message_out 							= new Message_Abstract().new Nack();
		        }
				catch (SocketTimeoutException eTO)
				{
					// Do nothing we will loop and do another 10s wait unless stopNow activated
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
 		LogIt.info("Thread_TCPListen", "Run", "Stopping", true);             
	}
}
 