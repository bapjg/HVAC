
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
        catch(ClassNotFoundException eCNF)
        {
            eCNF.printStackTrace();
            message_out 							= new Message_Nack();
        }
        catch(IOException eIO)
        {
            System.out.println((new StringBuilder("An IO Exception occured : ")).append(eIO).toString());
            message_out 							= new Message_Nack();
        }
        
        if (message_in == null)
        {
        	System.out.println ("null message received");
        }
        
        if (message_in.getClass() == Message_Temperatures.class)
        {
            Message_Temperatures 		readings 	= (Message_Temperatures) message_in;
            message_out								= processTemperatures(readings);
         } 
		else if (message_in.getClass() == Message_Fuel.class)
        {
            Message_Fuel 				readings 	= (Message_Fuel) message_in;
            message_out								= processFuel(readings);
        } 
		else if (message_in.getClass() == Message_Report.class)
        {
            Message_Report 				readings 	= (Message_Report) message_in;
            message_out								= processReport(readings);
        } 
		else if (message_in.getClass() == Message_Action.class)
        {
            Message_Action 				readings 	= (Message_Action) message_in;
            message_out								= processAction(readings);
        } 
		else
        {
            System.out.println("Unsupported message class received from client");
            message_out								= new Message_Nack();
        }
        
        reply(response, message_out);
    }
    public Message_Abstract processTemperatures(Message_Temperatures readings)
    {
        dbOpen();
        
        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 				dbResultSet 	= dbStatement.executeQuery("SELECT * FROM temperatures");
            dbResultSet.moveToInsertRow();
            
            dbResultSet.updateString("dateTime", dateTime2String(readings.dateTime));
            dbResultSet.updateInt("tempHotWater", readings.tempHotWater.intValue());
            dbResultSet.updateInt("tempBoiler", readings.tempBoiler.intValue());
            dbResultSet.updateInt("tempBoilerIn", readings.tempBoilerIn.intValue());
            dbResultSet.updateInt("tempFloorOut", readings.tempFloorOut.intValue());
            dbResultSet.updateInt("tempFloorCold", readings.tempFloorCold.intValue());
            dbResultSet.updateInt("tempFloorHot", readings.tempFloorHot.intValue());
            dbResultSet.updateInt("tempRadiatorOut", readings.tempRadiatorOut.intValue());
            dbResultSet.updateInt("tempRadiatorIn", readings.tempRadiatorIn.intValue());
            dbResultSet.updateInt("tempOutside", readings.tempOutside.intValue());
            dbResultSet.updateInt("tempLivingRoom", readings.tempLivingRoom.intValue());
            dbResultSet.insertRow();
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException eSQL)
        {
        	eSQL.printStackTrace();
            return new Message_Nack();
        }
        return new Message_Ack();
    }
    public Message_Abstract processFuel(Message_Fuel readings)
    {
        dbOpen();
        Boolean 					returnAck 		= false;

        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 				dbResultSet 	= dbStatement.executeQuery("SELECT * FROM fuel");
            dbResultSet.moveToInsertRow();

            dbResultSet.updateString("dateTime", dateTime2String(readings.dateTime));
            dbResultSet.updateLong("fuelConsumed", readings.fuelConsumed.longValue());
            dbResultSet.insertRow();
            returnAck	 							= true;
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new Message_Nack();
        }
        return new Message_Ack();
    }
    public Message_Abstract processReport(Message_Report readings)
    {
        dbOpen();
        Boolean 				returnAck 			= false;
        
        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 			dbResultSet 		= dbStatement.executeQuery("SELECT * FROM reports");
            dbResultSet.moveToInsertRow();
            
            dbResultSet.updateString("dateTime", dateTime2String(readings.dateTime));
            dbResultSet.updateString("reportType", readings.reportType);
            dbResultSet.updateString("className", readings.className);
            dbResultSet.updateString("methodName", readings.methodName);
            dbResultSet.updateString("reportText", readings.reportText);
            dbResultSet.insertRow();
            returnAck 								= true;
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new Message_Nack();
        }
        return new Message_Ack();
    }
    public Message_Abstract processAction(Message_Action readings)
    {
    	dbOpen();

        try
        {
            dbStatement 							= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 			dbResultSet 		= dbStatement.executeQuery("SELECT * FROM actions");
            dbResultSet.moveToInsertRow();
            
            dbResultSet.updateString("dateTime", dateTime2String(readings.dateTime));
            dbResultSet.updateString("device", readings.device);
            dbResultSet.updateString("action", readings.action);
            dbResultSet.insertRow();

            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new Message_Nack();
        }
        return new Message_Ack();
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
                catch (SQLException sqlex) 
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
}
