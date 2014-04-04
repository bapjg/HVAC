package com.bapjg.hvac_client;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class Panel_4_Action 		extends 	Panel_0_Fragment  
									implements 	View.OnClickListener, AdapterView.OnItemClickListener
{
	public Panel_4_Action()
	{
		super();
	}
    public Panel_4_Action(int menuLayout)
    {
		super(menuLayout);
    }
	public void onClick(View myView) 
	{
    	System.out.println("nonono We have arrived in onClick again");
    	
//    	Button 								myButton 					= (Button) myView;
//    	String								myCaption					= myButton.getText().toString();
//    	
//		// Set all textColours to white
//		ViewGroup 							viewParent					= (ViewGroup) myView.getParent();
//		for (int i = 0; i < viewParent.getChildCount(); i++)
//		{
//			Button							buttonChild 				= (Button) viewParent.getChildAt(i);
//			buttonChild.setTextColor(Color.WHITE);
//		}
//		
//		((Button) myView).setTextColor(Color.YELLOW);
//    	
//    	if (myCaption.equalsIgnoreCase("Thermometers"))
//    	{
//    		// buttonThermometersClick(myView);	
//    	}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
		
	}
}

