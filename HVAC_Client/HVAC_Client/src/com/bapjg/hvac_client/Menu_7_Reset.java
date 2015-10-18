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

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Menu_7_Reset 										extends 					Menu_0_Fragment
{
	Element_MenuButton											buttonResetConfig;
	Element_MenuButton											buttonResetCalendars;

	public Menu_7_Reset()
	{
		super(false, false);
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	buttonResetConfig																	= new Element_MenuButton("Hot Water");
    	buttonResetCalendars																= new Element_MenuButton("Radiator");

    	menuInsertPoint			.addView(buttonResetConfig);
    	menuInsertPoint			.addView(buttonResetCalendars);
    	
    	buttonResetConfig		.setListener((Menu_0_Fragment) this);
    	buttonResetCalendars	.setListener((Menu_0_Fragment) this);
    	
    	onElementClick(buttonResetConfig);

    	return menuView;
    }	
    public void onElementClick(View clickedView)
	{
		super.onElementClick(clickedView);
		
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
   	
    	if      (clickedView == buttonResetConfig)				panelFragment 				= new Panel_6_Actions_Relays();
    	else if (clickedView == buttonResetCalendars) 			panelFragment 				= new Panel_6_Actions_Test_Mail();

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
}
