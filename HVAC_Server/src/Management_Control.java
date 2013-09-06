import eRegulation.*;
import java.io.*;
import java.sql.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class Management_Control extends HttpServlet
{

    public 		Connection 			dbConnection;
    public 		Statement 			dbStatement;
    public 		String 				dbName;
    private 	DataSource 			dbPool;
	
    public Management_Control()
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
        ObjectInputStream 		input 			= new ObjectInputStream(request.getInputStream());
        Object 					message_in 		= null;
        Message_Abstract message_out 			= null;
        try
        {
            message_in 							= input.readObject();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        if (message_in == null)
		{
            System.out.println("null null null");
		}
        if(message_in.getClass() == Message_Calendar_Request_Index.class)
        {
            message_out 						= processCalendarRequestIndex();
            response.reset();
            response.setHeader("Content-Type", "application/x-java-serialized-object");
            ObjectOutputStream 		output 		= new ObjectOutputStream(response.getOutputStream());
            output.writeObject(message_out);
            output.flush();
            output.close();
        } 
		else if(message_in.getClass() == Message_Calendar_Request_Data.class)
        {
            message_out 						= processCalendarRequestData();
            response.reset();
            response.setHeader("Content-Type", "application/x-java-serialized-object");
            ObjectOutputStream 		output 		= new ObjectOutputStream(response.getOutputStream());
            output.writeObject(message_out);
            output.flush();
            output.close();
        } 
		else
        {
            System.out.println("Unsupported message class received from client");
        }
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

    public Message_Calendar_Report processCalendarRequestIndex()
    {
        dbOpen();
        Message_Calendar_Report returnBuffer = new Message_Calendar_Report();
        returnBuffer.dateTime 					= "";
        returnBuffer.calendars 					= "";
        try
        {
            dbStatement 						= dbConnection.createStatement(1004, 1008);
            ResultSet 			dbResultSet 	= dbStatement.executeQuery("SELECT MAX(dateTime) AS dateTime FROM calendars");
            dbResultSet.next();
            returnBuffer.dateTime 				= dbResultSet.getString("dateTime");
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return returnBuffer;
    }

    public Message_Calendar_Report processCalendarRequestData()
    {
        dbOpen();
        Message_Calendar_Report returnBuffer 	= new Message_Calendar_Report();
        returnBuffer.dateTime 					= "";
        returnBuffer.calendars 					= "";
        try
        {
            dbStatement 						= dbConnection.createStatement(1004, 1008);
            ResultSet 			dbResultSet 	= dbStatement.executeQuery("SELECT dateTime, calendars FROM calendars ORDER BY dateTime DESC LIMIT 1");
            dbResultSet.next();
            returnBuffer.dateTime 				= dbResultSet.getString("dateTime");
            returnBuffer.calendars 				= dbResultSet.getString("calendars");
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return returnBuffer;
    }
}
