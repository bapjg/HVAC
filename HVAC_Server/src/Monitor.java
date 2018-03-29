import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.sql.DataSource;

import HVAC_Common.Ctrl_Fuel_Consumption;
import HVAC_Common.Msg__Abstract;
import HVAC_Common.Rpt_Action;
import HVAC_Common.Rpt_MixerMouvement;
import HVAC_Common.Rpt_PID;
import HVAC_Common.Rpt_Report;
import HVAC_Common.Rpt_Temperatures;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Monitor extends HttpServlet
{

    public 		Connection 		dbConnection;
    public 		Statement 		dbStatement;
    public 		String 			dbName;
    private 	DataSource 		dbPool;
	
    public Monitor()
    {
        super();
    	dbName 																				= "jdbc:mysql://localhost/hvac_database";
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	// Accessed via http://192.168.5.10:8888/hvac/Monitor
    	
    	response.setContentType("text/html");
        PrintWriter 											out 						= response.getWriter();
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello World!</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>This uses Monitor/doGet transaction in TomCat</p>");
        out.println("<p>If SQL Works, a 'All is Ok' should appear after Hello World</p>");
        out.println("<p>if problem check Project/Properties/Project Facets/Compiler Version = 1.6</p>");
        
        dbOpen();
        Statement 												dbStatement 				= null;
        String dbField = "Z";
        
        try
        {
            dbStatement 																	= dbConnection.createStatement();
            String 												dbSQL 						= "SELECT * FROM Check_Test";
            ResultSet 											dbResult 					= dbStatement.executeQuery(dbSQL);
            dbResult.next();										
            dbField 																		= dbResult.getString(1);		// Only one field in database
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            dbField																			= e.toString();
        }
        out.println("<h1>Hello World</h1>");
        out.println("<p>" + dbField + "</p>");
        out.println("</body>");
        out.println("</html>");
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        Object 													message_in 					= null;
        Msg__Abstract 											message_out 				= null;
        
        try
        {
            ObjectInputStream 									input 						= new ObjectInputStream(request.getInputStream());
            message_in 																		= input.readObject();
        }
        catch (ClassNotFoundException eCNF) { System.out.println("Monitor : Caught CNF");        		eCNF.printStackTrace();            						message_out = new Msg__Abstract().new Nack(); }
        catch (IOException eIO)        		{ System.out.println("Monitor : Caught IO");        		System.out.println("An IO Exception occured : " + eIO); message_out = new Msg__Abstract().new Nack(); }
        catch (Exception e)        			{ System.out.println("Monitor : Caught another exception"); System.out.println("An Exception occured : " + e);      message_out = new Msg__Abstract().new Nack(); }

    	if 		(message_in == null)        						{ System.out.println("Monitor : Null received from client");	message_out	= new Msg__Abstract().new Nack();       				} 
    	else if (message_in instanceof Rpt_Temperatures)    		{ Rpt_Temperatures             readings = (Rpt_Temperatures)             message_in; message_out = processTemperatures  (readings); } 
		else if (message_in instanceof Ctrl_Fuel_Consumption.Update){ Ctrl_Fuel_Consumption.Update readings = (Ctrl_Fuel_Consumption.Update) message_in; message_out = processFuel          (readings); } 
		else if (message_in instanceof Rpt_Report)        			{ Rpt_Report                   readings = (Rpt_Report)                   message_in; message_out = processReport        (readings); } 
		else if (message_in instanceof Rpt_Action)        			{ Rpt_Action                   readings = (Rpt_Action)                   message_in; message_out = processAction        (readings); } 			
		else if (message_in instanceof Rpt_PID.Update)        		{ Rpt_PID.Update		       readings = (Rpt_PID.Update)               message_in; message_out = processPID           (readings); } 			
		else if (message_in instanceof Rpt_MixerMouvement)	        { Rpt_MixerMouvement 	       readings = (Rpt_MixerMouvement)           message_in; message_out = processMixerMouvement(readings); } 
		else        												{ System.out.println("Monitor : Unsupported message class received from client");    message_out = new Msg__Abstract().new Nack();  }
        reply(response, message_out);
    }
    public Msg__Abstract processTemperatures(Rpt_Temperatures readings)
    {
        dbOpen();
        
        try
        {
            dbStatement 																	= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 											dbResultSet 				= dbStatement.executeQuery("SELECT * FROM temperatures LIMIT 1");
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
            return new Msg__Abstract().new Nack();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            return new Msg__Abstract().new Nack();
        }
        return new Msg__Abstract().new Ack();
    }
    public Msg__Abstract processTempOutside3DDD(Rpt_Temperatures readings)
    {
        // Return tempOutSide 3Day Diurnal Distribution
    	// For each day average between 8am and 8pm
    	// Weighted average
    	dbOpen();
        
        try
        {
            dbStatement 																	= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            
            String 												sql 						= "";
            sql																				+= "SELECT    date,                              ";
            sql																				+= "          AVG(tempOutside)                   ";
            sql																				+= "FROM      temperatures                       ";
            sql																				+= "WHERE     date >= SUBDATE(CURDATE(), 3)      ";
            sql																				+= "AND  	  time > 08:00                       ";
            sql																				+= "AND  	  time < 20:00                       ";
            sql																				+= "GROUP BY  date                               ";

            ResultSet 											dbResultSet 				= dbStatement.executeQuery(sql);
            while (dbResultSet.next())			
            {			
            	String 											day							= dbResultSet.getString("date");
            }					
            dbResultSet.next();					
            Integer												dayM3						= dbResultSet.getInt("tempOutside");
            dbResultSet.next();					
            Integer												dayM2						= dbResultSet.getInt("tempOutside");
            dbResultSet.next();					
            Integer												dayM1						= dbResultSet.getInt("tempOutside");
            Long												dbDateTime					= dbResultSet.getLong("dateTime");
            Integer												dayM0Weighted				= (dayM1 * 3 + dayM2 * 2 + dayM3) / 6 ;
            dbResultSet.close();
            
            // TODO PUT RESULTS
            
             
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException eSQL)
        {
        	eSQL.printStackTrace();
            return new Msg__Abstract().new Nack();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            return new Msg__Abstract().new Nack();
        }
        return new Msg__Abstract().new Ack();
    }
    @SuppressWarnings("unused")
	public Msg__Abstract processMixerMouvement(Rpt_MixerMouvement readings)
    {
    	// Used to handle mixer handling statistics
    	// No longer required
    	// Should remove this from Client (eReg)
    	if (true) 
    	{
    		return new Msg__Abstract().new Ack();
    	}

        dbOpen();
        
        try
        {
            dbStatement 																	= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 											dbResultSet 				= dbStatement.executeQuery("SELECT * FROM temperatures LIMIT 1");
            
            if (readings.dateTimeStart > 0)
            {
	            dbResultSet.moveToInsertRow();
	            
	            dbResultSet.updateDouble	("dateTime", 				readings.dateTimeStart);
	            dbResultSet.updateString	("date", 					dateTime2Date(readings.dateTimeStart));
	            dbResultSet.updateString	("time", 					dateTime2Time(readings.dateTimeStart));
	            dbResultSet.updateInt		("positionTracked", 		readings.positionTrackedStart);
	 
	            dbResultSet.insertRow();
	        }

            if ((readings.dateTimeEnd > 0) && (readings.dateTimeEnd > readings.dateTimeStart))
            {
	            dbResultSet.moveToInsertRow();
	            
	            dbResultSet.updateDouble	("dateTime", 				readings.dateTimeEnd);
	            dbResultSet.updateString	("date", 					dateTime2Date(readings.dateTimeEnd));
	            dbResultSet.updateString	("time", 					dateTime2Time(readings.dateTimeEnd));
	            dbResultSet.updateInt		("positionTracked", 		readings.positionTrackedEnd);
	
	            dbResultSet.insertRow();
            }
            dbStatement.close();
        }
        catch(SQLException eSQL)
        {
        	eSQL.printStackTrace();
            return new Msg__Abstract().new Nack();
        }
        
        try
        {
            dbStatement 																	= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 											dbResultSet 				= dbStatement.executeQuery("SELECT * FROM mixer LIMIT 1");
            
            if (readings.dateTimeStart > 0)
            {
	            dbResultSet.moveToInsertRow();
	            
	            dbResultSet.updateDouble	("dateTime", 				readings.dateTimeStart);
	            dbResultSet.updateString	("date", 					dateTime2Date(readings.dateTimeStart));
	            dbResultSet.updateString	("time", 					dateTime2Time(readings.dateTimeStart));
	            dbResultSet.updateInt		("positionTracked", 		readings.positionTrackedStart);
	 
	            dbResultSet.insertRow();
            }
            else
            {
            	System.out.println("processMixerMouvement readings.dateTimeStart = zero, infact : " + readings.dateTimeStart);
            }

            if (readings.dateTimeEnd > 0)
            {
            	dbResultSet.moveToInsertRow();
            
	            dbResultSet.updateDouble	("dateTime", 				readings.dateTimeEnd);
	            dbResultSet.updateString	("date", 					dateTime2Date(readings.dateTimeEnd));
	            dbResultSet.updateString	("time", 					dateTime2Time(readings.dateTimeEnd));
	            dbResultSet.updateInt		("positionTracked", 		readings.positionTrackedEnd);

            	dbResultSet.insertRow();
            }
            else
            {
            	System.out.println("processMixerMouvement readings.dateTimeEnd = zero, infact : " + readings.dateTimeEnd);
            }
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException eSQL)
        {
        	eSQL.printStackTrace();
            return new Msg__Abstract().new Nack();
        }
        return new Msg__Abstract().new Ack();
    }
    @SuppressWarnings("all")
    public Msg__Abstract processPID(Rpt_PID.Update readings)
    {
    	if (true) return new Msg__Abstract().new Ack();
        dbOpen();

        try
        {
        	dbStatement 																	= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 											dbResultSet 				= dbStatement.executeQuery("SELECT * FROM pid LIMIT 1");
            dbResultSet.moveToInsertRow();

            dbResultSet.updateDouble	("dateTime", 			readings.dateTime);
            dbResultSet.updateString	("date", 				dateTime2Date(readings.dateTime));
            dbResultSet.updateString	("time", 				dateTime2Time(readings.dateTime));
            dbResultSet.updateInt		("target", 				readings.target);
            dbResultSet.updateInt		("tempCurrent", 		readings.tempCurrent);
            dbResultSet.updateInt		("tempCurrentError",	readings.tempCurrentError);
            
            dbResultSet.updateInt		("termProportional",	readings.termProportional);
            dbResultSet.updateInt		("termDifferential",	readings.termDifferential);
            dbResultSet.updateInt		("termIntegral", 		readings.termIntegral);
            
            dbResultSet.updateInt		("gainProportional",	readings.gainProportional);
            dbResultSet.updateInt		("gainDifferential",	readings.gainDifferential);
            dbResultSet.updateInt		("gainIntegral", 		readings.gainIntegral);
            
            dbResultSet.updateFloat		("kP", 					readings.kP);
            dbResultSet.updateFloat		("kD", 					readings.kD);
            dbResultSet.updateFloat		("kI", 					readings.kI);
            
            dbResultSet.updateInt		("gainTotal", 			readings.gainTotal);
            
            dbResultSet.updateInt		("tempFloorOut",		readings.tempOut);
            dbResultSet.updateInt		("tempBoiler", 			readings.tempBoiler);
            dbResultSet.updateInt		("positionTracked",		readings.positionTracked);
            
            dbResultSet.updateBoolean	("startMovement",		readings.startMovement);
            dbResultSet.insertRow();
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new Msg__Abstract().new Nack();
        }
        return new Msg__Abstract().new Ack();
    }
//    public Rpt_Abstract processFuel(Ctrl_Fuel_Consumption.Update readings)
    public Msg__Abstract processFuel(Ctrl_Fuel_Consumption.Update readings)
    {
        dbOpen();

        try
        {
            dbStatement 																	= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 											dbResultSet 				= dbStatement.executeQuery("SELECT * FROM fuel LIMIT 1");
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
            return new Msg__Abstract().new Nack();
        }
        return new Msg__Abstract().new Ack();
    }
    public Msg__Abstract processReport(Rpt_Report readings)
    {
        dbOpen();
        Boolean 												returnAck 					= false;
        
        try
        {
            dbStatement 																	= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 											dbResultSet 				= dbStatement.executeQuery("SELECT * FROM reports LIMIT 1");
            dbResultSet.moveToInsertRow();
            
            dbResultSet.updateDouble	("dateTime", 		readings.dateTime);
            dbResultSet.updateString	("date", 			dateTime2Date(readings.dateTime));
            dbResultSet.updateString	("time", 			dateTime2Time(readings.dateTime));
            dbResultSet.updateString	("reportType", 		readings.reportType);
            dbResultSet.updateString	("className", 		readings.className);
            dbResultSet.updateString	("methodName",		 readings.methodName);
            dbResultSet.updateString	("reportText", 		readings.reportText);
            dbResultSet.insertRow();
            returnAck 																		= true;
            
            dbStatement.close();
            dbConnection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new Msg__Abstract().new Nack();
        }
        return new Msg__Abstract().new Ack();
    }
    public Msg__Abstract processAction(Rpt_Action readings)
    {
    	dbOpen();

        try
        {
            dbStatement 																	= dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet 											dbResultSet 				= dbStatement.executeQuery("SELECT * FROM actions LIMIT 1");
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
            return new Msg__Abstract().new Nack();
        }
        return new Msg__Abstract().new Ack();
    }
    public void init() throws ServletException
    {
        Connection 												conn 						= null;
        Statement 												stmt 						= null;
        try										
        {										
            InitialContext 										ctx 						= new InitialContext();
            dbPool 																			= (DataSource) ctx.lookup("java:comp/env/jdbc/hvac");
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
            dbName 																			= "jdbc:mysql://localhost/hvac_database";
            dbConnection 																	= DriverManager.getConnection(dbName, "root", "llenkcarb");
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
    public void reply(HttpServletResponse response, Msg__Abstract message_out) throws IOException 
    {
        response.reset();
        response.setHeader("Content-Type", "application/x-java-serialized-object");
        ObjectOutputStream 										output						= null;;
		
		output 																				= new ObjectOutputStream(response.getOutputStream());
		output.writeObject(message_out);
        output.flush();
        output.close();
    }
    public String dateTime2String(Long dateTime)
    {
    	String													dateTimeString				= "";
										
        SimpleDateFormat 										sdf 						= new SimpleDateFormat("yyyy_MM_dd HH:mm:ss.SSS");
        GregorianCalendar 										calendar 					= new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        dateTimeString																		= sdf.format(dateTime);
    	
    	return dateTimeString;
    }
    public String dateTime2Date(Long dateTime)
    {
    	String													dateTimeString				= "";
										
        SimpleDateFormat 										sdf 						= new SimpleDateFormat("yyyy_MM_dd");
        GregorianCalendar 										calendar 					= new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        dateTimeString																		= sdf.format(dateTime);
    	
    	return dateTimeString;
    }
    public String dateTime2Time(Long dateTime)
    {
    	String													dateTimeString				= "";
										
        SimpleDateFormat 										sdf 						= new SimpleDateFormat("HH:mm:ss.SSS");
        GregorianCalendar 										calendar 					= new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        dateTimeString																		= sdf.format(dateTime);
    	
    	return dateTimeString;
    }
}
