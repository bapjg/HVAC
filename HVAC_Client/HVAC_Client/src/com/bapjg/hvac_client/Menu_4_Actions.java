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
public class Menu_4_Actions extends Menu_0_Fragment implements View.OnClickListener
{
	public Menu_4_Actions(Fragment panelFragment, int menuLayout)
	{
		super(panelFragment, menuLayout);
	}
	public void onClick(View myView) 
	{
		super.onClick(myView);
		
    	Button 								myButton 					= (Button) myView;
    	String								myCaption					= myButton.getText().toString();
    	
    	if (myCaption.equalsIgnoreCase("Action1"))
    	{
    		System.out.println("Action1 Click");
    		Fragment panelFragment = new Panel_41_Action();
    		FragmentTransaction transaction = getFragmentManager().beginTransaction();

    		transaction.replace(R.id.panel_container, panelFragment);
    		// and add the transaction to the back stack
    		transaction.addToBackStack(null);
    		transaction.commit();
    		System.out.println("Action1 processed");
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
