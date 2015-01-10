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

public class Menu_2_Immediate 									extends 					Menu_0_Fragment 
{
	Element_MenuButton											buttonHotWater;
	Element_MenuButton											buttonRadiator;
	Element_MenuButton											buttonFloor;

	public Menu_2_Immediate()
	{
		super(false);
//		this.menuLayout																		= R.layout.menu_2_immediate;
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	buttonHotWater																		= new Element_MenuButton("Hot Water");
    	buttonRadiator																		= new Element_MenuButton("Radiator");
    	buttonFloor																			= new Element_MenuButton("Floor");

    	menuInsertPoint.addView(buttonHotWater);
    	menuInsertPoint.addView(buttonRadiator);
    	menuInsertPoint.addView(buttonFloor);
     	
    	buttonHotWater			.setListener(this);
    	buttonRadiator			.setListener(this);
    	buttonFloor				.setListener(this);

    	onElementClick(buttonHotWater);
    	
    	return menuView;
    }	
    @Override
    public void onElementClick(View clickedView)
	{
		super.onElementClick(clickedView);
		
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
   	
    	if 		(clickedView == buttonHotWater) 				panelFragment 				= new Panel_2_Immediate("Hot_Water");
     	else if (clickedView == buttonRadiator)					panelFragment 				= new Panel_2_Immediate("Radiator");
    	else if (clickedView == buttonFloor)					panelFragment 				= new Panel_2_Immediate("Floor");

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
//    public void onClick(View myView) 
//	{
//		super.onClick(myView);
//		
//    	String													caption						= ((Button) myView).getText().toString();
//    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
//    	Fragment 												panelFragment				= null;
//    	
//    	if      (caption.equalsIgnoreCase("Hot Water"))			panelFragment 				= new Panel_2_Immediate("Hot_Water");
//		else if (caption.equalsIgnoreCase("Radiator"))			panelFragment 				= new Panel_2_Immediate("Radiator");
//		else if (caption.equalsIgnoreCase("Floor"))				panelFragment 				= new Panel_2_Immediate("Floor");
//
//    	if (panelFragment != null)
//    	{
//    		fTransaction.replace(R.id.panel_container, panelFragment);
//    	}
//    	fTransaction.commit();   	 	
//	}
}
