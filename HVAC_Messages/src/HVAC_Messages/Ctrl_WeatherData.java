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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;


//http://api.openweathermap.org/data/2.5/forecast/city?q=chambery,fr&mode=xml&units=metric&type=accurate


public class Ctrl_WeatherData 						extends DefaultHandler
{
	public 	ArrayList <Forecast>					forecasts		= new ArrayList <Forecast>();
	public	Sun										sun;
	public	Long									dateTimeObtained;
	private Forecast								forecast;
	
	public class Forecast
	{
		public DateTime								dateTime;		// Tag forcast/time
		public Symbol								symbol;			// Tag symbol
		public Precipitation						precipitation;	// Tag precipitation
		public WindDirection 						windDirection;	// Tag WindDirection	
		public WindSpeed 							windSpeed;		// Tag WindSpeed
		public Temperature 							temperature;	// Tag Temperature
		public Pressure 							pressure;		// Tag Pressure
		public Humidity 							humidity;		// Tag Humidity
		public Clouds 								clouds;			// Tag Clouds
	}
	
	public class DateTime
	{
		public Long 								from;			//Tag time/from
		public Long 								to;				//Tag time/to
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
	public class Sun 
	{
		public Long 								sunRise;		//Tag sun/rise
		public Long 								sunSet;			//Tag sun/set
	}
	public Ctrl_WeatherData() throws IOException, SAXException, ParserConfigurationException
	{

		StringBuffer 								response_msg 					= new StringBuffer();
		InputStream									response_xml					= null;
		//=====================================================================
		//
		// Get an xml version of the forecast from the net
		
		try 
		{
			URL 									serverURL 						= new URL("http://api.openweathermap.org/data/2.5/forecast/city?q=chambery,fr&mode=xml&units=metric&type=accurate");
			HttpURLConnection  						serverConnection 				= (HttpURLConnection) serverURL.openConnection();
			serverConnection.setRequestMethod("GET");
			serverConnection.setConnectTimeout(5000);
			serverConnection.setReadTimeout(10000);
			serverConnection.setRequestProperty("Accept", "application/xml");

			int 									responseCode 					= serverConnection.getResponseCode();
			
			if (responseCode == 200)
			{
				response_xml														= serverConnection.getInputStream();
			}
			else
			{
				System.out.println ("Error response from http : " + responseCode);
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Ctrl_WeatherData/Constructor error on contacting the weather server");
			return;	// Will try again in 5 mins (loop timer)
		}
		//
		//=====================================================================
		
		try 
		{
			SAXParserFactory 						saxFactory 						= SAXParserFactory.newInstance();
			SAXParser 								saxParser 						= saxFactory.newSAXParser();
			
			saxParser.parse(response_xml, this);
			this.dateTimeObtained													= System.currentTimeMillis();			// dateTimeStamp the forecast.
		} 
		catch (Exception e) 
		{
			System.out.println("Ctrl_WeatherData/Constructor error parsing the xml");
		}
		
	}
	@Override
	public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException 
    {
		if (tagName.equalsIgnoreCase("Sun"))
		{
			// Single Forecast tag. Each forecast is within a Time tag
		}
		else if (tagName.equalsIgnoreCase("Forecast"))
		{
			// Single Forecast tag. Each forecast is within a Time tag
		}
		else if (tagName.equalsIgnoreCase("Time"))
		{
			forecast																= new Forecast();
			forecast.dateTime														= new DateTime();
			forecast.dateTime.from													= dateTimeFromUTC(attributes.getValue("from"));
			forecast.dateTime.to													= dateTimeFromUTC(attributes.getValue("to"));
		}
		else if (tagName.equalsIgnoreCase("Symbol"))
		{
			forecast.symbol															= new Symbol();
			forecast.symbol.number													= Integer.parseInt(attributes.getValue("number"));
			forecast.symbol.name													= attributes.getValue("name");
			forecast.symbol.var														= attributes.getValue("var");
		}
		else if (tagName.equalsIgnoreCase("precipitation"))
		{
			if  (attributes.getLength() > 2)
			{
				forecast.precipitation												= new Precipitation();
				forecast.precipitation.value										= Float.parseFloat(attributes.getValue("value"));
				forecast.precipitation.unit											= attributes.getValue("unit");
				forecast.precipitation.type											= attributes.getValue("type");
			}
		}
		else if (tagName.equalsIgnoreCase("WindDirection"))
		{
			forecast.windDirection													= new WindDirection();
			forecast.windDirection.degrees											= Float.parseFloat(attributes.getValue("deg"));
			forecast.windDirection.code												= attributes.getValue("code");
			forecast.windDirection.name												= attributes.getValue("name");
		}
		else if (tagName.equalsIgnoreCase("WindSpeed"))
		{
			forecast.windSpeed														= new WindSpeed();
			forecast.windSpeed.speed												= Float.parseFloat(attributes.getValue("mps"));
			forecast.windSpeed.name													= attributes.getValue("name");
		}
		else if (tagName.equalsIgnoreCase("Temperature"))
		{
			forecast.temperature													= new Temperature();
			forecast.temperature.unit												= attributes.getValue("unit");
			forecast.temperature.value												= Float.parseFloat(attributes.getValue("value"));
			forecast.temperature.min												= Float.parseFloat(attributes.getValue("min"));
			forecast.temperature.max												= Float.parseFloat(attributes.getValue("max"));
		}
		else if (tagName.equalsIgnoreCase("Pressure"))
		{
			forecast.pressure														= new Pressure();
			forecast.pressure.unit													= attributes.getValue("unit");
			forecast.pressure.value													= Float.parseFloat(attributes.getValue("value"));
		}
		else if (tagName.equalsIgnoreCase("Humidity"))
		{
			forecast.humidity														= new Humidity();
			forecast.humidity.value													= Integer.parseInt(attributes.getValue("value"));
			forecast.humidity.unit													= attributes.getValue("unit");
			forecast.humidity.var													= attributes.getValue("var");
		}
		else if (tagName.equalsIgnoreCase("Clouds"))
		{
			forecast.clouds															= new Clouds();
			forecast.clouds.value													= attributes.getValue("value");
			forecast.clouds.all														= Integer.parseInt(attributes.getValue("all"));
			forecast.clouds.unit													= attributes.getValue("unit");
		}
		else if (tagName.equalsIgnoreCase("Rise"))
		{
			sun																		= new Sun();
			sun.sunRise																= dateTimeFromUTC(attributes.getValue("rise"));
			sun.sunSet																= dateTimeFromUTC(attributes.getValue("set"));
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
		SimpleDateFormat 								utcFormat					= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		GregorianCalendar 								calendar					= new GregorianCalendar(TimeZone.getTimeZone("UTC"));
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
