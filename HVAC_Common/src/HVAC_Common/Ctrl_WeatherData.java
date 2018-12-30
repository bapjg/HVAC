package HVAC_Common;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.net.*;
//import java.net.HttpURLConnection;
//import java.net.SocketTimeoutException;
//import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;


// http://api.openweathermap.org/data/2.5/forecast/city?q=chambery,fr&mode=xml&units=metric&type=accurate

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Ctrl_WeatherData 									extends 					DefaultHandler
																implements 					java.io.Serializable
{
	public 	ArrayList <Forecast>								forecasts		= new ArrayList <Forecast>();
	public	Sun													sun;
	public	Long												dateTimeObtained;
	private Forecast											forecast;
				
	public class Forecast										implements 		java.io.Serializable
	{			
		public DateTime											dateTime;		// Tag forcast/time
		public Symbol											symbol;			// Tag symbol
		public Precipitation									precipitation;	// Tag precipitation
		public WindDirection 									windDirection;	// Tag WindDirection	
		public WindSpeed 										windSpeed;		// Tag WindSpeed
		public Temperature 										temperature;	// Tag Temperature
		public Pressure 										pressure;		// Tag Pressure
		public Humidity 										humidity;		// Tag Humidity
		public Clouds 											clouds;			// Tag Clouds
	}
	
	public class DateTime										implements 		java.io.Serializable
	{			
		public Long 											from;			//Tag time/from
		public Long 											to;				//Tag time/to
	}			
	public class Symbol											implements 		java.io.Serializable
	{			
		public Number 											number;			//Tag number
		public String 											name;			//Tag name
		public String 											var;			//Tag var
	}			
	public class Precipitation									implements 		java.io.Serializable
	{			
		public Float 											value;			//Tag value
		public String 											unit;			//Tag unit
		public String 											type;			//Tag type
	}			
	public class WindDirection 									implements 		java.io.Serializable
	{			
		public Float 											degrees;		//Tag degrees
		public String 											code;			//Tag direction eg NNE
		public String 											name;			//Tag name e.g. North-northeast
	}
	public class WindSpeed										implements 		java.io.Serializable
	{			
		public Float 											speed;			//Tag mps (metres per second)
		public String 											name;			//Tag name (e.g. light breeze)
	}			
	public class Temperature 									implements 		java.io.Serializable
	{			
		public String 											unit;			//Tag unit (Celsius)
		public Float 											value;			//Tag value
		public Float 											min;			//Tag value
		public Float 											max;			//Tag value
	}			
	public class Pressure 										implements 		java.io.Serializable
	{			
		public String 											unit;			//Tag unit (hPa)
		public Number 											value;			//Tag value
	}			
	public class Humidity										implements 		java.io.Serializable
	{
		public Number 											value;			//Tag value
		public String 											unit;			//Tag unit "%"
		public String 											var;			//Tag 
	}			
	public class Clouds 										implements 		java.io.Serializable
	{			
		public String 											value;			//Tag value
		public Integer 											all;			//Tag all (e.g. 92)
		public String 											unit;			//Tag unit "%"
	}			
	public class Sun 											implements 		java.io.Serializable
	{			
		public Long 											sunRise;		//Tag sun/rise
		public Long 											sunSet;			//Tag sun/set
	}
	public Ctrl_WeatherData() throws Exception, IOException, SAXException, ParserConfigurationException
	{

		StringBuffer 											response_msg 				= new StringBuffer();
		InputStream												response_xml				= null;
		String													string_xml					= null;
		
		//=====================================================================
		//
		// Get an xml version of the forecast from the net
		
		try 
		{
			// Another weather site (not used in this implementation)
			// https://www.wunderground.com/weather/api
			// password (????) : bb358af85e0cc8e0
			
			URL 												serverURL 					= new URL("http://api.openweathermap.org/data/2.5/forecast?q=chambéry,fr&mode=xml&units=metric&type=accurate&appid=41515f9a266234d303e26f056173c60b");
			HttpURLConnection  									serverConnection 			= (HttpURLConnection) serverURL.openConnection();
			serverConnection.setRequestMethod("GET");
			serverConnection.setConnectTimeout(5000);
			serverConnection.setReadTimeout(10000);
			serverConnection.setRequestProperty("Accept", "application/xml");

			int 												responseCode 				= serverConnection.getResponseCode();
			
			if (responseCode == 200)
			{
				response_xml																= serverConnection.getInputStream();
			}
			else
			{
				this.dateTimeObtained														= null;
				this.forecasts																= null;
				Exception 										exception 					= new Exception("Response Code <> 200 (" + responseCode + "). Try again in 5 mins");
				throw exception;
//				return;																			// Will try again in 5 mins (loop timer)
			}		
		}
		catch (SocketTimeoutException eTO) 		
		{		
			this.dateTimeObtained															= null;
			this.forecasts																	= null;
			throw eTO;
//			return;																			// Will try again in 5 mins (loop timer)
		}		
		catch (Exception e) 																//	This can happen for a variety of reasons, just try again in 5 mins
		{
			this.dateTimeObtained															= null;
			this.forecasts																	= null;
			throw e;
//			return;																			// Will try again in 5 mins (loop timer)
		}
		//
		//=====================================================================
		
		try
		{
			DocumentBuilderFactory		docFactory											= DocumentBuilderFactory.newInstance();
			DocumentBuilder				docBuilder											= docFactory.newDocumentBuilder();
//			Document					xmlDcoument											= docBuilder.parse(new InputSource(new StringReader(string_xml)));
			Document					xmlDcoument											= docBuilder.parse(new InputSource(response_xml));
			xmlDcoument.getDocumentElement().normalize();		
					
			Node xmlSun																		= xmlDcoument.getElementsByTagName("sun").item(0);
			String sSunRise																	= ((Element) xmlSun).getAttribute("rise");
			String sSunSet																	= ((Element) xmlSun).getAttribute("set");
		
			sun																				= new Sun();
			sun.sunRise																		= dateTimeFromUTC(getAttribute("rise", xmlSun));
			sun.sunSet																		= dateTimeFromUTC(getAttribute("set", xmlSun));
					
			NodeList xmlForecasts 															= xmlDcoument.getElementsByTagName("forecast");
			NodeList xmlForecastItems 														= xmlDcoument.getElementsByTagName("time");
			
			int i = xmlForecastItems.getLength();
			for (i = 0; i < xmlForecastItems.getLength(); i++)
			{
				forecast																	= new Forecast();
		
				Node xmlForecastItemNode													= xmlForecastItems.item(i);
				Element xmlForecastItemElement												= (Element) xmlForecastItemNode;
		
				Node xmlNode																= xmlForecastItemElement;
				forecast.dateTime															= new DateTime();
				forecast.dateTime.from														= dateTimeFromUTC(getAttribute("from", xmlNode));
				forecast.dateTime.to														= dateTimeFromUTC(getAttribute("to", xmlNode));
						
				xmlNode																		= xmlForecastItemElement.getElementsByTagName("symbol").item(0);
				forecast.symbol																= new Symbol();
				forecast.symbol.number														= Integer.parseInt(getAttribute("number", xmlNode));
				forecast.symbol.name														= getAttribute("name", xmlNode);
				forecast.symbol.var															= getAttribute("var", xmlNode);
				
				xmlNode																		= xmlForecastItemElement.getElementsByTagName("precipitation").item(0);
				if  (xmlNode.getAttributes().getLength() > 2)
				{
					forecast.precipitation													= new Precipitation();
					forecast.precipitation.value											= Float.parseFloat(getAttribute("value", xmlNode));
					forecast.precipitation.unit												= getAttribute("unit", xmlNode);
					forecast.precipitation.type												= getAttribute("type", xmlNode);
				}		
		
				xmlNode																		= xmlForecastItemElement.getElementsByTagName("windDirection").item(0);
				forecast.windDirection														= new WindDirection();
				forecast.windDirection.degrees												= Float.parseFloat(getAttribute("deg", xmlNode));
				forecast.windDirection.code													= getAttribute("code", xmlNode);
				forecast.windDirection.name													= getAttribute("name", xmlNode);
		
				xmlNode																		= xmlForecastItemElement.getElementsByTagName("windSpeed").item(0);
				forecast.windSpeed															= new WindSpeed();
				forecast.windSpeed.speed													= Float.parseFloat(getAttribute("mps", xmlNode));
				forecast.windSpeed.name														= getAttribute("name", xmlNode);
		
				xmlNode																		= xmlForecastItemElement.getElementsByTagName("temperature").item(0);
				forecast.temperature														= new Temperature();
				forecast.temperature.unit													= getAttribute("unit", xmlNode);
				forecast.temperature.value													= Float.parseFloat(getAttribute("value", xmlNode));
				forecast.temperature.min													= Float.parseFloat(getAttribute("min", xmlNode));
				forecast.temperature.max													= Float.parseFloat(getAttribute("max", xmlNode));

				xmlNode																		= xmlForecastItemElement.getElementsByTagName("pressure").item(0);
				forecast.pressure															= new Pressure();
				forecast.pressure.unit														= getAttribute("unit", xmlNode);
				forecast.pressure.value														= Float.parseFloat(getAttribute("value", xmlNode));
		
				xmlNode																		= xmlForecastItemElement.getElementsByTagName("humidity").item(0);
				forecast.humidity															= new Humidity();
				forecast.humidity.value														= Integer.parseInt(getAttribute("value", xmlNode));
				forecast.humidity.unit														= getAttribute("unit", xmlNode);
				forecast.humidity.var														= getAttribute("var", xmlNode);
		
				xmlNode																		= xmlForecastItemElement.getElementsByTagName("clouds").item(0);
				forecast.clouds																= new Clouds();
				forecast.clouds.value														= getAttribute("value", xmlNode);
				forecast.clouds.all															= Integer.parseInt(getAttribute("all", xmlNode));
				forecast.clouds.unit														= getAttribute("unit", xmlNode);
			
				forecasts.add(forecast);				
			}
		}
		catch (Exception e) 
		{
			System.out.println("Ctrl_WeatherData/Constructor error parsing the xml");
			this.dateTimeObtained															= null;
			this.forecasts																	= null;
		}
	}
	public String getAttribute(String attribute, Node node)
	{
		Element 												element 					= (Element) node;
		String 													returnString 				= "";
		try
		{
			returnString 																	= element.getAttribute(attribute);
		}
		catch (Exception e)
		{
			returnString 																	= "";
		}
		return returnString;
	}
	public void startElement(String uri, String localName, String tagName, Attributes attributes)
    {
		if (tagName.equalsIgnoreCase("Sun"))
		{
			sun																				= new Sun();
			sun.sunRise																		= dateTimeFromUTC(attributes.getValue("rise"));
			sun.sunSet																		= dateTimeFromUTC(attributes.getValue("set"));
		}
		else if (tagName.equalsIgnoreCase("Forecast"))
		{
			// Single Forecast tag. Each forecast is within a Time tag
		}
		else if (tagName.equalsIgnoreCase("Time"))
		{
			forecast																		= new Forecast();
			forecast.dateTime																= new DateTime();
			forecast.dateTime.from															= dateTimeFromUTC(attributes.getValue("from"));
			forecast.dateTime.to															= dateTimeFromUTC(attributes.getValue("to"));
		}		
		else if (tagName.equalsIgnoreCase("Symbol"))		
		{		
			forecast.symbol																	= new Symbol();
			forecast.symbol.number															= Integer.parseInt(attributes.getValue("number"));
			forecast.symbol.name															= attributes.getValue("name");
			forecast.symbol.var																= attributes.getValue("var");
		}
		else if (tagName.equalsIgnoreCase("precipitation"))
		{
			if  (attributes.getLength() > 2)
			{
				forecast.precipitation														= new Precipitation();
				forecast.precipitation.value												= Float.parseFloat(attributes.getValue("value"));
				forecast.precipitation.unit													= attributes.getValue("unit");
				forecast.precipitation.type													= attributes.getValue("type");
			}
		}
		else if (tagName.equalsIgnoreCase("WindDirection"))
		{
			forecast.windDirection															= new WindDirection();
			forecast.windDirection.degrees													= Float.parseFloat(attributes.getValue("deg"));
			forecast.windDirection.code														= attributes.getValue("code");
			forecast.windDirection.name														= attributes.getValue("name");
		}		
		else if (tagName.equalsIgnoreCase("WindSpeed"))		
		{		
			forecast.windSpeed																= new WindSpeed();
			forecast.windSpeed.speed														= Float.parseFloat(attributes.getValue("mps"));
			forecast.windSpeed.name															= attributes.getValue("name");
		}		
		else if (tagName.equalsIgnoreCase("Temperature"))		
		{		
			forecast.temperature															= new Temperature();
			forecast.temperature.unit														= attributes.getValue("unit");
			System.out.println("v " + attributes.getValue("value"));		
			System.out.println("i " + attributes.getValue("min"));		
			System.out.println("a " + attributes.getValue("max"));		
			forecast.temperature.value														= Float.parseFloat(attributes.getValue("value"));
			forecast.temperature.min														= Float.parseFloat(attributes.getValue("min"));
			forecast.temperature.max														= Float.parseFloat(attributes.getValue("max"));
		}
		else if (tagName.equalsIgnoreCase("Pressure"))
		{
			forecast.pressure																= new Pressure();
			forecast.pressure.unit															= attributes.getValue("unit");
			forecast.pressure.value															= Float.parseFloat(attributes.getValue("value"));
		}		
		else if (tagName.equalsIgnoreCase("Humidity"))		
		{		
			forecast.humidity																= new Humidity();
			forecast.humidity.value															= Integer.parseInt(attributes.getValue("value"));
			forecast.humidity.unit															= attributes.getValue("unit");
			forecast.humidity.var															= attributes.getValue("var");
		}		
		else if (tagName.equalsIgnoreCase("Clouds"))		
		{		
			forecast.clouds																	= new Clouds();
			forecast.clouds.value															= attributes.getValue("value");
			forecast.clouds.all																= Integer.parseInt(attributes.getValue("all"));
			forecast.clouds.unit															= attributes.getValue("unit");
		}
		else
		{
			// Nothing of interest
		}
	}
	@Override
	public void endElement(String uri, String localName, String tagName) throws SAXException 
	{
		if (tagName.equalsIgnoreCase("Time"))
		{
			// Single Forecast tag. Each forecast is within a Time tag
			forecasts.add(forecast);
		}
	}
	@Override
	public void characters(char ch[], int start, int length) throws SAXException 
	{
	}
	public Long dateTimeFromUTC(String utc)
	{
		// Dates in forecast are UTC in the form YYYY-MM-DDTHH:MM (note the "T" in the middle)
		// We need to convert to Long data type based on CET (UTC +1hr or 2hrs)
		SimpleDateFormat 										utcFormat					= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		GregorianCalendar 										calendar					= new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		try
		{
			calendar.setTime(utcFormat.parse(utc));
			return calendar.getTimeInMillis();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}
}	
