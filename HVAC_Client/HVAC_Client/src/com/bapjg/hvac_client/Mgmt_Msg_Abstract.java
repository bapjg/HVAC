package com.bapjg.hvac_client;

// This class was intended as abstract. But to be able to instanciate/reference inner classes
// abstract has been removed
public class Mgmt_Msg_Abstract implements java.io.Serializable
{
	public static final long 		serialVersionUID 			= 1000L;
	
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
