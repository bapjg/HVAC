import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import HVAC_Common.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Management extends HttpServlet
{

    public 		Connection 			dbConnection;
    public 		Statement 			dbStatement;
    public 		String 				dbName;
    private 	DataSource 			dbPool;
	
    public Management()
    {
        super();
    	dbName 									= "jdbc:mysql://localhost/hvac_database";
    }
    public void init() throws ServletException
    {
        try
        {
            InitialContext 		ctx 			= new InitialContext();
            dbPool 								= (DataSource)ctx.lookup("java:comp/env/jdbc/hvac");
            if(dbPool == null)
			{
				throw new ServletException("Unknown DataSource 'jdbc/hvac'");
			}
        }
        catch(NamingException ex)
        {
            ex.printStackTrace();
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        Object 							message_in 							= null;
        Ctrl__Abstract	 				message_out 						= null;
        
        try
        {
            ObjectInputStream 			input 								= new ObjectInputStream(request.getInputStream());
            message_in 														= input.readObject();
        }
        catch (ClassNotFoundException eCNF)
        {
            eCNF.printStackTrace();
            message_out 													= (new Ctrl__Abstract()).new Nack();
        }
        catch (IOException eIO)
        {
            System.out.println(dateTime2Time(now()) + " An IO Exception occured : " + eIO);
            message_out 													= (new Ctrl__Abstract()).new Nack();
        }
        catch (Exception e)
        {
            System.out.println(dateTime2Time(now()) + " An Exception occurred : " + e);
            message_out 													= (new Ctrl__Abstract()).new Nack();
        }
        
        if (message_in != null)
        {
            System.out.println(dateTime2Time(now()) + " Class received : " + message_in.getClass().toString());
        } 

        
        if      (message_in == null)         								message_out 	= (new Ctrl__Abstract()).new Nack();
		else if (message_in instanceof Ctrl_Configuration.Request)			message_out 	= processConfiguration_Request();
		else if (message_in instanceof Ctrl_Configuration.Update)			message_out 	= processConfiguration_Update((Ctrl_Configuration.Update) message_in);
        
		else if (message_in instanceof Ctrl_Calendars.Request)				message_out 	= processCalendars_Request();
		else if (message_in instanceof Ctrl_Calendars.Update)				message_out 	= processCalendars_Update((Ctrl_Calendars.Update) message_in);
        
		else if (message_in instanceof Ctrl_Fuel_Consumption.Request)		message_out 	= processFuelConsumption_Request();
 		else
        {
            System.out.println(dateTime2Time(now()) + " Unsupported message class received from client");
            message_out 													= (new Ctrl__Abstract()).new Nack();;
        }
       	reply(response, message_out);
     }
    public void dbOpen()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            dbName 									= "jdbc:mysql://localhost/hvac_database";
            dbConnection 							= DriverManager.getConnection(dbName, "root", "llenkcarb");
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
//    public Ctrl_Abstract.Data 			processTemperaturesReq()
//    {
//        dbOpen();
//        
//        Mgmt_Msg_Temperatures.Data 	returnBuffer  	= new Mgmt_Msg_Temperatures().new Data();
//
//        try
//        {
//            dbStatement 							= dbConnection.createStatement(1004, 1008);
//            
//            String				dbSQL				= "";
//            dbSQL									+= "SELECT     dateTime,        ";	
//            dbSQL									+= "           date,            ";	
//            dbSQL									+= "           time,            ";	
//            dbSQL									+= "           tempHotWater,    ";	
//            dbSQL									+= "           tempBoiler,      ";	
//            dbSQL									+= "           tempBoilerIn,    ";
//            dbSQL									+= "           tempBoilerOut,   ";
//            dbSQL									+= "           tempFloorIn,     ";
//            dbSQL									+= "           tempFloorOut,    ";
//            dbSQL									+= "           tempRadiatorIn,  ";
//            dbSQL									+= "           tempRadiatorOut, ";
//            dbSQL									+= "           tempOutside,     ";
//            dbSQL									+= "           tempLivingRoom	";
//            dbSQL									+= "FROM       temperatures     ";	
//            dbSQL									+= "ORDER BY   dateTime DESC    ";	
//            dbSQL									+= "LIMIT      1                ";	
//            
//            ResultSet 			dbResultSet 		= dbStatement.executeQuery(dbSQL);
//            dbResultSet.next();
//            returnBuffer.dateTime 					= dbResultSet.getLong("dateTime");
//            returnBuffer.date 						= dbResultSet.getString("date");
//            returnBuffer.time 						= dbResultSet.getString("time");
//            returnBuffer.tempHotWater 				= dbResultSet.getInt("tempHotWater");
//            returnBuffer.tempBoiler 				= dbResultSet.getInt("tempBoiler");
//            returnBuffer.tempBoilerIn 				= dbResultSet.getInt("tempBoilerIn");
//            returnBuffer.tempBoilerOut				= dbResultSet.getInt("tempBoilerOut");
//            returnBuffer.tempFloorIn 				= dbResultSet.getInt("tempFloorIn");
//            returnBuffer.tempFloorOut 				= dbResultSet.getInt("tempFloorOut");
//            returnBuffer.tempRadiatorIn 			= dbResultSet.getInt("tempRadiatorIn");
//            returnBuffer.tempRadiatorOut 			= dbResultSet.getInt("tempRadiatorOut");
//            returnBuffer.tempOutside 				= dbResultSet.getInt("tempOutside");
//            returnBuffer.tempLivingRoom 			= dbResultSet.getInt("tempLivingRoom");
//            
//            dbStatement.close();
//            dbConnection.close();
//        }
//        catch(SQLException eSQL)
//        {
//            eSQL.printStackTrace();
//        }
//        return returnBuffer;
//    }
    public Ctrl__Abstract 						processCalendars_Request()
    {
        dbOpen();
        
        Ctrl__Abstract									returnBuffer 		= new Ctrl__Abstract().new Nack();

        try
        {
            dbStatement 													= dbConnection.createStatement(1004, 1008);
            ResultSet 									dbResultSet 		= dbStatement.executeQuery("SELECT dateTime, calendars FROM calendars ORDER BY dateTime DESC LIMIT 1");
            dbResultSet.next();

            Long										dbDateTime			= dbResultSet.getLong("dateTime");
            String										dbJsonString		= dbResultSet.getString("calendars");
    		
            dbStatement.close();
            dbConnection.close();
 
            Ctrl_Calendars.Data							returnBufferPrep	= new Gson().fromJson(dbJsonString, Ctrl_Calendars.Data.class);
    		returnBufferPrep.dateTime										= dbDateTime;											// Add time stamp to mesage
     		returnBuffer													= (Ctrl__Abstract) returnBufferPrep;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return returnBuffer;
    }
    public Ctrl__Abstract		 				processCalendars_Update(Ctrl_Calendars.Update message_in)
    {
    	dbOpen();
        
        Ctrl__Abstract 									returnBuffer		= new Ctrl__Abstract().new Ack();

        try
        {
            dbStatement 													= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 									dbResultSet 		= dbStatement.executeQuery("SELECT dateTime, Date, Time, Calendars FROM Calendars ORDER BY dateTime DESC LIMIT 1");

            Gson gson 														= new GsonBuilder().setPrettyPrinting().create();
    		String 										dbJsonString 		= gson.toJson((Ctrl_Calendars.Update) message_in);

    		Long										dateTime			= System.currentTimeMillis();		// Do not use dateTime supplied in input message
    		
    		dbResultSet.moveToInsertRow();		
            dbResultSet.updateDouble	("dateTime", 				dateTime);
            dbResultSet.updateString	("date", 					dateTime2Date(dateTime));
            dbResultSet.updateString	("time", 					dateTime2Time(dateTime));
            dbResultSet.updateString	("Calendars", 				dbJsonString);
            dbResultSet.insertRow();

            dbStatement.close();
            dbConnection.close();

            returnBuffer													= new Ctrl__Abstract().new Ack();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            returnBuffer													= new Ctrl__Abstract().new Nack();
        }
        return returnBuffer;
    }
    public Ctrl__Abstract		 				processConfiguration_Request()
    {
        dbOpen();
        
        Ctrl__Abstract 									returnBuffer		= new Ctrl__Abstract().new Nack();

        try
        {
            dbStatement 													= dbConnection.createStatement(1004, 1008);
            ResultSet 									dbResultSet 		= dbStatement.executeQuery("SELECT dateTime, configuration FROM configuration ORDER BY dateTime DESC LIMIT 1");
            dbResultSet.next();
 
            Long										dbDateTime			= dbResultSet.getLong("dateTime");
            String										dbJsonString		= dbResultSet.getString("configuration");
    		
            dbStatement.close();
            
            Ctrl_Configuration.Data						returnBufferPrep	= new Gson().fromJson(dbJsonString, Ctrl_Configuration.Data.class);
    		returnBufferPrep.dateTime										= dbDateTime;											// Add time stamp to mesage
             
            dbStatement 													= dbConnection.createStatement(1004, 1008);
            dbResultSet 													= dbStatement.executeQuery("SELECT dateTime, FuelConsumed FROM Fuel ORDER BY dateTime DESC LIMIT 1");
            dbResultSet.next();

            returnBufferPrep.burner.fuelConsumption							= dbResultSet.getLong("FuelConsumed");
            
            dbConnection.close();
 
    		returnBuffer													= (Ctrl_Configuration.Data) returnBufferPrep;
       }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return returnBuffer;
    }
    public Ctrl__Abstract		 				processConfiguration_Update(Ctrl_Configuration.Update message_in)
    {
    	dbOpen();
        
        Ctrl__Abstract 									returnBuffer		= new Ctrl__Abstract().new Ack();

        try
        {
            dbStatement 													= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 									dbResultSet 		= dbStatement.executeQuery("SELECT dateTime, Date, Time, Configuration FROM configuration ORDER BY dateTime DESC LIMIT 1");

            Gson gson 														= new GsonBuilder().setPrettyPrinting().create();
    		String 										dbJsonString 		= gson.toJson((Ctrl_Configuration.Data) message_in);

    		Long										dateTime			= System.currentTimeMillis();		// Do not use dateTime supplied in input message
    		
    		dbResultSet.moveToInsertRow();
            dbResultSet.updateDouble	("dateTime", 				dateTime);
            dbResultSet.updateString	("date", 					dateTime2Date(dateTime));
            dbResultSet.updateString	("time", 					dateTime2Time(dateTime));
            dbResultSet.updateString	("Configuration", 			dbJsonString);
            dbResultSet.insertRow();

            dbStatement.close();
            dbConnection.close();

            returnBuffer													= new Ctrl__Abstract().new Ack();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            returnBuffer													= new Ctrl__Abstract().new Nack();
        }
        return returnBuffer;
    }
    public Ctrl__Abstract 						processFuelConsumption_Request()
    {
        dbOpen();
        
        Ctrl__Abstract									returnBuffer 		= new Ctrl__Abstract().new Nack();

        try
        {
            dbStatement 													= dbConnection.createStatement(1004, 1008);
            ResultSet 									dbResultSet 		= dbStatement.executeQuery("SELECT dateTime, FuelConsumed FROM Fuel ORDER BY dateTime DESC LIMIT 1");
            dbResultSet.next();

            Long										dbDateTime			= dbResultSet.getLong("dateTime");
            Long										dbFuelConsumed		= dbResultSet.getLong("FuelConsumed");
            
            Ctrl_Fuel_Consumption.Data					dbData				= new Ctrl_Fuel_Consumption.Data();
    		dbData.fuelConsumed												= dbFuelConsumed;
    		dbData.dateTime													= dbDateTime;

    		returnBuffer													= (Ctrl__Abstract) dbData;
    		
    		dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return returnBuffer;
    }

    public void reply(HttpServletResponse response, Ctrl__Abstract message_out) throws IOException 
    {
        System.out.println(dateTime2Time(now()) + " ----Class replied " + message_out.getClass().toString());
        response.reset();
        response.setHeader("Content-Type", "application/x-java-serialized-object");
        ObjectOutputStream 								output 				= new ObjectOutputStream(response.getOutputStream());
		output.writeObject(message_out);
        output.flush();
        output.close();
    }
    public String dateTime2String(Long dateTime)
    {
    	String					dateTimeString		= "";
 
        SimpleDateFormat 		sdf 				= new SimpleDateFormat("yyyy_MM_dd HH:mm:ss.SSS");
        GregorianCalendar 		calendar 			= new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        dateTimeString								= sdf.format(dateTime);
    	
    	return dateTimeString;
    }
    public String dateTime2Date(Long dateTime)
    {
    	String					dateTimeString		= "";
 
        SimpleDateFormat 		sdf 				= new SimpleDateFormat("yyyy_MM_dd");
        GregorianCalendar 		calendar 			= new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        dateTimeString								= sdf.format(dateTime);
    	
    	return dateTimeString;
    }
    public String dateTime2Time(Long dateTime)
    {
    	String					dateTimeString		= "";
 
        SimpleDateFormat 		sdf 				= new SimpleDateFormat("HH:mm:ss.SSS");
        GregorianCalendar 		calendar 			= new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        dateTimeString								= sdf.format(dateTime);
    	
    	return dateTimeString;
    }
    public Long now()
    {
    	return System.currentTimeMillis();
    }
}
