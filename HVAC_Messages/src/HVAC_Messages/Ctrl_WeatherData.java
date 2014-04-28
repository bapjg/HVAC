package HVAC_Messages;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


//http://api.openweathermap.org/data/2.5/forecast/city?q=chambery,fr&mode=xml&units=metric&type=accurate


public class Ctrl_WeatherData 						extends DefaultHandler
{
	public ArrayList <Forecast>						forecasts		= new ArrayList <Forecast>();
	public Forecast									forecast;
	
	public class Forecast
	{
		public DateTime								dateTime;		// Tag forcast/time
		public Symbol								symbol;			// Tag symbol
		public Precipitation						precipitation;	// Tag precipitation
		public WindDirection 						windDirection ;	// Tag WindDirection	
		public WindSpeed 							windSpeed ;		// Tag WindSpeed
		public Temperature 							temperature ;	// Tag Temperature
		public Pressure 							pressure ;		// Tag Pressure
		public Humidity 							humidity ;		// Tag Humidity
		public Clouds 								clouds ;		// Tag Clouds
	}
	
	public class DateTime
	{
		public String 								from;			//Tag time/from
		public String 								to;				//Tag time/to
	}
	public class Symbol
	{
		public Number 								number;			//Tag number
		public String 								name;			//Tag name
		public String 								var;			//Tag var
	}
	public class Precipitation
	{
		public Float 								value;			//Tag value
		public String 								unit;			//Tag unit
		public String 								type;			//Tag type
	}
	public class WindDirection 
	{
		public Float 								degrees;		//Tag degrees
		public String 								code;			//Tag direction eg NNE
		public String 								name;			//Tag name e.g. North-northeast
	}
	public class WindSpeed
	{
		public Float 								speed;			//Tag mps (metres per second)
		public String 								name;			//Tag name (e.g. light breeze)
	}
	public class Temperature 
	{
		public String 								unit;			//Tag unit (Celsius)
		public Float 								value;			//Tag value
		public Float 								min;			//Tag value
		public Float 								max;			//Tag value
	}
	public class Pressure 
	{
		public String 								unit;			//Tag unit (hPa)
		public Number 								value;			//Tag value
	}
	public class Humidity
	{
		public Number 								value;			//Tag value
		public String 								unit;			//Tag unit "%"
		public String 								var;			//Tag 
	}
	public class Clouds 
	{
		public String 								value;			//Tag value
		public Number 								all;			//Tag all (e.g. 92)
		public String 								unit;			//Tag unit "%"
	}
	public Ctrl_WeatherData() throws IOException, SAXException, ParserConfigurationException
	{

		StringBuffer 								response_msg 					= new StringBuffer();
		InputStream									response_xml					= null;
		
		//=====================================================================
		//
		// This part is the first attempt
		// Get an xml version of the calendar from the server
		
		try 
		{
			URL 									serverURL 						= new URL("http://api.openweathermap.org/data/2.5/forecast/city?q=chambery,fr&mode=xml&units=metric&type=accurate");
			HttpURLConnection  						serverConnection 				= (HttpURLConnection) serverURL.openConnection();
			serverConnection.setRequestMethod("GET");
			serverConnection.setConnectTimeout(1000);
			serverConnection.setReadTimeout(5000);
			
			serverConnection.setRequestProperty("Accept", "application/xml");

			int 									responseCode 					= serverConnection.getResponseCode();
			
			if (responseCode == 200)
			{
				response_xml															= serverConnection.getInputStream();
			}
			else
			{
				System.out.println ("Error response from http : " + responseCode);
			}
	 

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		//
		//=====================================================================

		
		try 
		{
			SAXParserFactory 	saxFactory 			= SAXParserFactory.newInstance();
			SAXParser 			saxParser 			= saxFactory.newSAXParser();
			
			saxParser.parse(response_xml, this);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	@Override
	public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException 
    {
		if (tagName.equalsIgnoreCase("Forecast"))
		{
			// Single Forecast tag. Each forecast is within a Time tag
		}
		else if (tagName.equalsIgnoreCase("Time"))
		{
			this.forecast							= new Forecast();
			forecast.dateTime						= new DateTime();
			forecast.dateTime.from					= attributes.getValue("from");
			forecast.dateTime.to					= attributes.getValue("to");
		}
		else if (tagName.equalsIgnoreCase("Symbol"))
		{
			forecast.symbol							= new Symbol();
			forecast.symbol.number					= Integer.parseInt(attributes.getValue("number"));
			forecast.symbol.name					= attributes.getValue("name");
			forecast.symbol.var						= attributes.getValue("var");
		}
		else if (tagName.equalsIgnoreCase("precipitation"))
		{
			if  (attributes.getLength() > 2)
			{
				forecast.precipitation				= new Precipitation();
				forecast.precipitation.value		= Float.parseFloat(attributes.getValue("value"));
				forecast.precipitation.unit			= attributes.getValue("unit");
				forecast.precipitation.type			= attributes.getValue("type");
			}
		}
		else if (tagName.equalsIgnoreCase("WindDirection"))
		{
			forecast.windDirection					= new WindDirection();
			forecast.windDirection.degrees			= Float.parseFloat(attributes.getValue("deg"));
			forecast.windDirection.code				= attributes.getValue("code");
			forecast.windDirection.name				= attributes.getValue("name");
		}
		else if (tagName.equalsIgnoreCase("WindSpeed"))
		{
			forecast.windSpeed						= new WindSpeed();
			forecast.windSpeed.speed				= Float.parseFloat(attributes.getValue("mps"));
			forecast.windSpeed.name					= attributes.getValue("name");
		}
		else if (tagName.equalsIgnoreCase("Temperature"))
		{
			forecast.temperature					= new Temperature();
			forecast.temperature.unit				= attributes.getValue("unit");
			forecast.temperature.value				= Float.parseFloat(attributes.getValue("value"));
			forecast.temperature.min				= Float.parseFloat(attributes.getValue("min"));
			forecast.temperature.max				= Float.parseFloat(attributes.getValue("max"));
		}
		else if (tagName.equalsIgnoreCase("Pressure"))
		{
			forecast.pressure						= new Pressure();
			forecast.pressure.unit					= attributes.getValue("unit");
			forecast.pressure.value					= Float.parseFloat(attributes.getValue("value"));
		}
		else if (tagName.equalsIgnoreCase("Humidity"))
		{
			forecast.humidity						= new Humidity();
			forecast.humidity.value					= Integer.parseInt(attributes.getValue("value"));
			forecast.humidity.unit					= attributes.getValue("unit");
			forecast.humidity.var					= attributes.getValue("var");
		}
		else if (tagName.equalsIgnoreCase("Clouds"))
		{
			forecast.clouds							= new Clouds();
			forecast.clouds.value					= attributes.getValue("value");
			forecast.clouds.all						= Integer.parseInt(attributes.getValue("all"));
			forecast.clouds.unit					= attributes.getValue("unit");
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
}	
