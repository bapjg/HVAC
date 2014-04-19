package eRegulation;

import java.util.ArrayList;

public class TestXML
{
	public String thermo1							= "abc";
	public Integer thermo2							= 2;
	public Thermometer subk							= new Thermometer();
	public ArrayList <Thermometer> ThermometerList	= new ArrayList<Thermometer>(); 
	
	public class Thermometer
	{
		Integer	henry						= 3;
		String	alf							= "alf";
	}
}