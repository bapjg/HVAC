package com.bapjg.hvac_client;

import HVAC_Messages.Ctrl_Abstract;
import HVAC_Messages.Ctrl_Calendars;
import HVAC_Messages.Ctrl_Configuration;
import HVAC_Messages.Ctrl_Temperatures;
import HVAC_Messages.Ctrl_Configuration.Request;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class Panel_3_Calendars_Circuits 				extends 					Panel_0_Fragment
														implements					AdapterView.OnItemClickListener	
{
	private LayoutInflater								myInflater;
	private Activity									myActivity;
	private ViewGroup									myContainer;
	private FragmentManager								myFragmentManager;
	
	public String										circuitName;

	public Panel_3_Calendars_Circuits()
	{
		super();
		circuitName																	= "";
	}
    public Panel_3_Calendars_Circuits(String circuitName)
    {
		super();
		this.circuitName															= circuitName;
   }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        
        // Inflate the layout for this fragment
//        myFragmentManager 															= myActivity.getFragmentManager();
        View 											panelView					= inflater.inflate(R.layout.panel_3_calendars, container, false);
        TextView 										heading						= (TextView) panelView.findViewById(R.id.name);
        heading.setText(this.circuitName);	

        AdapterView <Adapter_3_Calendars_Circuits>		adapterView					= (AdapterView) panelView.findViewById(R.id.List_View);

        Adapter_3_Calendars_Circuits					arrayAdapter				= null;	
        for (Ctrl_Calendars.Circuit 		circuit 	: Global.eRegCalendars.circuitList)
        {
        	if (circuit.name.equalsIgnoreCase(this.circuitName))
        	{
        		arrayAdapter														= new Adapter_3_Calendars_Circuits(Global.actContext, R.id.List_View, circuit.calendarList);
        	}
        }
        
        adapterView.setAdapter(arrayAdapter);
      	adapterView.setOnItemClickListener((OnItemClickListener) this);

        return panelView;
    }
	public void OnItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		myActivity																	= getActivity();
    	FragmentTransaction								fTransaction				= getActivity().getFragmentManager().beginTransaction();
//    	Fragment 										panelFragment				= new Item_3_Calendars_Circuits();
//    	fTransaction.replace(R.id.panel_container, panelFragment);
    	fTransaction.commit();  
	}
    private OnItemClickListener 						itemListener 				= new OnItemClickListener()
    {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
	    	System.out.println("onItemClick");
	    	System.out.println("position :" + position);
	    	System.out.println("id :" + id);
		}
	};
	public void displayHeader()
	{
//        TextView 										heading						= (TextView) panelView.findViewById(R.id.name);
//        heading.setText(this.circuitName);	
	}
	public void displayContents(Ctrl_Temperatures.Data msg_received)
	{
	}
}

