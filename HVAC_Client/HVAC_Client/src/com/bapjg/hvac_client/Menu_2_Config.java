package com.bapjg.hvac_client;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class Menu_2_Config extends Menu_0_Fragment implements View.OnClickListener
{
	public Menu_2_Config(int menuLayout)
	{
		super( menuLayout);
	}
	public void onClick(View myView) // This is the onClick event from the Menu
	{
		super.onClick(myView);
		
    	Button 								myButton 					= (Button) myView;
    	String								myCaption					= myButton.getText().toString();
    	

	   	if (myCaption.equalsIgnoreCase("Thermometers"))
    	{
    		FragmentTransaction transaction = getFragmentManager().beginTransaction();

    		transaction.replace(R.id.panel_container, Global.panelConfiguration);
    		// and add the transaction to the back stack
    		transaction.addToBackStack(null);
    		transaction.commit();
    	}

	   	
	   	
	   	
	   	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
//    	if (myCaption.equalsIgnoreCase("Thermometers"))
//    	{
//    		Global.panelConfiguration.menuButtonThermometersClick(myView);	
//    	}
	}
}
