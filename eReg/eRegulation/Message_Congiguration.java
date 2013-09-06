package eRegulation;

import java.util.ArrayList;



public class Message_Congiguration  extends Message_Abstract
{
	@SuppressWarnings("unused")
	private static final long 				serialVersionUID 	= 99003L;
	
	public ArrayList<Message_Circuit> 		circuitList 		= new ArrayList <Message_Circuit> ();
	public ArrayList<Message_Relay> 		relayList 			= new ArrayList <Message_Relay> ();
	public ArrayList<Message_Thermometer> 	thermometerList 	= new ArrayList <Message_Thermometer> ();
	public ArrayList<Message_PID> 			pidList 			= new ArrayList <Message_PID> ();
	
	public Message_Congiguration()
	{
		
	}
}

