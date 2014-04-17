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
public class Menu_2_Immediate 				extends 					Menu_0_Fragment 
											implements 					View.OnClickListener
{
	public Menu_2_Immediate(int menuLayout)
	{
		super(menuLayout);
	}
	public void onClick(View myView) 
	{
   	 	super.onClick(myView);

		super.onClick(myView);
		
    	Button 								myButton 					= (Button) myView;
    	String								myCaption					= myButton.getText().toString();
    	FragmentManager 					fManager					= getFragmentManager();
    	FragmentTransaction					fTransaction				= fManager.beginTransaction();
    	Fragment 							panelFragment				= null;
    	
    	if (myCaption.equalsIgnoreCase("Hot Water"))
    	{
     		panelFragment 												= new Panel_2_Immediate(R.layout.panel_2_immediate, "Hot_Water");
    	}
    	else if (myCaption.equalsIgnoreCase("Radiator"))
    	{
    		panelFragment 												= new Panel_2_Immediate(R.layout.panel_2_immediate, "Radiator");
     	}
    	else if (myCaption.equalsIgnoreCase("Floor"))
    	{
    		panelFragment 												= new Panel_2_Immediate(R.layout.panel_2_immediate, "Floor");
    	}
    	if (panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();   	 	
	}
}
