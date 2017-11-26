package eReg_Forms;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class Form_Control_LogIt_Items extends AbstractTableModel 
{

	ArrayList <Form_Control_LogIt_Item> logItItems = new ArrayList <Form_Control_LogIt_Item> ();
	private int maxSize = 5;

    public Form_Control_LogIt_Items()
    {
    }    
    public Form_Control_LogIt_Items(int maxSize)
    {
    	this.maxSize																		= maxSize;
    }    
    public void add(Form_Control_LogIt_Item item)
    {
    	if (logItItems.size() >= maxSize)
    	{
    		logItItems.remove(logItItems.size() - 1);
    	}
    	logItItems.add(0, item);
    }
    public void add(String dateTimeStamp, String severity, String sender, String message)
    {
    	Form_Control_LogIt_Item							item 								= new Form_Control_LogIt_Item(dateTimeStamp, severity, sender, message);
    	add(item);
    }
    @Override
    public int getRowCount() 
    {
        return logItItems.size();
    }

    @Override
    public int getColumnCount() 
    {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) 
    {
    	switch (columnIndex)
    	{
    		case 0 : return "Date & Time";
     		case 1 : return "Severity";
    		case 2 : return "Sender";
    		case 3 : return "Message";
    		default : return "Error";
    	}
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch (columnIndex)
    	{
    		case 0 : return logItItems.get(rowIndex).dateTimeStamp;
    		case 1 : return logItItems.get(rowIndex).severity;
    		case 2 : return logItItems.get(rowIndex).sender;
    		case 3 : return logItItems.get(rowIndex).message;
    		default : return "Error";
    	}
    }
}
