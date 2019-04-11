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

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Menu_6_Actions 									extends 					Menu_0_Fragment
{
	Element_MenuButton											buttonRelays;
	Element_MenuButton											buttonTestMail;
	Element_MenuButton											buttonMixer;

	public Menu_6_Actions()
	{
		super(false, false);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		buttonRelays																		= new Element_MenuButton("Relays");
		buttonTestMail																		= new Element_MenuButton("Test Mail");
		buttonMixer																			= new Element_MenuButton("Mixer");

    	menuInsertPoint			.addView(buttonRelays);
    	menuInsertPoint			.addView(buttonTestMail);
    	menuInsertPoint			.addView(buttonMixer);
    	
    	buttonRelays			.setListener((Menu_0_Fragment) this);
    	buttonTestMail			.setListener((Menu_0_Fragment) this);
    	buttonMixer				.setListener((Menu_0_Fragment) this);
    	
    	onElementClick(buttonRelays);

    	return menuView;
	}
    public void onElementClick(View clickedView)
	{
		super.onElementClick(clickedView);
		
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
   	
    	if      (clickedView == buttonRelays)					panelFragment 				= new Panel_6_Actions_Relays();
    	else if (clickedView == buttonTestMail) 				panelFragment 				= new Panel_6_Actions_Test_Mail();
     	else if (clickedView == buttonMixer)					panelFragment 				= new Panel_6_Actions_Test_Mail(); // Panel_6_Actions_Mixer

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
}
