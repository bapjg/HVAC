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
public class Menu_4_Config 							extends 			Menu_0_Fragment 
													implements 			View.OnClickListener
{
	public Menu_4_Config(int menuLayout)
	{
		super( menuLayout);
	}
	public void onClick(View myView) // This is the onClick event from the Menu
	{
   	 	super.onClick(myView);
		
    	Button 								myButton 					= (Button) myView;
    	String								myCaption					= myButton.getText().toString();
    	FragmentManager 					fManager					= getFragmentManager();
    	FragmentTransaction					fTransaction;
    	Fragment 							panelFragment;
    	
    	if (myCaption.equalsIgnoreCase("Thermometers"))
    	{
     		panelFragment 												= new Panel_4_Configuration(R.layout.panel_4_configuration);

    		fTransaction												= fManager.beginTransaction();
    		fTransaction.replace(R.id.panel_container, panelFragment);
    		fTransaction.commit();
    	}
	}
}
