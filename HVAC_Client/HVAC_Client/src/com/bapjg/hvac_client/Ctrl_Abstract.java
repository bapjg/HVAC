package com.bapjg.hvac_client;

//This class was intended as abstract. But to be able to instanciate/reference inner classes
//abstract has been removed
public class Ctrl_Abstract implements java.io.Serializable
{
	private static final long 		serialVersionUID 	= 1L;
	
	@SuppressWarnings("serial")
	public class Ack extends Ctrl_Abstract
	{
	}
	@SuppressWarnings("serial")
	public class Nack extends Ctrl_Abstract
	{
	}
}