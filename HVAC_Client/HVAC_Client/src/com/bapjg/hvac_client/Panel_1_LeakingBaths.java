package com.bapjg.hvac_client;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_1_LeakingBaths 								extends 					Panel_0_Fragment 
{		
//	public TCP_Task												task;
	private Ctrl_Temperatures.Data								temperatureData;
	private Element_Heading										listHeading;
	private Element_Standard 									tempFloorOut;
	private Element_Standard 									tempFloorIn;
	private Element_Standard 									tempRoom;
	private Element_Standard 									tempOutSide;

	private Element_Standard_x_2								proportionFloor;
	private Element_Standard_x_2								proportionPlus;
	private Element_Standard_x_2								proportionMinus;

	
	public Panel_1_LeakingBaths()
	{
		super("Standard");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	displayTitles("Temperatures", "Leaking Baths");
    	super.TCP_Send(new Ctrl_Temperatures().new Request());
    	
    	listHeading																			= new Element_Heading("Obtained at");

    	tempFloorOut 																		= new Element_Standard("Floor Out");
    	tempFloorIn 																		= new Element_Standard("Floor In");
    	tempRoom 																			= new Element_Standard("Room");
    	tempOutSide																			= new Element_Standard("Outside");
    	
    	proportionFloor																		= new Element_Standard_x_2("Heat Supply");
    	proportionPlus																		= new Element_Standard_x_2("Room Addition");
    	proportionMinus																		= new Element_Standard_x_2("Room Loss");
        	
       	panelInsertPoint.addView(listHeading);
       	panelInsertPoint.addView(tempFloorOut);
       	panelInsertPoint.addView(proportionFloor);
       	panelInsertPoint.addView(tempFloorIn);
       	panelInsertPoint.addView(proportionPlus);
       	panelInsertPoint.addView(tempRoom);
       	panelInsertPoint.addView(proportionMinus);
       	panelInsertPoint.addView(tempOutSide);
           	
        return panelView;
    }
	public void processFinishTCP(Msg__Abstract result) 
	{             
		super.processFinishTCP(result);
		if 	(result instanceof Ctrl_Temperatures.Data)
		{
			temperatureData																	= (Ctrl_Temperatures.Data) result;																	
			displayContents();
			setListens();
		}
    } 
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
			listHeading			.setTextRight	(Global.displayTimeShort	(temperatureData.dateTime));
			tempFloorOut		.setValue 		(Global.displayTemperature	(temperatureData.tempFloorOut));
			tempFloorIn			.setValue 		(Global.displayTemperature	(temperatureData.tempFloorIn));
			tempRoom			.setValue 		(Global.displayTemperature	(temperatureData.tempLivingRoom));
			tempOutSide			.setValue 		(Global.displayTemperature	(temperatureData.tempOutside));
			
			Integer												dQFloorValue				= temperatureData.tempFloorOut   - temperatureData.tempFloorIn;
			Integer												dQRoomPlusValue				= temperatureData.tempFloorIn    - temperatureData.tempLivingRoom;
			Integer												dQRoomMinusValue			= temperatureData.tempLivingRoom - temperatureData.tempOutside;
			Integer												dQTotal						= temperatureData.tempFloorOut   - temperatureData.tempOutside;

			float												dQFloorPerCent				= dQFloorValue.floatValue()/dQTotal.floatValue();
			float												dQRoomPlusPerCent			= dQRoomPlusValue.floatValue()/dQTotal.floatValue();
			float												dQRoomMinusPerCent			= dQRoomMinusValue.floatValue()/dQTotal.floatValue();
			
			proportionFloor		.setRightValue	(Global.displayTemperature	(dQFloorValue),     "°C");
			proportionPlus		.setRightValue	(Global.displayTemperature	(dQRoomPlusValue),  "°C");
			proportionMinus		.setRightValue	(Global.displayTemperature	(dQRoomMinusValue), "°C");

			proportionFloor		.setCenterValue	(dQFloorPerCent     * 100F, "%");
			proportionPlus		.setCenterValue	(dQRoomPlusPerCent  * 100F, "%");
			proportionMinus		.setCenterValue	(dQRoomMinusPerCent * 100F, "%");
		}
	}
	public void setListens()
	{
	}
}

