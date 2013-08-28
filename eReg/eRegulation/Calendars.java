package eRegulation;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;

import java.net.URL;
import java.net.URLConnection;
public class Calendars extends DefaultHandler
{
	private Circuit_Abstract	circuit;
	
	public Calendars() throws IOException, SAXException, ParserConfigurationException
    {
		Message_Calendar_Report 					xmlCalendarString				= null;

		try 
		{
			URL 									serverURL 						= new URL("http://192.168.5.20:8080/hvac/Calendar");
			URLConnection 							servletConnection 				= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			
			Message_Calendar_Request_Data 			messageSend 					= new Message_Calendar_Request_Data();
			
			ObjectOutputStream 						outputToServlet;
			outputToServlet 														= new ObjectOutputStream(servletConnection.getOutputStream());
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
			
			ObjectInputStream 						response 						= new ObjectInputStream(servletConnection.getInputStream());
			Message_Abstract 						messageReceive 					= null;
			
			try
			{
				messageReceive 														= (Message_Abstract) response.readObject();
			}
	    	catch (ClassNotFoundException e) 
	    	{
				e.printStackTrace();
			}
			
			if (messageReceive instanceof Message_Calendar_Report)
			{
				xmlCalendarString													= (Message_Calendar_Report) messageReceive;
//				System.out.println("dateTime  : " + xmlCalendarString.dateTime);
//				System.out.println("calendars : " + xmlCalendarString.calendars);
			}
			else
			{
				System.out.println("instanceof The data  is : Not Message_Calendar_Report");
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.println("Hi");
		
		try 
		{
			SAXParserFactory 	saxFactory 			= SAXParserFactory.newInstance();
			SAXParser 			saxParser 			= saxFactory.newSAXParser();
			
			Global.calendarsDateTime				= xmlCalendarString.dateTime;
//			saxParser.parse(new InputSource(new StringReader(xmlCalendarString.calendars)), this);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 		
		
		
		// This part for file calendar
		// If there's a network problem need to go for file based version
		
		try 
		{
			SAXParserFactory 	saxFactory 			= SAXParserFactory.newInstance();
			SAXParser 			saxParser 			= saxFactory.newSAXParser();
			
			saxParser.parse("eCalendars.xml", this);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 		
	}
	public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException 
    {
		if (attributes.getLength() > 0)
		{
			if (attributes.getValue("type").equalsIgnoreCase("Collection")) 
			{
//				if (tagName.equalsIgnoreCase("Thermometers"))
//				{
//					Global.thermometers 			= new Thermometers(); 
//				}
			}
			else if (attributes.getValue("type").equalsIgnoreCase("Object"))
			{
				if (tagName.equalsIgnoreCase("Circuit"))
				{
					String name 					= attributes.getValue("name");

					this.circuit					= Global.circuits.fetchcircuit(name);
				}
				if (tagName.equalsIgnoreCase("Calendar"))
				{
					String days 					= attributes.getValue("days");
					String timeStart 				= attributes.getValue("timeStart");
					String timeEnd 					= attributes.getValue("timeEnd");
					String tempObjective 			= attributes.getValue("tempObjective");
					String stopOnObjective 			= attributes.getValue("stopOnObjective");

					this.circuit.addCircuitTask(timeStart, timeEnd, tempObjective, stopOnObjective, days, false);
					LogIt.info("Calendar Entry", this.circuit.name, "Time start/end " + timeStart + "/" + timeEnd + " Days " + days);
				}
			}
			else
			{
				// Nothing of interest
			}
		}
	}
	public void endElement(String uri, String localName, String tagName) throws SAXException 
	{
	}
	public void characters(char ch[], int start, int length) throws SAXException 
	{
	}
	
}
