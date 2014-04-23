package eRegulation;

import java.util.ArrayList;

public class Circuits implements java.io.Serializable
{
	private static final 	long 		serialVersionUID = 999999L;
	
	public ArrayList<Circuit_Abstract> circuitList = new ArrayList<Circuit_Abstract>();
	
//	public void add
//		(
//		String 			name, 
//		String 			friendlyName,  
//		String			circuitType, 
//		String			tempMax, 
//		String			rampUpTime
//		)
//	{
//		//Berk berk berkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
//		
//		Integer			circuitTypeInteger	=	Integer.parseInt(circuitType);
//		
//		if (circuitTypeInteger == Circuit_Abstract.CIRCUIT_TYPE_HotWater)
//		{
//			Circuit_HotWater circuitItem = new Circuit_HotWater(name, friendlyName, circuitTypeInteger, tempMax, rampUpTime);
//			circuitList.add(circuitItem);
//		}
//		else if (circuitTypeInteger == Circuit_Abstract.CIRCUIT_TYPE_Gradient)
//		{
//			Circuit_Radiator circuitItem = new Circuit_Radiator(name, friendlyName, circuitTypeInteger, tempMax, rampUpTime);
//			circuitList.add(circuitItem);
//		}
//		else if (circuitTypeInteger == Circuit_Abstract.CIRCUIT_TYPE_Mixer)
//		{
//			Circuit_Mixer circuitItem = new Circuit_Mixer(name, friendlyName, circuitTypeInteger, tempMax, rampUpTime);
//			circuitList.add(circuitItem);
//		}
//	}
	public void addFromObject
		(
		String 			name, 
		Integer			circuitType, 
		String			pumpName, 
		String			thermometerName,
		Integer			tempMax
		)
	{
		//Berk berk berkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
		
		
		if (circuitType == Circuit_Abstract.CIRCUIT_TYPE_HotWater)
		{
			Circuit_HotWater circuitItem 				= new Circuit_HotWater(name, circuitType, pumpName, thermometerName, tempMax);
			circuitList.add(circuitItem);
		}
		else if (circuitType == Circuit_Abstract.CIRCUIT_TYPE_Gradient)
		{
			Circuit_Radiator circuitItem 				= new Circuit_Radiator(name, circuitType, pumpName, thermometerName, tempMax);
			circuitList.add(circuitItem);
		}
		else if (circuitType == Circuit_Abstract.CIRCUIT_TYPE_Mixer)
		{
			Circuit_Mixer circuitItem 					= new Circuit_Mixer(name, circuitType, pumpName, thermometerName, tempMax);
			circuitList.add(circuitItem);
		}
	}
	public Boolean isSingleActiveCircuit()
	{
		/*
		 * Returns the count of active circuits.
		 * If only one active circuit, returns true
		 * if more, returns false
		 */
		int count = 0;

		for (Circuit_Abstract circuit : circuitList) 
		{
			if (circuit.taskActive != null)
			{
				count++;
			}
		}		
		if (count == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public Circuit_Abstract fetchcircuit(String name)
	{
		for (Circuit_Abstract circuit : Global.circuits.circuitList) 
		{
			if (circuit.name.equalsIgnoreCase(name))
			{
				return circuit;
			}
		}
		return (Circuit_Abstract) null;
	}
}
