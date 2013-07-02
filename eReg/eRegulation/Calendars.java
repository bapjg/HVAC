package eRegulation;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.Calendar;

public class Calendars extends DefaultHandler
{
	private Circuit_Abstract	circuit;
	
	public Calendars(String xmlCalendars) throws IOException, SAXException, ParserConfigurationException
    {
		try 
		{
			SAXParserFactory 	saxFactory 			= SAXParserFactory.newInstance();
			SAXParser 			saxParser 			= saxFactory.newSAXParser();
			
			saxParser.parse(xmlCalendars, this);
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
				if (tagName.equalsIgnoreCase("Thermometers"))
				{
					Global.thermometers 			= new Thermometers(); 
				}
			}
			else if (attributes.getValue("type").equalsIgnoreCase("Object"))
			{
				if (tagName.equalsIgnoreCase("Circuit"))
				{
					String name 					= attributes.getValue("name");

					this.circuit					= Global.circuits.fetchcircuit(name);
					//LogIt.info("Calendars", "startElement", "Managing Calendar for " + name);
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
