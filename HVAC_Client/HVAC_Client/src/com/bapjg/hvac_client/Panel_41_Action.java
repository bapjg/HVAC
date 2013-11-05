package com.bapjg.hvac_client;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

public class Panel_41_Action extends Fragment  implements View.OnClickListener, AdapterView.OnItemClickListener
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.panel_41_actions, container, false);
    }
	public void onClick(View myView) 
	{
    	System.out.println("We have arrived in onClick again");
    	
    	Button 								myButton 					= (Button) myView;
    	String								myCaption					= myButton.getText().toString();
    	
		// Set all textColours to white
		ViewGroup 							viewParent					= (ViewGroup) myView.getParent();
		for (int i = 0; i < viewParent.getChildCount(); i++)
		{
			Button							buttonChild 				= (Button) viewParent.getChildAt(i);
			buttonChild.setTextColor(Color.WHITE);
		}
		
		((Button) myView).setTextColor(Color.YELLOW);
    	
    	if (myCaption.equalsIgnoreCase("Thermometers"))
    	{
    		// buttonThermometersClick(myView);	
    	}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
		
	}
}

