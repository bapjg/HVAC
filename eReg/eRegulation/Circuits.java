package eRegulation;

import java.util.ArrayList;

public class Circuits implements java.io.Serializable
{
	private static final 	long 		serialVersionUID = 999999L;
	
	public ArrayList<Circuit_Abstract> circuitList = new ArrayList<Circuit_Abstract>();
	
	public void add
		(
		String 			name, 
		String 			friendlyName,  
		String 			circuitType, 
		String			tempMax, 
		String			rampUp, 
		String			rampDown
		)
	{
		if (circuitType.equalsIgnoreCase("hotWater"))
		{
			Circuit_HotWater circuitItem = new Circuit_HotWater(name, friendlyName, circuitType, tempMax, rampUp, rampDown);
			circuitList.add(circuitItem);
		}
		else if (circuitType.equalsIgnoreCase("tempGradient"))
		{
			Circuit_Gradient circuitItem = new Circuit_Gradient(name, friendlyName, circuitType, tempMax, rampUp, rampDown);
			circuitList.add(circuitItem);
		}
		else if (circuitType.equalsIgnoreCase("Mixer"))
		{
			Circuit_Mixer circuitItem = new Circuit_Mixer(name, friendlyName, circuitType, tempMax, rampUp, rampDown);
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
