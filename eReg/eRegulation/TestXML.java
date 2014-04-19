package eRegulation;

import java.util.ArrayList;

public class TestXML
{
	public String thermo1							= "abc";
	public Integer thermo2							= 2;
	public SubClass subk							= new SubClass();
	public ArrayList <SubClass> subs				= new ArrayList<SubClass>(); 
	
	public class SubClass
	{
		Integer	henry						= 3;
		String	alf							= "alf";
	}
}