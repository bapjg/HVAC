package com.bapjg.hvac_client;

public abstract class Mgmt_Msg_Abstract implements java.io.Serializable
{
	public static final long 		serialVersionUID 			= 1001L;
	
	public class Ack extends Mgmt_Msg_Abstract
	{
	}
	public class Nack extends Mgmt_Msg_Abstract
	{
	}
	public class Ping extends Mgmt_Msg_Abstract
	{
	}

}
