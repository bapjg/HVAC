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
public class Menu_6_Actions 									extends Menu_0_Fragment 	implements View.OnClickListener
{
	Element_MenuButton											buttonRelays;
	Element_MenuButton											buttonTestMail;
	Element_MenuButton											buttonStop;

	public Menu_6_Actions()
	{
		super(false);
//		this.menuLayout																		= R.layout.menu_6_actions;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		buttonRelays																		= new Element_MenuButton("Relays");
		buttonTestMail																		= new Element_MenuButton("Test Mail");
		buttonStop																			= new Element_MenuButton("Stop");

    	menuInsertPoint.addView(buttonRelays);
    	menuInsertPoint.addView(buttonTestMail);
    	menuInsertPoint.addView(buttonStop);
    	
    	buttonRelays			.setListener((Menu_0_Fragment) this);
    	buttonTestMail			.setListener((Menu_0_Fragment) this);
    	buttonStop				.setListener((Menu_0_Fragment) this);

    	return menuView;
	}
    public void onMenuElementClick(View clickedView)
	{
		super.onMenuElementClick(clickedView);
		
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
   	
    	if      (clickedView == buttonRelays)				panelFragment 					= new Panel_6_Actions_Relays();
    	else if (clickedView == buttonTestMail) 				panelFragment 				= new Panel_6_Actions_Test_Mail();
     	else if (clickedView == buttonStop)					panelFragment 					= new Panel_6_Actions_Stop();

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
	
	
//	public void onClick(View myView) 
//	{
//		super.onClick(myView);
//		
//    	String													caption						= ((Button) myView).getText().toString();
//    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
//    	Fragment 												panelFragment				= null;
//    	
//    	if      (caption.equalsIgnoreCase("Relays"))			panelFragment 				= new Panel_6_Actions_Relays();
//    	else if (caption.equalsIgnoreCase("Test Mail"))			panelFragment 				= new Panel_6_Actions_Test_Mail();
//    	else if (caption.equalsIgnoreCase("Stop"))				panelFragment 				= new Panel_6_Actions_Stop();
//
//    	if (panelFragment != null)
//    	{
//    		fTransaction.replace(R.id.panel_container, panelFragment);
//    	}
//		fTransaction.commit();
//	}
}
