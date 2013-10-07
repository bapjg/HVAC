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

import com.bapjg.hvac_client.*;
import com.bapjg.hvac_client.Mgmt_Msg_Abstract.Ping;
import com.bapjg.hvac_client.Mgmt_Msg_Calendar.Data;

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
        Object 							message_in 	= null;
        Mgmt_Msg_Abstract 				message_out = null;
        
        try
        {
//            System.out.println("Step 1");
            ObjectInputStream 			input 		= new ObjectInputStream(request.getInputStream());
//            System.out.println("Step 2");
            message_in 								= input.readObject();
//            System.out.println("Step 3");
        }
        catch (ClassNotFoundException eCNF)
        {
            eCNF.printStackTrace();
            message_out 							= (new Mgmt_Msg_Abstract()).new Nack();
        }
        catch (IOException eIO)
        {
            System.out.println("An IO Exception occured : " + eIO);
            message_out 							= (new Mgmt_Msg_Abstract()).new Nack();
        }
        catch (Exception e)
        {
            System.out.println("An Exception occured : " + e);
            message_out 							= (new Mgmt_Msg_Abstract()).new Nack();
        }
        
        if (message_in == null)
        {
            message_out 							= (new Mgmt_Msg_Abstract()).new Nack();
        } 
        else if (message_in.getClass() == Mgmt_Msg_Temperatures.Request.class)
        {
            message_out 							= processTemperaturesReq();
        } 
		else if (message_in.getClass() == Mgmt_Msg_Calendar.Request.class)
        {
            message_out 							= processCalendarRequest();
        } 
		else
        {
            System.out.println("Unsupported message class received from client");
            message_out 							= (new Mgmt_Msg_Abstract()).new Nack();;
        }
        
        reply(response, message_out);

    }
    public void dbOpen()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            dbName 								= "jdbc:mysql://localhost/hvac_database";
            dbConnection 						= DriverManager.getConnection(dbName, "root", "llenkcarb");
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
    public Mgmt_Msg_Temperatures.Data processTemperaturesReq()
    {
        dbOpen();
        
        Mgmt_Msg_Temperatures.Data 	returnBuffer  	= new Mgmt_Msg_Temperatures().new Data();

        try
        {
            dbStatement 							= dbConnection.createStatement(1004, 1008);
            
            String				dbSQL				= "";
            dbSQL									+= "SELECT     dateTime,        ";	
            dbSQL									+= "           tempBoiler,      ";	
            dbSQL									+= "           tempHotWater,    ";	
            dbSQL									+= "           tempBoilerIn,    ";
            dbSQL									+= "           tempFloorOut,    ";
            dbSQL									+= "           tempFloorCold,   ";
            dbSQL									+= "           tempFloorHot,    ";
            dbSQL									+= "           tempRadiatorOut, ";
            dbSQL									+= "           tempRadiatorIn,  ";
            dbSQL									+= "           tempOutside,     ";
            dbSQL									+= "           tempLivingRoom	";
            dbSQL									+= "FROM       temperatures     ";	
            dbSQL									+= "ORDER BY   dateTime DESC    ";	
            dbSQL									+= "LIMIT      1                ";	
            
            ResultSet 			dbResultSet 		= dbStatement.executeQuery(dbSQL);
            dbResultSet.next();
            returnBuffer.dateTime 					= dbResultSet.getString("dateTime");
            returnBuffer.tempBoiler 				= dbResultSet.getInt("tempBoiler");
            returnBuffer.tempHotWater 				= dbResultSet.getInt("tempHotWater");
            returnBuffer.tempBoilerIn 				= dbResultSet.getInt("tempBoilerIn");
            returnBuffer.tempFloorOut 				= dbResultSet.getInt("tempFloorOut");
            returnBuffer.tempFloorCold 				= dbResultSet.getInt("tempFloorCold");
            returnBuffer.tempFloorHot 				= dbResultSet.getInt("tempFloorHot");
            returnBuffer.tempRadiatorOut 			= dbResultSet.getInt("tempRadiatorOut");
            returnBuffer.tempRadiatorIn 			= dbResultSet.getInt("tempRadiatorIn");
            returnBuffer.tempOutside 				= dbResultSet.getInt("tempOutside");
            returnBuffer.tempLivingRoom 			= dbResultSet.getInt("tempLivingRoom");
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException eSQL)
        {
            eSQL.printStackTrace();
        }

        return returnBuffer;
    }
    public Mgmt_Msg_Calendar.Data processCalendarRequestIndex()
    {
        dbOpen();
        
        Mgmt_Msg_Calendar.Data 		returnBuffer 	= new Mgmt_Msg_Calendar().new Data();

//        returnBuffer.calendars 					= "";
//        try
//        {
//            dbStatement 						= dbConnection.createStatement(1004, 1008);
//            ResultSet 			dbResultSet 	= dbStatement.executeQuery("SELECT MAX(dateTime) AS dateTime FROM calendars");
//            dbResultSet.next();
//            returnBuffer.dateTime 				= dbResultSet.getString("dateTime");
//            dbStatement.close();
//            dbConnection.close();
//        }
//        catch(SQLException eSQL)
//        {
//            eSQL.printStackTrace();
//        }
        returnBuffer.dateTime 					= "2013_01_01 00:01:02";

        return returnBuffer;
    }
    public Mgmt_Msg_Calendar.Data processCalendarRequest()
    {
        dbOpen();
        
        Mgmt_Msg_Calendar.Data 	returnBuffer 	= new Mgmt_Msg_Calendar().new Data();


        try
        {
            dbStatement 						= dbConnection.createStatement(1004, 1008);
            ResultSet 			dbResultSet 	= dbStatement.executeQuery("SELECT dateTime, calendars FROM calendars ORDER BY dateTime DESC LIMIT 1");
            dbResultSet.next();
            //returnBuffer.dateTime 				= dbResultSet.getString("dateTime");
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return returnBuffer;
    }
    public void reply(HttpServletResponse response, Mgmt_Msg_Abstract message_out) throws IOException 
    {
        response.reset();
        response.setHeader("Content-Type", "application/x-java-serialized-object");
        ObjectOutputStream 		output				= null;;
		
		output 										= new ObjectOutputStream(response.getOutputStream());
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

}
