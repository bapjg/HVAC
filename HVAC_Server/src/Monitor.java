
import eRegulation.*;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class Monitor extends HttpServlet
{

    public 		Connection 		dbConnection;
    public 		Statement 		dbStatement;
    public 		String 			dbName;
    private 	DataSource 		dbPool;
	
    public Monitor()
    {
        super();
    	dbName 										= "jdbc:mysql://localhost/hvac_database";
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter 					out 		= response.getWriter();
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello World!</title>");
        out.println("</head>");
        out.println("<body>");
        
        dbOpen();
        Statement 						dbStatement = null;
        String dbField = "Z";
        
        try
        {
            dbStatement 							= dbConnection.createStatement();
            String 						dbSQL 		= "SELECT * FROM Test";
            ResultSet 					dbResult 	= dbStatement.executeQuery(dbSQL);
            dbResult.next();
            dbField 								= dbResult.getString(2);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        out.println((new StringBuilder("<h1>Hello World : ")).append(dbField).append("!</h1>").toString());
        out.println("</body>");
        out.println("</html>");
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        Object 							message_in 	= null;
        Message_Abstract 				message_out = null;
        
        try
        {
            ObjectInputStream 			input 		= new ObjectInputStream(request.getInputStream());
            message_in 								= input.readObject();
        }
        catch (ClassNotFoundException eCNF)
        {
        	System.out.println("Caught CNF");
        	eCNF.printStackTrace();
            message_out 							= new Message_Abstract().new Nack();
        }
        catch (IOException eIO)
        {
        	System.out.println("Caught IO");
        	System.out.println("An IO Exception occured : " + eIO);
        	message_out 							= new Message_Abstract().new Nack();
        }
        catch (Exception e)
        {
        	System.out.println("Caught another exception");
        	System.out.println("An Exception occured : " + e);
        	message_out 							= new Message_Abstract().new Nack();
        }

    	if (message_in == null)
        {
            System.out.println("Null received from client");
            message_out 							= new Message_Abstract().new Nack();
        } 
    	else if (message_in instanceof Message_Temperatures)
        {
    		Message_Temperatures 		readings 	= (Message_Temperatures) message_in;
            message_out								= processTemperatures(readings);
         } 
		else if (message_in instanceof Message_Fuel.Update)
        {
            Message_Fuel.Update			readings 	= (Message_Fuel.Update) message_in;
            message_out								= processFuel(readings);
        } 
		else if (message_in instanceof Message_Report)
        {
            Message_Report 				readings 	= (Message_Report) message_in;
            message_out								= processReport(readings);
        } 
		else if (message_in instanceof Message_Action)
        {
            Message_Action 				readings 	= (Message_Action) message_in;
            message_out								= processAction(readings);
        } 
		else if (message_in instanceof Message_PID.Update)
        {
            Message_PID.Update			readings 	= (Message_PID.Update) message_in;
            message_out								= processPID(readings);
        } 
		else if (message_in instanceof Message_MixerMouvement)
        {
			Message_MixerMouvement 		readings 	= (Message_MixerMouvement) message_in;
            message_out								= processMixerMouvement(readings);
         } 
		else
        {
            System.out.println("Unsupported message class received from client");
            message_out 							= new Message_Abstract().new Nack();
        }
        reply(response, message_out);
    }
    public Message_Abstract processTemperatures(Message_Temperatures readings)
    {
        dbOpen();
        
        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 				dbResultSet 	= dbStatement.executeQuery("SELECT * FROM temperatures LIMIT 1");
            dbResultSet.moveToInsertRow();
            
            dbResultSet.updateDouble	("dateTime", 				readings.dateTime);
            dbResultSet.updateString	("date", 					dateTime2Date(readings.dateTime));
            dbResultSet.updateString	("time", 					dateTime2Time(readings.dateTime));
            dbResultSet.updateInt		("tempHotWater", 			readings.tempHotWater.intValue());
            dbResultSet.updateInt		("tempBoiler", 				readings.tempBoiler.intValue());
            dbResultSet.updateInt		("tempBoilerIn", 			readings.tempBoilerIn.intValue());
            dbResultSet.updateInt		("tempBoilerOut", 			readings.tempBoilerOut.intValue());
            dbResultSet.updateInt		("tempFloorOut", 			readings.tempFloorOut.intValue());
            dbResultSet.updateInt		("tempFloorIn", 			readings.tempFloorIn.intValue());
            dbResultSet.updateInt		("tempRadiatorOut", 		readings.tempRadiatorOut.intValue());
            dbResultSet.updateInt		("tempRadiatorIn", 			readings.tempRadiatorIn.intValue());
            dbResultSet.updateInt		("tempOutside", 			readings.tempOutside.intValue());
            dbResultSet.updateInt		("tempLivingRoom", 			readings.tempLivingRoom.intValue());
            dbResultSet.updateFloat		("pidMixerDifferential",	readings.pidMixerDifferential);
            dbResultSet.updateFloat		("pidBoilerOutDifferential",readings.pidBoilerOutDifferential);
            dbResultSet.updateInt		("pidMixerTarget", 			readings.pidMixerTarget);
            dbResultSet.updateInt		("tempLivingRoomTarget", 	readings.tempLivingRoomTarget);
            dbResultSet.insertRow();
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException eSQL)
        {
        	eSQL.printStackTrace();
            return new Message_Abstract().new Nack();
        }
        return new Message_Abstract().new Ack();
    }
    public Message_Abstract processMixerMouvement(Message_MixerMouvement readings)
    {
        dbOpen();
        
        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 				dbResultSet 	= dbStatement.executeQuery("SELECT * FROM temperatures LIMIT 1");
            
            if (readings.dateTimeStart > 0)
            {
            dbResultSet.moveToInsertRow();
            
            dbResultSet.updateDouble	("dateTime", 				readings.dateTimeStart);
            dbResultSet.updateString	("date", 					dateTime2Date(readings.dateTimeStart));
            dbResultSet.updateString	("time", 					dateTime2Time(readings.dateTimeStart));
            dbResultSet.updateInt		("positionTracked", 		readings.positionTrackedStart);
 
            System.out.println("dateTime 1 :" + readings.dateTimeStart);
            System.out.println("date     1 :" + dateTime2Date(readings.dateTimeStart));
            System.out.println("    Time 1 :" + dateTime2Time(readings.dateTimeStart));
            System.out.println("pos      1 :" + readings.positionTrackedStart);

            dbResultSet.insertRow();
            }

            if (readings.dateTimeStart > 0)
            {
            dbResultSet.moveToInsertRow();
            
            dbResultSet.updateDouble	("dateTime", 				readings.dateTimeEnd);
            dbResultSet.updateString	("date", 					dateTime2Date(readings.dateTimeEnd));
            dbResultSet.updateString	("time", 					dateTime2Time(readings.dateTimeEnd));
            dbResultSet.updateInt		("positionTracked", 		readings.positionTrackedEnd);

            System.out.println("dateTime 2 :" + readings.dateTimeStart);
            System.out.println("date     2 :" + dateTime2Date(readings.dateTimeStart));
            System.out.println("    Time 2 :" + dateTime2Time(readings.dateTimeStart));
            System.out.println("pos      2 :" + readings.positionTrackedEnd);
            
            dbResultSet.insertRow();
            }
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException eSQL)
        {
        	eSQL.printStackTrace();
            return new Message_Abstract().new Nack();
        }
        return new Message_Abstract().new Ack();
    }
    public Message_Abstract processPID(Message_PID.Update readings)
    {
        dbOpen();

        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 				dbResultSet 	= dbStatement.executeQuery("SELECT * FROM pid LIMIT 1");
            dbResultSet.moveToInsertRow();

            dbResultSet.updateDouble	("dateTime", 		readings.dateTime);
            dbResultSet.updateString	("date", 			dateTime2Date(readings.dateTime));
            dbResultSet.updateString	("time", 			dateTime2Time(readings.dateTime));
            dbResultSet.updateInt		("target", 			readings.target);
            dbResultSet.updateFloat		("proportional", 	readings.proportional);
            dbResultSet.updateFloat		("differential", 	readings.differential);
            dbResultSet.updateFloat		("integral", 		readings.integral);
            dbResultSet.updateFloat		("kP", 				readings.kP);
            dbResultSet.updateFloat		("kD", 				readings.kD);
            dbResultSet.updateFloat		("kI", 				readings.kI);
            dbResultSet.updateFloat		("result", 			readings.result);
            dbResultSet.updateInt		("tempFloorOut",	readings.tempOut);
            dbResultSet.updateInt		("tempBoiler", 		readings.tempBoiler);
            dbResultSet.insertRow();
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new Message_Abstract().new Nack();
        }
        return new Message_Abstract().new Ack();
    }
    public Message_Abstract processFuel(Message_Fuel.Update readings)
    {
        dbOpen();

        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 				dbResultSet 	= dbStatement.executeQuery("SELECT * FROM fuel LIMIT 1");
            dbResultSet.moveToInsertRow();

            dbResultSet.updateDouble	("dateTime", 		readings.dateTime);
            dbResultSet.updateString	("date", 			dateTime2Date(readings.dateTime));
            dbResultSet.updateString	("time", 			dateTime2Time(readings.dateTime));
            dbResultSet.updateLong		("fuelConsumed", 	readings.fuelConsumed.longValue());
            dbResultSet.insertRow();
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new Message_Abstract().new Nack();
        }
        return new Message_Abstract().new Ack();
    }
    public Message_Abstract processReport(Message_Report readings)
    {
        dbOpen();
        Boolean 				returnAck 			= false;
        
        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 			dbResultSet 		= dbStatement.executeQuery("SELECT * FROM reports LIMIT 1");
            dbResultSet.moveToInsertRow();
            
            dbResultSet.updateDouble	("dateTime", 		readings.dateTime);
            dbResultSet.updateString	("date", 			dateTime2Date(readings.dateTime));
            dbResultSet.updateString	("time", 			dateTime2Time(readings.dateTime));
            dbResultSet.updateString	("reportType", 		readings.reportType);
            dbResultSet.updateString	("className", 		readings.className);
            dbResultSet.updateString	("methodName",		 readings.methodName);
            dbResultSet.updateString	("reportText", 		readings.reportText);
            dbResultSet.insertRow();
            returnAck 								= true;
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new Message_Abstract().new Nack();
        }
        return new Message_Abstract().new Ack();
    }
    public Message_Abstract processAction(Message_Action readings)
    {
    	dbOpen();

        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 			dbResultSet 		= dbStatement.executeQuery("SELECT * FROM actions LIMIT 1");
            dbResultSet.moveToInsertRow();
            
            dbResultSet.updateDouble	("dateTime", 		readings.dateTime);
            dbResultSet.updateString	("date", 			dateTime2Date(readings.dateTime));
            dbResultSet.updateString	("time", 			dateTime2Time(readings.dateTime));
            dbResultSet.updateString	("device", 			readings.device);
            dbResultSet.updateString	("action", 			readings.action);
            dbResultSet.insertRow();

            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new Message_Abstract().new Nack();
        }
        return new Message_Abstract().new Ack();
    }
    public void init() throws ServletException
    {
        Connection 						conn 		= null;
        Statement 						stmt 		= null;
        try
        {
            InitialContext 				ctx 		= new InitialContext();
            dbPool 									= (DataSource) ctx.lookup("java:comp/env/jdbc/hvac");
            if(dbPool == null)
			{
                throw new ServletException("Unknown DataSource 'jdbc/hvac'");
			}
//            conn 									= dbPool.getConnection();
//            stmt 									= conn.createStatement();
//            stmt.execute("SELECT * FROM reports");
//            
//            ResultSet 					resSet 		= stmt.executeQuery("SELECT * FROM reports");
//            stmt.close();
//            stmt = null;
//
//            conn.close();
            conn = null;
        }
        catch(NamingException ex)
        {
            ex.printStackTrace();
        }
//		catch (SQLException eSQL)
//		{
//			// TODO Auto-generated catch block
//			eSQL.printStackTrace();
//		}
        finally 
        {
            if (stmt != null) 
            {
                try 
                {
                    stmt.close();
                } 
                catch (SQLException eSQL) 
                {
                }
                stmt = null;
            }
            if (conn != null) 
            {
                try 
                {
                    conn.close();
                } 
                catch (SQLException sqlex) 
                {
                }
                conn = null;
            }
        }
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
    public void reply(HttpServletResponse response, Message_Abstract message_out) throws IOException 
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
}
