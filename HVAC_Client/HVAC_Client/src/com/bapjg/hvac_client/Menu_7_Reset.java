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
	Element_MenuButton											buttonReset;
	Element_MenuButton											buttonStop;
	

	public Menu_7_Reset()
	{
		super(false, false);
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	buttonReset																			= new Element_MenuButton("Reset");
       	buttonStop																			= new Element_MenuButton("Stop");

    	menuInsertPoint			.addView(buttonReset);
    	menuInsertPoint			.addView(buttonStop);
    	
    	buttonReset				.setListener((Menu_0_Fragment) this);
    	buttonStop				.setListener((Menu_0_Fragment) this);
    	
    	onElementClick(buttonStop);

    	return menuView;
    }	
    public void onElementClick(View clickedView)
	{
		super.onElementClick(clickedView);
		
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
   	
    	if 		(clickedView == buttonReset)					panelFragment 				= new Panel_7_Reset();
    	else if (clickedView == buttonStop)						panelFragment 				= new Panel_7_Reset_Stop();

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
}
