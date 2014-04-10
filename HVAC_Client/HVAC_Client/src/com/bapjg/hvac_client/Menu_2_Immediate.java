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
public class Menu_2_Immediate extends Menu_0_Fragment implements View.OnClickListener
{
	public Menu_2_Immediate(int menuLayout)
	{
		super(menuLayout);
	}
	public void onClick(View myView) 
	{
		super.onClick(myView);
		
    	Button 								myButton 					= (Button) myView;
    	String								myCaption					= myButton.getText().toString();
    	FragmentManager 					fManager					= getFragmentManager();
    	FragmentTransaction					fTransaction;
    	Fragment 							panelFragment;
    	
    	if (myCaption.equalsIgnoreCase("Hot_Water"))
    	{
    		System.out.println("Action Hot_Water Click");
    		panelFragment 												= new Panel_2_Immediate(R.layout.panel_2_immediate, "Hot_Water");

    		fTransaction												= fManager.beginTransaction();
    		fTransaction.replace(R.id.panel_container, panelFragment);
//    		fTransaction.addToBackStack(null);
    		fTransaction.commit();
    		System.out.println("Action Hot Water processed");
    	}
    	else if (myCaption.equalsIgnoreCase("Radiator"))
    	{
    		System.out.println("Action Radiator Click");
    		panelFragment 												= new Panel_2_Immediate(R.layout.panel_2_immediate, "Radiator");

			fTransaction												= fManager.beginTransaction();
    		fTransaction.replace(R.id.panel_container, panelFragment);
//    		fTransaction.addToBackStack(null);
    		fTransaction.commit();
    		System.out.println("Action Radiator processed");
    	}
    	else if (myCaption.equalsIgnoreCase("Floor"))
    	{
    		System.out.println("Action Floor Click");
    		panelFragment 												= new Panel_2_Immediate(R.layout.panel_2_immediate, "Floor");

			fTransaction												= fManager.beginTransaction();
    		fTransaction.replace(R.id.panel_container, panelFragment);
//    		fTransaction.addToBackStack(null);
     		fTransaction.commit();
    		System.out.println("Action Floor processed");
    	}
	}
}
