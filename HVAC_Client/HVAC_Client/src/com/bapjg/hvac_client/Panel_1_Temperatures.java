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
    	
    	tempBoiler 																			= new Element_Standard(getActivity(), "Boiler");
    	tempHW 																				= new Element_Standard(getActivity(), "Hot Water");
    	tempOutside 																		= new Element_Standard(getActivity(), "Outside");
    	tempLivingRoom 																		= new Element_Standard(getActivity(), "Living Room");
    	tempFloorOut 																		= new Element_Standard(getActivity(), "Floor Out");
    	tempFloorIn 																		= new Element_Standard(getActivity(), "Floor In");
    	tempBoilerOut 																		= new Element_Standard(getActivity(), "Boiler Out");
    	tempRadiatorOut																		= new Element_Standard(getActivity(), "Radiator Out");
    	tempRadiatorIn																		= new Element_Standard(getActivity(), "Radiator In");
    	tempBoilerIn 																		= new Element_Standard(getActivity(), "Boiler In");

       	Element_Heading											listHeading					= new Element_Heading(getActivity(), "Thermometer", "Temperature");

       	panelInsertPoint.addView(listHeading);
       	panelInsertPoint.addView(tempBoiler);
       	panelInsertPoint.addView(tempHW);
       	panelInsertPoint.addView(tempOutside);
       	panelInsertPoint.addView(new Element_Filler(getActivity()));
       	panelInsertPoint.addView(tempLivingRoom);
       	panelInsertPoint.addView(new Element_Filler(getActivity()));
       	panelInsertPoint.addView(tempFloorOut);
       	panelInsertPoint.addView(tempFloorIn);
       	panelInsertPoint.addView(tempBoilerOut);
       	panelInsertPoint.addView(new Element_Filler(getActivity()));
       	panelInsertPoint.addView(tempRadiatorOut);
       	panelInsertPoint.addView(tempRadiatorIn);
       	panelInsertPoint.addView(new Element_Filler(getActivity()));
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
			tempBoiler.setTextRight 	(Global.displayTemperature	(temperatureData.tempBoiler));
			tempHW.setTextRight			(Global.displayTemperature	(temperatureData.tempHotWater));
			tempOutside.setTextRight	(Global.displayTemperature	(temperatureData.tempOutside));

	    	tempLivingRoom.setTextRight(Global.displayTemperature	(temperatureData.tempLivingRoom));
	    	
	    	tempFloorOut.setTextRight(Global.displayTemperature		(temperatureData.tempFloorOut));
	    	tempFloorIn.setTextRight(Global.displayTemperature		(temperatureData.tempFloorIn));
	    	tempBoilerOut.setTextRight(Global.displayTemperature	(temperatureData.tempBoilerOut));
	    	
	    	tempRadiatorOut.setTextRight(Global.displayTemperature	(temperatureData.tempRadiatorOut));
	    	tempRadiatorIn.setTextRight(Global.displayTemperature	(temperatureData.tempRadiatorIn));
	    	
	    	tempBoilerIn.setTextRight(Global.displayTemperature		(temperatureData.tempBoilerIn));
		}
	}
	public void setListens()
	{
	}
}

