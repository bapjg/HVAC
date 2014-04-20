package HVAC_Messages;

import java.util.ArrayList;


@SuppressWarnings("serial")
public class Ctrl_Configuration_New 			extends 					Ctrl_Abstract
{
	private static final long 					serialVersionUID 			= 1L;
	public final Integer						CIRCUIT_TYPE_HotWater		= 0;
	public final Integer						CIRCUIT_TYPE_Gradient		= 1;
	public final Integer						CIRCUIT_TYPE_Mixer			= 2;
	
	public Long									dateTime;

	public Ctrl_Configuration_New()
	{
	}
	
	public class Request 						extends 					Ctrl_Configuration_New
	{
	}
	public static class Data					extends 					Ctrl_Configuration_New
	{
		private static final long 				serialVersionUID 			= 1L;
		public ArrayList<PID_Data> 				pidList 					= new ArrayList<PID_Data>();
		public ArrayList<Thermometer> 			thermometerList 			= new ArrayList<Thermometer>();
		public ArrayList<Relay> 				relayList 					= new ArrayList<Relay>();
		public ArrayList<Pump> 					pumpList 					= new ArrayList<Pump>();
		public ArrayList<Circuit> 				circuitList 				= new ArrayList<Circuit>();
		public Burner					burner						= new Burner();
		public Boiler					Boiler						= new Boiler();
	}
	public class Update 				extends 					Ctrl_Configuration_New.Data
	{
		private static final long 		serialVersionUID 			= 1L;
	}
	public class Thermometer 			extends  					Ctrl_Configuration_New
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public String 					address;
		public String 					pidName;
	}
	public class Relay 					extends  					Ctrl_Configuration_New
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public Integer 					relayBank;
		public Integer 					relayNumber;
	}
	public class Pump 					extends  					Ctrl_Configuration_New
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public String 					relay;
	}
	public class Circuit 				extends  					Ctrl_Configuration_New
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public String 					pump;
		public String 					thermometer;
		public Integer 					type;
		public Integer					tempMax;
		public Mixer					mixer;
		public TempGradient				tempGradient;
	}
	public class Burner 				extends  					Ctrl_Configuration_New
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					relay;
		public Long 					fuelConsumption;
	}
	public class Boiler 				extends  					Ctrl_Configuration_New
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					relay;
		public String 					thermometer;
		public Integer					tempNeverExceed;
		public Integer					tempOverShoot;
	}
	public class TempGradient 			extends  					Ctrl_Configuration_New
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public Integer 					outsideLow;
		public Integer					outsideHigh;
		public Integer 					tempLow;
		public Integer					tempHigh;
	}
	public class PID_Data				extends  					Ctrl_Configuration_New
	{
		public String					name;
		public Integer					depth;
		public Integer					sampleIncrement;
	}
	public class PID_Params				extends  					Ctrl_Configuration_New
	{
		public String					thermometer;
		public Float					gainP;
		public Float					timeD;
		public Float					gainI;
		public Float					timeI;
	}
	public class Mixer					extends  					Ctrl_Configuration_New
	{
		public String					name;
		public Integer					swingTime;
		public Long						lagTime;
		public PID_Params				pidParams;
	}
	public void initialise()
	{
		Ctrl_Configuration_New.Update	confUpdate					= new Ctrl_Configuration_New.Update();
		confUpdate.dateTime											= 99L; //Must set time
		
		// PIDs
		Ctrl_Configuration_New.PID_Data	pid							= new Ctrl_Configuration_New.PID_Data();
		pid.name													= "Floor_In";
		pid.depth													= 10;
		pid.sampleIncrement											= 1;
		confUpdate.pidList.add(pid);
		
		pid															= new Ctrl_Configuration_New.PID_Data();
		pid.name													= "Floor_Out";
		pid.depth													= 10;
		pid.sampleIncrement											= 1;
		confUpdate.pidList.add(pid);
		
		pid															= new Ctrl_Configuration_New.PID_Data();
		pid.name													= "Boiler_Out";
		pid.depth													= 10;
		pid.sampleIncrement											= 1;
		confUpdate.pidList.add(pid);
		
		// Thermometers
		Ctrl_Configuration_New.Thermometer		thermometer			= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Boiler";
		thermometer.address											= "28.3F9A 8504 0000";
		thermometer.pidName											= null;
		confUpdate.thermometerList.add(thermometer);

		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Boiler_Old";
		thermometer.address											= "28.1C01 8F04 0000";
		thermometer.pidName											= null; 		
		confUpdate.thermometerList.add(thermometer);
		
		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Floor_In";
		thermometer.address											= "28.629F 8E04 0000";
		thermometer.pidName											= "Floor_In"; 		
		confUpdate.thermometerList.add(thermometer);
		
		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Floor_Out";
		thermometer.address											= "28.4492 8E04 0000";
		thermometer.pidName											= "Floor_Out";		
		confUpdate.thermometerList.add(thermometer);
		
		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Boiler_Out";
		thermometer.address											= "28.69DA 8E04 0000";
		thermometer.pidName											= "Boiler_Out";	
		confUpdate.thermometerList.add(thermometer);
		
		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Boiler_In";
		thermometer.address											= "28.2C06 8E04 0000";
		thermometer.pidName											= null; 		
		confUpdate.thermometerList.add(thermometer);
		
		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Radiator_Out";
		thermometer.address											= "28.A194 8E04 0000";
		thermometer.pidName											= null; 		
		confUpdate.thermometerList.add(thermometer);

		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Radiator_In";
		thermometer.address											= "28.76EF 8E04 0000";
		thermometer.pidName											= null; 		
		confUpdate.thermometerList.add(thermometer);

		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Outside";
		thermometer.address											= "28.41DB 8E04 0000";
		thermometer.pidName											= null; 
		confUpdate.thermometerList.add(thermometer);
		
		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Living_Room";
		thermometer.address											= "28.90DB 8E04 0000";
		thermometer.pidName											= "Living_Room" ;
		confUpdate.thermometerList.add(thermometer);
		
		thermometer													= new Ctrl_Configuration_New.Thermometer();
		thermometer.name											= "Hot_Water";
		thermometer.address											= "28.09E1 8504 0000";
		thermometer.pidName											= null; 		
		confUpdate.thermometerList.add(thermometer);
		
		// Relays
		Ctrl_Configuration_New.Relay			relay				= new Ctrl_Configuration_New.Relay();
		relay.name 													= "Burner";
		relay.relayBank												= 0;
		relay.relayNumber 											= 0;
		confUpdate.relayList.add(relay);
		 
		relay														= new Ctrl_Configuration_New.Relay();
		relay.name 													= "Mixer_Up";
		relay.relayBank												= 0;
		relay.relayNumber 											= 1;
		confUpdate.relayList.add(relay);
		 
		relay														= new Ctrl_Configuration_New.Relay();
		relay.name 													= "Mixer_Down";
		relay.relayBank												= 0;
		relay.relayNumber 											= 2;
		confUpdate.relayList.add(relay);
		 
		relay														= new Ctrl_Configuration_New.Relay();
		relay.name 													= "Pump_Water";
		relay.relayBank												= 0;
		relay.relayNumber 											= 3;
		confUpdate.relayList.add(relay);
		 
		relay														= new Ctrl_Configuration_New.Relay();
		relay.name 													= "Pump_Radiator";
		relay.relayBank												= 0;
		relay.relayNumber 											= 4;
		confUpdate.relayList.add(relay);
		 
		relay														= new Ctrl_Configuration_New.Relay();
		relay.name 													= "Pump_Floor";
		relay.relayBank												= 0;
		relay.relayNumber 											= 5;
		confUpdate.relayList.add(relay);
		 
		// Pumps
		Ctrl_Configuration_New.Pump			pump					= new Ctrl_Configuration_New.Pump();
		pump.name 													= "Pump_Water";
		pump.relay													= "Pump_Water";
		confUpdate.pumpList.add(pump);
		
		pump														= new Ctrl_Configuration_New.Pump();
		pump.name 													= "Pump_Radiator";
		pump.relay													= "Pump_Radiator";
		confUpdate.pumpList.add(pump);
		
		pump														= new Ctrl_Configuration_New.Pump();
		pump.name 													= "Pump_Floor";
		pump.relay													= "Pump_Floor";
		confUpdate.pumpList.add(pump);
		
		// Circuits
		Ctrl_Configuration_New.Circuit		circuit					= new Ctrl_Configuration_New.Circuit();
		circuit.name 												= "Hot_Water";
		circuit.pump												= "Hot_Water";
		circuit.thermometer											= "Hot_Water";
		circuit.type												= 2;
		circuit.tempMax												= 75000;
		circuit.mixer												= null;
		circuit.tempGradient										= null;
		confUpdate.circuitList.add(circuit);
		
		circuit														= new Ctrl_Configuration_New.Circuit();
		circuit.name 												= "Radiator";
		circuit.pump												= "Radiator";
		circuit.thermometer											= "Radiator";
		circuit.type												= 1;
		circuit.tempMax												= 90000;
		circuit.mixer												= null;
		circuit.tempGradient										= new TempGradient();
		circuit.tempGradient.outsideLow								= -15000;
		circuit.tempGradient.tempLow								= 80000;
		circuit.tempGradient.outsideHigh							= 15000;
		circuit.tempGradient.tempHigh								= 35000;
		confUpdate.circuitList.add(circuit);
		
		circuit														= new Ctrl_Configuration_New.Circuit();
		circuit.name 												= "Floor";
		circuit.pump												= "Floor";
		circuit.thermometer											= "Floor";
		circuit.type												= 2;
		circuit.tempMax												= 50000;
		circuit.mixer												= new Mixer();
		circuit.mixer.swingTime										= 92;
		circuit.mixer.lagTime										= 20000L;
		circuit.mixer.pidParams										= new PID_Params();
		circuit.mixer.pidParams.thermometer							= "Living_Room";
		circuit.mixer.pidParams.gainP								= 0.9F;
		circuit.mixer.pidParams.timeD								= 90F;
		circuit.mixer.pidParams.gainI								= 0F;
		circuit.mixer.pidParams.timeI								= 0F;		
		circuit.tempGradient										= new TempGradient();
		circuit.tempGradient.outsideLow								= -15000;
		circuit.tempGradient.tempLow								= 45000;
		circuit.tempGradient.outsideHigh							= 15000;
		circuit.tempGradient.tempHigh								= 30000;
		confUpdate.circuitList.add(circuit);
	}
}
