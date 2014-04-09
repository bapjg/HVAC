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
    	
    	if (myCaption.equalsIgnoreCase("Hot Water"))
    	{
    		System.out.println("Action Hot Water Click");
    		panelFragment 												= new Panel_4_Action(R.layout.panel_4_actions_hotwater);

			fTransaction												= fManager.beginTransaction();
    		fTransaction.replace(R.id.panel_container, panelFragment);
    		fTransaction.addToBackStack(null);
    		fTransaction.commit();
    		System.out.println("Action Hot Water processed");
    	}
    	else if (myCaption.equalsIgnoreCase("Action2"))
    	{
    		System.out.println("Action2 Click");
    		System.out.println("Action2 processed");
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
