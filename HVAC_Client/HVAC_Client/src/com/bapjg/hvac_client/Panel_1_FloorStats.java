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

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_1_FloorStats 								extends 					Panel_0_Fragment 
{		
//	public TCP_Task												task;
	private Ctrl_Temperatures.Data								temperatureData;
	private Element_Heading										listHeading;
	private Element_Standard									dQFloor;
	private Element_Standard									dQRoomPlus;
	private Element_Standard									dQRoomMinus;
	private Element_Centered_x_1								formula1;
	private Element_Standard									parameterP;
	private Element_Standard									parameterM;
	private Element_Centered_x_1								formula2;
	private Element_Standard									parameterK;

	
	public Panel_1_FloorStats()
	{
		super("Standard");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	displayTitles("Temperatures", "Statistics");
    	super.TCP_Send(new Ctrl_Temperatures().new Request());
    	
    	listHeading																			= new Element_Heading("Obtained at");

    	dQFloor 																			= new Element_Standard("dQ Floor");
    	dQRoomPlus 																			= new Element_Standard("dQ+ Room");
    	dQRoomMinus 																		= new Element_Standard("dQ- Room");

    	formula1																			= new Element_Centered_x_1("dQ Floor  =  p dQ+  =  m dQ-");
    	
    	parameterP 																			= new Element_Standard("p  =");
    	parameterM 																			= new Element_Standard("m  =");

    	formula2																			= new Element_Centered_x_1("delta t(in)  =  delta t(out) x (p - m)/(p - m + mp²)");

    	parameterK																			= new Element_Standard("(p - m)/(p - m + mp²)  =");
    	
       	panelInsertPoint.addView(listHeading);
       	panelInsertPoint.addView(dQFloor);
       	panelInsertPoint.addView(dQRoomPlus);
       	panelInsertPoint.addView(dQRoomMinus);
       	panelInsertPoint.addView(new Element_Filler());
       	panelInsertPoint.addView(formula1);
       	panelInsertPoint.addView(parameterP);
       	panelInsertPoint.addView(parameterM);
       	panelInsertPoint.addView(new Element_Filler());
       	panelInsertPoint.addView(formula2);
       	panelInsertPoint.addView(parameterK);
           	
        return panelView;
    }
	public void processFinishTCP(Ctrl__Abstract result) 
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
			
			Integer												dQFloorValue				= temperatureData.tempFloorOut   - temperatureData.tempFloorIn;
			Integer												dQRoomPlusValue				= temperatureData.tempLivingRoom - temperatureData.tempFloorIn;
			Integer												dQRoomMinusValue			= temperatureData.tempLivingRoom - temperatureData.tempOutside;

			dQFloor				.setValue 		(Global.displayTemperature	(dQFloorValue));
			dQRoomPlus			.setValue		(Global.displayTemperature	(dQRoomPlusValue));
			dQRoomMinus			.setValue		(Global.displayTemperature	(dQRoomMinusValue));
			
			
			float 												paramP						= 0;
			float 												paramM						= 0;

			if (dQRoomPlusValue > 0)
			{
				paramP 																		= dQFloorValue.floatValue() / dQRoomPlusValue.floatValue();
				parameterP		.setValue		(paramP);
			}
			else
			{
				parameterP		.setValue		("Not significant");
			}
			if (dQRoomMinusValue > 0)
			{
				paramM 																		= dQFloorValue.floatValue() / dQRoomMinusValue.floatValue();
				parameterM		.setValue		(paramM);
			}
			else
			{
				parameterM		.setValue		("Not significant");
			}
			if ((dQRoomPlusValue > 0) && (dQRoomMinusValue > 0))
			{
				float 											paramK 						= (paramP - paramM) / (paramP - paramM + paramP * paramP * paramM);
				parameterK		.setValue		(paramK);
			}
			else
			{
				parameterK		.setValue		("Not significant");
			}

		}
	}
	public void setListens()
	{
	}
}

