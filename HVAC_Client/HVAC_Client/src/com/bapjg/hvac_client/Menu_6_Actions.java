package com.bapjg.hvac_client;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
//Template										variable			= something
//Template										ext/imp				class
public class Menu_6_Actions extends Menu_0_Fragment implements View.OnClickListener
{
	public Menu_6_Actions(int menuLayout)
	{
		super(menuLayout);
	}
	public void onClick(View myView) 
	{
		super.onClick(myView);
		
    	Button 									myButton 			= (Button) myView;
    	String									myCaption			= myButton.getText().toString();
    	FragmentManager 						fManager			= getFragmentManager();
    	FragmentTransaction						fTransaction		= fManager.beginTransaction();
    	Fragment 								panelFragment		= null;
    	
    	if (myCaption.equalsIgnoreCase("Relays"))
    	{
    		panelFragment 											= new Panel_6_Actions_Relays();
    	}
    	else if (myCaption.equalsIgnoreCase("Test Mail"))
    	{
    		panelFragment 											= new Panel_6_Actions_Test_Mail();
    	}
    	else if (myCaption.equalsIgnoreCase("Stop"))
    	{
    		panelFragment 											= new Panel_6_Actions_Stop();
    	}
    	else if (myCaption.equalsIgnoreCase("Action4"))
    	{
    		System.out.println("Action4 Click");
    		System.out.println("Action4 processed");
    	}
    	if (panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
		fTransaction.commit();
	}
}
