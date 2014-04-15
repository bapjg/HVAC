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
public class Menu_5_Actions extends Menu_0_Fragment implements View.OnClickListener
{
	public Menu_5_Actions(int menuLayout)
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
    	
    	if (myCaption.equalsIgnoreCase("Relays"))
    	{
    		panelFragment 												= new Panel_5_Actions_Relays(R.layout.panel_5_actions_relays);

			fTransaction												= fManager.beginTransaction();
    		fTransaction.replace(R.id.panel_container, panelFragment);
    		fTransaction.commit();
    	}
    	else if (myCaption.equalsIgnoreCase("Test Mail"))
    	{
    		panelFragment 												= new Panel_5_Actions_Test_Mail(R.layout.panel_5_actions_test_mail);

			fTransaction												= fManager.beginTransaction();
    		fTransaction.replace(R.id.panel_container, panelFragment);
    		fTransaction.commit();
    	}
    	else if (myCaption.equalsIgnoreCase("Action3"))
    	{
    		System.out.println("Action3 Click");
    		System.out.println("Action3 processed");
    	}
    	else if (myCaption.equalsIgnoreCase("Action4"))
    	{
    		System.out.println("Action4 Click");
    		System.out.println("Action4 processed");
    	}
	}
}
