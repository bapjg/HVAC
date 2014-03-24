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
 
        Object 							message_in 							= null;
        Message_Abstract				message_out 						= null;

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
			        message_in 												= input.readObject();
			        
			    	if (message_in == null)
			        {
			            System.out.println("Null received from client");
			            (Ctrl_Abstract.Nack) message_out 						= new Ctrl_Abstract().new Nack();
			        } 
			    	else if (message_in instanceof Ctrl_Temperatures.Request)
			        {
			    		Ctrl_Temperatures.Response message_out				= new Ctrl_Temperatures().new Response();
			            message_out.dateTime 								= 3L;
			            message_out.date 									= "01/01/2001";
			            message_out.time  									= "15:15:15";
			            message_out.tempBoiler	 							= 10;
			            message_out.tempBoilerIn	 						= 10;
			            message_out.tempBoilerOut	 						= 10;
			    		
			            message_out.tempFloorIn	 							= 10;
			            message_out.tempFloorOut	 						= 10;
			    		
			            message_out.tempRadiatorOut	 						= 10;
			            message_out.tempRadiatorIn	 						= 10;

			            message_out.tempHotWater	 						= 10;
			            message_out.tempOutside	 							= 10;
			            message_out.tempLivingRoom	 						= 10;
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
 