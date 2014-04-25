package eRegulation;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import HVAC_Messages.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.parsers.ParserConfigurationException;

import java.io.*;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Calendars extends DefaultHandler
{
	private Circuit_Abstract	circuit;
	
	public Calendars() throws IOException, SAXException, ParserConfigurationException
    {
		//==================================================================================
		//
		// Get message from server
		//
		if (!Global.httpSemaphore.semaphoreLock("LogIt.logMessage"))
		{
			System.out.println(Global.dateTimeDisplay() + " Calendars.constructor Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request	<Ctrl_Calendars.Request>			httpRequest			= new HTTP_Request <Ctrl_Calendars.Request> ("Management");
		
		Ctrl_Calendars.Request	 							messageSend 		= new Ctrl_Calendars.Request();
		Ctrl_Abstract 										messageReceive 		= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Ctrl_Calendars.Data))
		{
			System.out.println(Global.dateTimeDisplay() + " Calendars.constructor messageType is : Nack");
			// There is a problem, so read the last file received
			try
			{
				File										file				= new File("eCalendars_Json.txt");
				FileInputStream  							fileread			= new FileInputStream (file);
				byte[] 										data 				= new byte[(int) file.length()];
				fileread.read(data);
				fileread.close();

			    String 										dataIn 				= new String(data);
				
			    Ctrl_Calendars.Data							dataInJson 			= new Gson().fromJson(dataIn, Ctrl_Calendars.Data.class);
				messageReceive													= (Ctrl_Abstract) dataInJson;
			}  
			catch(IOException ex)
			{
				System.out.println("I/O error on open : " + ex);
			}	
		}
		else
		{
			// All is Ok, so see if we need to write a copy locally
			try
			{
				File										file				= new File("eCalendars_Json.txt");
				if (file.exists())
				{
					Long timeFile												= file.lastModified();
					Ctrl_Calendars.Data thisData								= (Ctrl_Calendars.Data) messageReceive;
					Long timeData												= thisData.dateTime;
					
					if (timeData > timeFile)
					{
						System.out.println("Calendars.constructor writing eRegulator_Json.txt file");
						try
						{
							FileWriter 						filewrite			= new FileWriter("eRegulator_Json.txt");
							
							Gson 							gson 				= new GsonBuilder().setPrettyPrinting().create();
							
							String 							messageJson 		= gson.toJson((Ctrl_Calendars.Data) messageReceive);

							filewrite.write(messageJson);
							filewrite.flush();
							filewrite.close();
						}  
						catch(IOException ex)
						{
							System.out.println("I/O error on open : " + ex);
						}	
					}
				}
			}
			catch (Exception e)
			{
				System.out.println("Calendars.constructor Exception = ");
			}
		}
		Global.httpSemaphore.semaphoreUnLock();			
		
		Ctrl_Calendars.Data									calendarData		= (Ctrl_Calendars.Data) messageReceive;

		//
		//==================================================================================
		
		//==================================================================================
		//
		// Got message from server
		//

		
//		this.vocabulary.configure(calendarData.wordList);
		for (Ctrl_Calendars.Word 					word 				: calendarData.wordList) 
		{
			LogIt.info("Vocabulary Entry", word.name, "Days " + word.days);
		}
		
		for (Ctrl_Calendars.Circuit 						paramCircuit 		: calendarData.circuitList)
		{
			Circuit_Abstract								circuit				= Global.circuits.fetchcircuit(paramCircuit.name);
		
			for (Ctrl_Calendars.Calendar 					paramCalendar 		: paramCircuit.calendarList)
			{
				if (paramCalendar.active)
				{
					for (Ctrl_Calendars.Word 					word 				: calendarData.wordList) 
					{
						paramCalendar.days 												= paramCalendar.days.replace(word.name, word.days);
					}
					circuit.addCircuitTask(paramCalendar);
					LogIt.info("Calendar Entry", circuit.name, "Time start/end " + paramCalendar.timeStart + "/" + paramCalendar.stopCriterion.timeEnd + " Days " + paramCalendar.days);
				}
			}
		}

		// Now handle the away items
		
		

		//
		//==================================================================================

		
//		try 
//		{
//			SAXParserFactory 	saxFactory 			= SAXParserFactory.newInstance();
//			SAXParser 			saxParser 			= saxFactory.newSAXParser();
//			
//			saxParser.parse("eCalendars.xml", this);
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		}
		
		// Write serialised object to file
//		try
//		{
//			OutputStream 		file 				= new FileOutputStream("eCalendars_obj.txt");
//			ObjectOutputStream 	output 				= new ObjectOutputStream(file);
//		    try
//		    {
//		    	output.writeObject(Global.circuits);
//		    }
//			catch(IOException ex)
//			{
//				System.out.println("I/O error on writeObject : " + ex);
//			}	
//		    finally
//		    {
//		        output.close();
//		    }
//		}  
//		catch(IOException ex)
//		{
//			System.out.println("I/O error on open : " + ex);
//		}	

	}
//	public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException 
//    {
//		if (attributes.getLength() > 0)
//		{
//			if (attributes.getValue("type").equalsIgnoreCase("Collection")) 
//			{
//			}
//			else if (attributes.getValue("type").equalsIgnoreCase("Object"))
//			{
//				if (tagName.equalsIgnoreCase("Circuit"))
//				{
//					String name 					= attributes.getValue("name");
//
//					this.circuit					= Global.circuits.fetchcircuit(name);
//				}
//				else if (tagName.equalsIgnoreCase("Word"))
//				{
////					String name 					= attributes.getValue("name");
////					String days 					= attributes.getValue("days");
////					String use	 					= attributes.getValue("use");
////					if (use.equalsIgnoreCase("days"))
////					{
//////						this.vocabulary.add(name, days);
//////						LogIt.info("Vocabulary Entry", name, "Days " + days);
////					}
//				}
//				else if (tagName.equalsIgnoreCase("Calendar"))
//				{
//					String days 					= attributes.getValue("days");
//					String timeStart 				= attributes.getValue("timeStart");
//					String timeEnd 					= attributes.getValue("timeEnd");
//					String tempObjective 			= attributes.getValue("tempObjective");
//					String stopOnObjective 			= attributes.getValue("stopOnObjective");
//					for (Word word : vocabulary.wordList) 
//					{
//						days = days.replace(word.name, word.days);
//					}
//					this.circuit.addCircuitTask(timeStart, timeEnd, tempObjective, stopOnObjective, days);
//					LogIt.info("Calendar Entry", this.circuit.name, "Time start/end " + timeStart + "/" + timeEnd + " Days " + days);
//				}
//			}
//			else
//			{
//				// Nothing of interest
//			}
//		}
//	}
//	public void endElement(String uri, String localName, String tagName) throws SAXException 
//	{
//	}
//	public void characters(char ch[], int start, int length) throws SAXException 
//	{
//	}
//	private class Vocabulary
//	{
//		public ArrayList<Word>	wordList = new ArrayList<Word>();
//		
//		public Vocabulary()
//		{
//		}
//		public void configure(ArrayList <Ctrl_Calendars.Word> words)
//		{
//			for (Ctrl_Calendars.Word word : words)
//			{
//				wordList.add(new Word(word.name, word.days));
//				LogIt.info("Vocabulary Entry", word.name, "Days " + word.days);
//			}
//		}
////		public void add(String name, String days)
////		{
////			Word word 	= new Word(name, days);
////			wordList.add(word);
////		}
//	}
//	private class Word
//	{
//		public String name;
//		public String days;
//		public Word()
//		{
//		}
//		public Word(String name, String days)
//		{
//			this.name 		    									= name;
//			this.days  												= days;
//		}
//
//	}
	
}
