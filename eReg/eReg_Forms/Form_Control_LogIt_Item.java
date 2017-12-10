package eReg_Forms;

import java.text.SimpleDateFormat;
import java.util.Date;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Form_Control_LogIt_Item
{
	public String 								dateTimeStamp;
	public String								severity;
	public String								sender;
	public String								message;
	
	public Form_Control_LogIt_Item()
	{
		Date 													now 						= new Date();
		SimpleDateFormat 										dateFormat 					= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		this.dateTimeStamp 																	= dateFormat.format(now);
	}
	public Form_Control_LogIt_Item(String severity, String sender, String message)
	{
		// TODO : Delete this method
		
		Date 													now 						= new Date();
		SimpleDateFormat 										dateFormat 					= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		this.dateTimeStamp 																	= dateFormat.format(now);
		this.severity 																		= severity;
		this.sender 																		= sender;
		this.message 																		= message;
	}
	public Form_Control_LogIt_Item(String dateTimeStamp, String severity, String sender, String message)
	{
		this.dateTimeStamp 																	= dateTimeStamp;
		this.severity 																		= severity;
		this.sender 																		= sender;
		this.message 																		= message;
	}
}
