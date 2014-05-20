package HVAC_Messages;

import java.util.ArrayList;


@SuppressWarnings("serial")
public class Ctrl_Configuration 				extends 					Ctrl__Abstract
{
	private static final long 					serialVersionUID 			= 1L;
	public final transient Integer				CIRCUIT_TYPE_HotWater		= 0;
	public final transient Integer				CIRCUIT_TYPE_Gradient		= 1;
	public final transient Integer				CIRCUIT_TYPE_Mixer			= 2;
	
	public Long									dateTime;

	public Ctrl_Configuration()
	{
	}
	
	public class Request 						extends 					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
	}
	public static class Data					extends 					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public ArrayList<PID_Data> 				pidList 					= new ArrayList<PID_Data>();
		public ArrayList<Thermometer> 			thermometerList 			= new ArrayList<Thermometer>();
		public ArrayList<Relay> 				relayList 					= new ArrayList<Relay>();
		public ArrayList<Pump> 					pumpList 					= new ArrayList<Pump>();
		public ArrayList<Circuit> 				circuitList 				= new ArrayList<Circuit>();
		public Boiler							boiler						= new Boiler();
		public Burner							burner						= new Burner();
		public ArrayList<String>				eMailList					= new ArrayList<String>();
	}
	public class Update 						extends 					Ctrl_Configuration.Data
	{
		private static final long 				serialVersionUID 			= 1L;
	}
	public class Thermometer 					extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public String 							address;
		public String 							pidName;
	}
	public class Relay 							extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public Integer 							relayBank;
		public Integer 							relayNumber;
	}
	public class Pump 							extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public String 							relay;
	}
	public class Circuit 						extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public String 							pump;
		public String 							thermometer;
		public Integer 							type;
		public Integer							tempMax;
		public Mixer							mixer;
		public TempGradient						tempGradient;
	}
	public class Burner 						extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							relay;
		public Long 							fuelConsumption;
	}
	public class Boiler 						extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							thermometer;
		public Integer							tempNeverExceed;
		public Integer							tempOverShoot;
	}
	public class TempGradient 					extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public Integer 							outsideLow;
		public Integer							outsideHigh;
		public Integer 							tempLow;
		public Integer							tempHigh;
	}
	public class PID_Data						extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String							name;
		public Integer							depth;
		public Integer							sampleIncrement;
	}
	public class PID_Params						extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String							thermometer;
		public Float							gainP;
		public Float							timeD;
		public Float							gainI;
		public Float							timeI;
		public Integer							timeDelay;
		public Integer							timeProjection;
		public Integer							marginProjection;
	}
	public class Mixer							extends  					Ctrl_Configuration
	{
		private static final long 				serialVersionUID 			= 1L;
		public String							name;
		public Integer							swingTime;
		public String							relayUp;
		public String							relayDown;
		public PID_Params						pidParams;
	}
	public void initialise()
	{
		Update	confUpdate													= (Update) this;
		confUpdate.dateTime													= null;			// Server is used as utlimate arbitrator for time
		
		// PIDs
		PID_Data								pid							= new PID_Data();
		pid.name															= "Floor_In";
		pid.depth															= 10;
		pid.sampleIncrement													= 90;		// 90 x 10s = 900s = 15 mins
		confUpdate.pidList.add(pid);
		
		pid																	= new PID_Data();
		pid.name															= "Floor_Out";
		pid.depth															= 10;
		pid.sampleIncrement													= 1;
		confUpdate.pidList.add(pid);
		
		pid																	= new PID_Data();
		pid.name															= "Boiler_Out";
		pid.depth															= 10;
		pid.sampleIncrement													= 1;
		confUpdate.pidList.add(pid);

		pid																	= new PID_Data();
		pid.name															= "Living_Room";
		pid.depth															= 10;
		pid.sampleIncrement													= 180;		// 180 x 10s = 900s = 30 mins
		confUpdate.pidList.add(pid);

		// Thermometers
		Thermometer								thermometer					= new Thermometer();
		thermometer.name													= "Boiler";
		thermometer.address													= "28.3F9A 8504 0000";
		thermometer.pidName													= null;
		confUpdate.thermometerList.add(thermometer);

		thermometer															= new Thermometer();
		thermometer.name													= "Boiler_Old";
		thermometer.address													= "28.1C01 8F04 0000";
		thermometer.pidName													= null; 		
		confUpdate.thermometerList.add(thermometer);
		
		thermometer															= new Thermometer();
		thermometer.name													= "Floor_In";
		thermometer.address													= "28.629F 8E04 0000";
		thermometer.pidName													= "Floor_In"; 		
		confUpdate.thermometerList.add(thermometer);		
				
		thermometer															= new Thermometer();
		thermometer.name													= "Floor_Out";
		thermometer.address													= "28.4492 8E04 0000";
		thermometer.pidName													= "Floor_Out";		
		confUpdate.thermometerList.add(thermometer);		
				
		thermometer															= new Thermometer();
		thermometer.name													= "Boiler_Out";
		thermometer.address													= "28.69DA 8E04 0000";
		thermometer.pidName													= "Boiler_Out";	
		confUpdate.thermometerList.add(thermometer);		
				
		thermometer															= new Thermometer();
		thermometer.name													= "Boiler_In";
		thermometer.address													= "28.2C06 8E04 0000";
		thermometer.pidName													= null; 		
		confUpdate.thermometerList.add(thermometer);
		
		thermometer															= new Thermometer();
		thermometer.name													= "Radiator_Out";
		thermometer.address													= "28.A194 8E04 0000";
		thermometer.pidName													= null; 		
		confUpdate.thermometerList.add(thermometer);		
		
		thermometer															= new Thermometer();
		thermometer.name													= "Radiator_In";
		thermometer.address													= "28.76EF 8E04 0000";
		thermometer.pidName													= null; 		
		confUpdate.thermometerList.add(thermometer);		
		
		thermometer															= new Thermometer();
		thermometer.name													= "Outside";
		thermometer.address													= "28.41DB 8E04 0000";
		thermometer.pidName													= null; 
		confUpdate.thermometerList.add(thermometer);		
				
		thermometer															= new Thermometer();
		thermometer.name													= "Living_Room";
		thermometer.address													= "28.90DB 8E04 0000";
		thermometer.pidName													= "Living_Room" ;
		confUpdate.thermometerList.add(thermometer);		
				
		thermometer															= new Thermometer();
		thermometer.name													= "Hot_Water";
		thermometer.address													= "28.09E1 8504 0000";
		thermometer.pidName													= null; 		
		confUpdate.thermometerList.add(thermometer);
		
		// Relays
		Relay									relay						= new Relay();
		relay.name 															= "Burner";
		relay.relayBank														= 0;
		relay.relayNumber 													= 0;
		confUpdate.relayList.add(relay);
		 
		relay																= new Relay();
		relay.name 															= "Mixer_Up";
		relay.relayBank														= 0;
		relay.relayNumber 													= 1;
		confUpdate.relayList.add(relay);
		 
		relay																= new Relay();
		relay.name 															= "Mixer_Down";
		relay.relayBank														= 0;
		relay.relayNumber 													= 2;
		confUpdate.relayList.add(relay);		
				
		relay																= new Relay();
		relay.name 															= "Pump_Water";
		relay.relayBank														= 0;
		relay.relayNumber 													= 3;
		confUpdate.relayList.add(relay);		
				
		relay																= new Relay();
		relay.name 															= "Pump_Radiator";
		relay.relayBank														= 0;
		relay.relayNumber 													= 4;
		confUpdate.relayList.add(relay);		
				
		relay																= new Relay();
		relay.name 															= "Pump_Floor";
		relay.relayBank														= 0;
		relay.relayNumber 													= 5;
		confUpdate.relayList.add(relay);
		 
		// Pumps
		Pump									pump						= new Pump();
		pump.name 															= "Pump_Water";
		pump.relay															= "Pump_Water";
		confUpdate.pumpList.add(pump);		
				
		pump																= new Pump();
		pump.name 															= "Pump_Radiator";
		pump.relay															= "Pump_Radiator";
		confUpdate.pumpList.add(pump);		
				
		pump																= new Pump();
		pump.name 															= "Pump_Floor";
		pump.relay															= "Pump_Floor";
		confUpdate.pumpList.add(pump);
		
		// Circuits
		Circuit									circuit						= new Circuit();
		circuit.name 														= "Hot_Water";
		circuit.pump														= "Pump_Water";
		circuit.thermometer													= "Hot_Water";
		circuit.type														= CIRCUIT_TYPE_HotWater;
		circuit.tempMax														= 75000;
		circuit.mixer														= null;
		circuit.tempGradient												= null;
		confUpdate.circuitList.add(circuit);
		
		circuit																= new Circuit();
		circuit.name 														= "Radiator";
		circuit.pump														= "Pump_Radiator";
		circuit.thermometer													= "Radiator_Out";
		circuit.type														= CIRCUIT_TYPE_Gradient;
		circuit.tempMax														= 90000;
		circuit.mixer														= null;
		circuit.tempGradient												= new TempGradient();
		circuit.tempGradient.outsideLow										= -15000;
		circuit.tempGradient.tempLow										= 80000;
		circuit.tempGradient.outsideHigh									= 15000;
		circuit.tempGradient.tempHigh										= 35000;
		confUpdate.circuitList.add(circuit);
		
		circuit																= new Circuit();
		circuit.name 														= "Floor";
		circuit.pump														= "Pump_Floor";
		circuit.thermometer													= "Living_Room";
		circuit.type														= CIRCUIT_TYPE_Mixer;
		circuit.tempMax														= 50000;
		circuit.mixer														= new Mixer();
		circuit.mixer.swingTime												= 92000;
		circuit.mixer.relayUp												= "Mixer_Up";
		circuit.mixer.relayDown												= "Mixer_Down";
		circuit.mixer.pidParams												= new PID_Params();
		circuit.mixer.pidParams.thermometer									= "Floor_Out";
		circuit.mixer.pidParams.gainP										= 0.9F;
		circuit.mixer.pidParams.timeD										= 90F;
		circuit.mixer.pidParams.gainI										= 0F;
		circuit.mixer.pidParams.timeI										= 0F;		
		circuit.mixer.pidParams.timeDelay									= 30000;
		circuit.mixer.pidParams.timeProjection								= 50000;
		circuit.mixer.pidParams.marginProjection							= 2000;
		circuit.tempGradient												= new TempGradient();
		circuit.tempGradient.outsideLow										= -15000;
		circuit.tempGradient.tempLow										= 45000;
		circuit.tempGradient.outsideHigh									= 15000;
		circuit.tempGradient.tempHigh										= 30000;
		confUpdate.circuitList.add(circuit);
		
		// Boiler/Burner
		Boiler									boiler						= new Boiler();
		boiler.thermometer													= "Boiler";
		boiler.tempNeverExceed												= 95000;
		boiler.tempOverShoot												= 18000;
		confUpdate.boiler													= boiler;
		// Boiler/Burner
		Burner									burner						= new Burner();
		burner.relay														= "Burner";
		burner.fuelConsumption												= 0L;
		confUpdate.burner													= burner;
		
		confUpdate.eMailList.add(new String("andre@bapjg.com"));
		confUpdate.eMailList.add(new String("brigitte@bapjg.com"));
	}
}
