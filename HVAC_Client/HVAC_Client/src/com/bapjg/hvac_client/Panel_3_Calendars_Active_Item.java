package com.bapjg.hvac_client;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Panel_3_Calendars_Active_Item 						extends 					Panel_0_Fragment
																implements					Dialog_Response
{		
	private Ctrl_Calendars.Circuit	 							itemData;
	private Element_Switch										switchActive;

	public Panel_3_Calendars_Active_Item(Ctrl_Calendars.Circuit itemData)
	{
		super("Ok_Delete");
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	switchActive			 															= new Element_Switch(itemData.name);

    	panelInsertPoint.addView(switchActive);

        displayContents();
        setListens();
        
        return panelView;
    }
	public void displayContents()
	{
		switchActive					.setChecked		(itemData.active);
   	}
	public void setListens()
	{
		switchActive					.setListener(this);
	}
    @Override
	public void onElementClick(View clickedView) 
	{
    	itemData.active																		= ! itemData.active;
    	Element_Switch					switchClicked										= (Element_Switch) clickedView;
    	switchClicked					.setChecked		(itemData.active);
 	}
// 	public void onPanelButtonOk()
//    {
//    	getFragmentManager().popBackStackImmediate();
//    }
//    @Override
// 	public void onPanelButtonAdd()
//    {
//     	Global.toaster("Invalid button in this situation", true);
//     	getFragmentManager().popBackStackImmediate();
//    }
//    @Override
// 	public void onPanelButtonDelete()
//    {
//    	Global.eRegCalendars.awayList.remove(itemData);
//    	getFragmentManager().popBackStackImmediate();
//    }
//    @Override
//    public void onDialogReturnWithId(int id)
////    public void onDialogReturn()
//    {
////    	if (id == 1)																		// dateTimeStart has just been modified
////    	{
////    		if (itemData.dateTimeStart > itemData.dateTimeEnd)								// Start is after end, end = start + 24 hours
////    		{
////    			itemData.dateTimeEnd														= itemData.dateTimeStart + 24L * 60L * 60L * 1000L;
////    		}
////    	}
////    	else																				// dateTimeEnd has just been modified
////    	{
////    		if (itemData.dateTimeStart > itemData.dateTimeEnd)								// Start is after end, end = start + 24 hours
////    		{
////    			itemData.dateTimeStart														= itemData.dateTimeEnd - 24L * 60L * 60L * 1000L;
////    		}
////    	}
//    	displayContents();
//    }
}