package com.bapjg.hvac_client;

import java.util.ArrayList;

import com.bapjg.hvac_client.Mgmt_Msg_Configuration;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Fragment_Configuration extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
{
	private Adapter_Thermometers 		adapter;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        View myView =  inflater.inflate(R.layout.fragment_configuration, container, false);
        //myView.findViewById(R.id.buttonThermometers).setOnClickListener((OnClickListener) this);
        //ViewGroup myViewGroup = (ViewGroup) myView;
        //setButtonOnClick(myViewGroup);
        return myView;
    }
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
        if (position > 0)
        {
        	System.out.println("position : " + position);
	        Activity activity											= (Activity) Global.actContext;
	        ViewGroup 					viewGroup						= (ViewGroup) activity.findViewById(R.id.Detail_View);
        	
	        LayoutInflater 				inflater 						= (LayoutInflater) Global.actContext.getSystemService(Global.actContext.LAYOUT_INFLATER_SERVICE);
        	
        	View 						newView 						= inflater.inflate(R.layout.detail_thermometer, viewGroup, true);

            //Need to setup an update screen
        }
        else
        {
	    	// We have clicked in Title area
        	
        	
        	// This sets height of the listview 0, and item view 100
	    	// Cant remember why - the if clause is just to not execute the code until I remember why
        	if (position == 0)
        	{
        		
        	}
        	else
        	{
		    	AbsListView 				layout_list						= (AbsListView) view.findViewById(R.id.List_View);
		        LinearLayout.LayoutParams	params_list						= (LinearLayout.LayoutParams) layout_list.getLayoutParams();
		        params_list.height											= 0;
		        layout_list.setLayoutParams(params_list);
		        
		        LinearLayout 				layout_item						= (LinearLayout) view.findViewById(R.id.Item_View);
		        LinearLayout.LayoutParams	params_item						= (LinearLayout.LayoutParams) layout_item.getLayoutParams();
		        params_item.height											= 100;
		        layout_item.setLayoutParams(params_item);
        	}
       }
   	}
    public void onClick(View myView)
    {
    	Button 								myButton 					= (Button) myView;
    	Integer								myId						= myButton.getId();
    	
    	if (myId == R.id.buttonThermometers)
    	{
	    	Mgmt_Msg_Configuration			message_in					= Global.configuration;
	        ArrayList  						data		 				= Global.configuration.thermometerList;
	        Activity activity											= (Activity) Global.actContext;
	        AdapterView <Adapter_Thermometers> view						= (AdapterView) activity.findViewById(R.id.List_View);
	        
	        Adapter_Thermometers 			adapter						= new Adapter_Thermometers(Global.actContext, R.id.List_View, data);
	        
	        view.setAdapter(adapter);
	        view.setOnItemClickListener((OnItemClickListener) this);	
    	}
    }
    private void setButtonOnClick(ViewGroup v) 
    {
        View a;
        for(int i = 0; i < v.getChildCount(); i++) 
        {
            a = v.getChildAt(i);
            if (a instanceof ViewGroup)
        	{
            	setButtonOnClick((ViewGroup) a);
        	}
            else if (a instanceof Button) 
            {
                a.setOnClickListener((OnClickListener) this);
            }
        }
        return;
    }
}