package HVAC_Messages;

import java.util.ArrayList;


@SuppressWarnings("serial")
public class Ctrl_Parameters 			extends 					Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;
	public final Integer				CIRCUIT_TYPE_HotWater		= 0;
	public final Integer				CIRCUIT_TYPE_Gradient		= 1;
	public final Integer				CIRCUIT_TYPE_Mixer			= 2;

	public Ctrl_Parameters()
	{
	}
	
	public class Request 				extends 					Ctrl_Parameters
	{
	}
	public class Data 					extends 					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public ArrayList<Thermometer> 	thermometerList 			= new ArrayList<Thermometer>();
		public ArrayList<Relay> 		relayList 					= new ArrayList<Relay>();
		public ArrayList<Pump> 			pumpList 					= new ArrayList<Pump>();
		public ArrayList<Circuit> 		circuitList 				= new ArrayList<Circuit>();
		public Burner					burner						= new Burner();
		public Boiler					Boiler						= new Boiler();
	}
	public class Update 				extends 					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public ArrayList<Thermometer> 	thermometerList 			= new ArrayList<Thermometer>();
		public ArrayList<Relay> 		relayList 					= new ArrayList<Relay>();
		public ArrayList<Pump> 			pumpList 					= new ArrayList<Pump>();
	}
	public class Thermometer 			extends  					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public String 					address;
	}
	public class Relay 					extends  					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public Integer 					relayBank;
		public Integer 					relayNumber;
	}
	public class Pump 					extends  					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public String 					relay;
	}
	public class Circuit 				extends  					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public String 					pump;
		public String 					thermometer;
		public Integer 					type;
	}
	public class Burner 				extends  					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					relay;
		public Long 					fuelConsumption;
	}
	public class Boiler 				extends  					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					relay;
		public String 					thermometer;
		public Integer					tempNeverExceed;
		public Integer					tempOverShoot;
	}
}
