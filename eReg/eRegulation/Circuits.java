package eRegulation;

import java.util.ArrayList;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Configuration;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Circuits
{
	// Was serialisable no longer since 23/04/2014
	public ArrayList<Circuit_Abstract> circuitList = new ArrayList<Circuit_Abstract>();
	
	public void configure(ArrayList<Ctrl_Configuration.Circuit> paramCircuits)
	{
		for (Ctrl_Configuration.Circuit		 	paramCircuit : paramCircuits)
		{
			if (paramCircuit.type == Circuit_Abstract.CIRCUIT_TYPE_HotWater)
			{
				Circuit_HotWater circuitItem 				= new Circuit_HotWater(paramCircuit);
				circuitList.add(circuitItem);
			}
			else if (paramCircuit.type == Circuit_Abstract.CIRCUIT_TYPE_Gradient)
			{
				Circuit_Radiator circuitItem 				= new Circuit_Radiator(paramCircuit);
				circuitList.add(circuitItem);
			}
			else if (paramCircuit.type == Circuit_Abstract.CIRCUIT_TYPE_Mixer)
			{
				Circuit_Mixer circuitItem 					= new Circuit_Mixer(paramCircuit);
				circuitList.add(circuitItem);
			}
		}
	}
/**
 * Returns the count of active circuits. TODO : A bit over simplified
 */	
	public Integer getActiveCircuitCount()
	{
		Integer count = 0;
	
		for (Circuit_Abstract circuit : circuitList) 
		{
			if (circuit.taskActive != null)
			{
				count++;
			}
		}
		return count;
	}
/**
 * Returns the count of active circuits.
 * If only one active circuit, returns true
 * if more, returns false
 */	
	public Boolean isSingleActiveCircuit()
	{
		return (getActiveCircuitCount() == 1);
	}
	public Circuit_Abstract fetchCircuit(String name)
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
