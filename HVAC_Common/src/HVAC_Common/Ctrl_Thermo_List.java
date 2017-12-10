package HVAC_Common;

import java.util.ArrayList;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Ctrl_Thermo_List 							extends 					Ctrl__Abstract
{
	private static final long 							serialVersionUID 			= 1L;
	
	public Ctrl_Thermo_List()
	{
	}
	public class Thermo 								extends 					Ctrl_Thermo_List
	{
		public String 									name						= "";
		public String 									address						= "";
		public Boolean									isNew						= false;
		public Boolean 									isLost						= false;
		public int 										temperature					= 0;
	}
	public class Data 									extends 					Ctrl_Thermo_List
	{
		private static final long 						serialVersionUID 			= 1L;
		public ArrayList <Thermo>						thermos						= new ArrayList <Thermo> ();
	}
	public class Request 								extends 					Ctrl_Thermo_List
	{
		private static final long 						serialVersionUID 			= 1L;
	}
	public class Nack 									extends 					Ctrl_Thermo_List
	{
		private static final long 						serialVersionUID 			= 1L;
		public String									errorMessage;
		public Nack (String errorMessage)
		{
			this.errorMessage														= errorMessage;
		}
	}
}
