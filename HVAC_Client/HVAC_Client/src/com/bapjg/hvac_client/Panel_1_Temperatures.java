package com.bapjg.hvac_client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_1_Temperatures 								extends 					Panel_0_Fragment 
{		
	public TCP_Task												task;
	private Ctrl_Temperatures.Data								temperatureData;
	private Element_Standard									tempBoiler;
	private Element_Standard									tempHW;
	private Element_Standard									tempOutside;
	private Element_Standard  									tempLivingRoom;
	private Element_Standard  									tempFloorOut;
	private Element_Standard  									tempFloorIn;
	private Element_Standard  									tempBoilerOut;
	private Element_Standard  									tempRadiatorOut;
	private Element_Standard  									tempRadiatorIn;
	private Element_Standard  									tempBoilerIn;
	
	public Panel_1_Temperatures()
	{
		super("Standard");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	displayTitles("Temperatures", "Readings");
    	TCP_Send(new Ctrl_Temperatures().new Request());
    	
    	tempBoiler 																			= new Element_Standard("Boiler");
    	tempHW 																				= new Element_Standard("Hot Water");
    	tempOutside 																		= new Element_Standard("Outside");
    	tempLivingRoom 																		= new Element_Standard("Living Room");
    	tempFloorOut 																		= new Element_Standard("Floor Out");
    	tempFloorIn 																		= new Element_Standard("Floor In");
    	tempBoilerOut 																		= new Element_Standard("Boiler Out");
    	tempRadiatorOut																		= new Element_Standard("Radiator Out");
    	tempRadiatorIn																		= new Element_Standard("Radiator In");
    	tempBoilerIn 																		= new Element_Standard("Boiler In");

       	Element_Heading											listHeading					= new Element_Heading("Thermometer", "Temperature");

       	panelInsertPoint.addView(listHeading);
       	panelInsertPoint.addView(tempBoiler);
       	panelInsertPoint.addView(tempHW);
       	panelInsertPoint.addView(tempOutside);
       	panelInsertPoint.addView(new Element_Filler());
       	panelInsertPoint.addView(tempLivingRoom);
       	panelInsertPoint.addView(new Element_Filler());
       	panelInsertPoint.addView(tempFloorOut);
       	panelInsertPoint.addView(tempFloorIn);
       	panelInsertPoint.addView(tempBoilerOut);
       	panelInsertPoint.addView(new Element_Filler());
       	panelInsertPoint.addView(tempRadiatorOut);
       	panelInsertPoint.addView(tempRadiatorIn);
       	panelInsertPoint.addView(new Element_Filler());
       	panelInsertPoint.addView(tempBoilerIn);
          	
        return panelView;
    }
	public void processFinishTCP(Ctrl__Abstract result) 
	{             
		super.processFinishTCP(result);
		if 	(result instanceof Ctrl_Temperatures.Data)
		{
			temperatureData																	= (Ctrl_Temperatures.Data) result;																	
			displayContents();
		}
        setListens();
    } 
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
//			((TextView) panelView.findViewById(R.id.Date)).setText				(Global.displayDateShort	(temperatureData.dateTime));
//			((TextView) panelView.findViewById(R.id.Time)).setText				(Global.displayTimeShort	(temperatureData.dateTime));
//			
			tempBoiler			.setValue 	(Global.displayTemperature	(temperatureData.tempBoiler));
			tempHW				.setValue	(Global.displayTemperature	(temperatureData.tempHotWater));
			tempOutside			.setValue	(Global.displayTemperature	(temperatureData.tempOutside));

	    	tempLivingRoom		.setValue	(Global.displayTemperature	(temperatureData.tempLivingRoom));
	    	
	    	tempFloorOut		.setValue	(Global.displayTemperature	(temperatureData.tempFloorOut));
	    	tempFloorIn			.setValue	(Global.displayTemperature	(temperatureData.tempFloorIn));
	    	tempBoilerOut		.setValue	(Global.displayTemperature	(temperatureData.tempBoilerOut));
	    	
	    	tempRadiatorOut		.setValue	(Global.displayTemperature	(temperatureData.tempRadiatorOut));
	    	tempRadiatorIn		.setValue	(Global.displayTemperature	(temperatureData.tempRadiatorIn));
	    	
	    	tempBoilerIn		.setValue	(Global.displayTemperature	(temperatureData.tempBoilerIn));
		}
	}
	public void setListens()
	{
	}
}

