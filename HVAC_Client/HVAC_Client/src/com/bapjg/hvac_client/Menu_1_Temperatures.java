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
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Menu_1_Temperatures 								extends 					Menu_0_Fragment 
{
	Element_MenuButton											buttonTemperatures;
	Element_MenuButton											buttonFloorStats;

	public Menu_1_Temperatures()
	{
		super(false);
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	buttonTemperatures																	= new Element_MenuButton("Temperatures");
    	buttonFloorStats																	= new Element_MenuButton("Floor Stats");
    	
    	menuInsertPoint.addView(buttonTemperatures);
    	menuInsertPoint.addView(buttonFloorStats);
    	
    	buttonTemperatures			.setListener(this);
    	buttonFloorStats			.setListener(this);
    	
    	onElementClick(buttonTemperatures);													// Force execution of panel
    	return menuView;
    }
    public void onElementClick(View clickedView)
	{
		super.onElementClick(clickedView);
		
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
   	
    	if      (clickedView == buttonTemperatures)				panelFragment 				= new Panel_1_Temperatures();
    	else if (clickedView == buttonFloorStats)				panelFragment 				= new Panel_1_FloorStats();

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
}
