package com.bapjg.hvac_client;

import HVAC_Messages.Ctrl_Abstract;
import HVAC_Messages.Ctrl_Configuration;
import HVAC_Messages.Ctrl_Configuration.Request;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;


@SuppressLint("ValidFragment")
//Template										variable			= something
//Template										ext/imp				class
public class Panel_3_Calendars 					extends 			Panel_0_Fragment
												implements 			TCP_Response
{
	private Adapter_Circuits	 				adapter;
	private LayoutInflater						myInflater;
	private Activity							myActivity;
	private ViewGroup							myContainer;
	private View								myAdapterView;
	private FragmentManager						myFragmentManager;
	
	public String								circuitName;

	public Panel_3_Calendars()
	{
		super();
		circuitName													= "";
	}
    public Panel_3_Calendars(int menuLayout, String circuitName)
    {
		super(menuLayout);
		this.circuitName											= circuitName;
   }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
//OLDOLDOLD
    	// Inflate the layout for this fragment
//        return inflater.inflate(R.layout.panel_3_calendars, container, false);

        
        // Inflate the layout for this fragment
        myInflater													= inflater;
        myContainer 												= container;
        myActivity													= getActivity();
        myFragmentManager 											= myActivity.getFragmentManager();
        View 									panelView			= myInflater.inflate(R.layout.panel_5_config_header, container, false);
        myAdapterView												= (AdapterView) panelView.findViewById(R.id.List_View);


// From configuration        
//        HTTP_Send(new Ctrl_Configuration().new Request());
 
        return panelView;
    }
    @Override
	public void onClick(View myView) 
	{
    	System.out.println("We have arrived in onClick/panel3Calendars again");
    	
    	Button 									myButton 			= (Button) myView;
    	String									myCaption			= myButton.getText().toString();
    	
		// Set all textColours to white
		ViewGroup 								viewParent			= (ViewGroup) myView.getParent();
		for (int i = 0; i < viewParent.getChildCount(); i++)
		{
			Button								buttonChild 		= (Button) viewParent.getChildAt(i);
			buttonChild.setTextColor(Color.WHITE);
		}
		
		((Button) myView).setTextColor(Color.YELLOW);
    	
    	if (myCaption.equalsIgnoreCase("Thermometers"))
    	{
    		// buttonThermometersClick(myView);	
    	}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
		
	}
	public void TCP_Send(Ctrl_Abstract message)
	{
		TCP_Task								task				= new TCP_Task();
	   	task.callBack												= this;					// processFinish
	   	task.execute(message);
	}
	public void processFinishTCP(Ctrl_Abstract result) 
	{
		Activity								activity			= getActivity();		
	}
}

