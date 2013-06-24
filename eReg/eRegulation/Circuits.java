package eRegulation;

import java.util.ArrayList;
import java.util.Iterator;


public class Circuits
{
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
//		<T>circuitItem = new Circuit<T>(name, friendlyName, circuitType, tempMax, rampUp, rampDown);
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
			if (circuit.activeTask != null)
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
//	public Circuit fetchCircuit(int index)
//	{
//		Circuit element 				= null;
//		int i;
//		Iterator <Circuit> itr			= circuitList.iterator();
//		
//		for (i = 0; i <= index; i++)
//		{
//			element 					= itr.next();
//		}
//		return (Circuit) element;
//	}
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
