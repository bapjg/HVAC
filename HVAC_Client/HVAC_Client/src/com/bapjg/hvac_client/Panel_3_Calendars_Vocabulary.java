package com.bapjg.hvac_client;

import HVAC_Messages.*;
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
import android.widget.Button;
import android.widget.TextView;


@SuppressLint("ValidFragment")
//Template												NEWNEWNEW					= NEWNEWNEW
//Template												variable					= something
//Template												ext/imp						class
public class Panel_3_Calendars_Vocabulary 				extends 					Panel_0_Fragment
{		
	private LayoutInflater								myInflater;
	private Activity									myActivity;
	private ViewGroup									myContainer;
	private View										myAdapterView;
	private FragmentManager								myFragmentManager;
	
	public Panel_3_Calendars_Vocabulary()
	{
		super();
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        myInflater																	= inflater;
        myContainer 																= container;
        myActivity																	= getActivity();
        myFragmentManager 															= myActivity.getFragmentManager();
        View 											panelView					= myInflater.inflate(R.layout.panel_3_calendars, container, false);
        TextView 										name						= (TextView) panelView.findViewById(R.id.name);
        name.setText("Vocabulary");		
        
        myAdapterView																= (AdapterView) panelView.findViewById(R.id.List_View);

        // This part can be in processFinishTCP/HTTP 
        HTTP_Send(new Ctrl_Calendars().new Request());
        
        return panelView;
     }
//    @Override
//	public void onClick(View myView) 
//	{
//    	System.out.println("We have arrived in onClick/panel3Calendars again");
//    	
//    	Button 											myButton 					= (Button) myView;
//    	String											myCaption					= myButton.getText().toString();
//						
//		// Set all textColours to white				
//		ViewGroup 										viewParent					= (ViewGroup) myView.getParent();
//		for (int i = 0; i < viewParent.getChildCount(); i++)
//		{
//			Button										buttonChild 				= (Button) viewParent.getChildAt(i);
//			buttonChild.setTextColor(Color.WHITE);
//		}
//		
//		((Button) myView).setTextColor(Color.YELLOW);
//    	
//    	if (myCaption.equalsIgnoreCase("Thermometers"))
//    	{
//    		// buttonThermometersClick(myView);	
//    	}
//	}
	public void processFinishHTTP(Ctrl_Abstract result) 
	{  
		Activity										activity					= getActivity();		

		if (result instanceof Ctrl_Calendars.Data)
		{
			Global.eRegCalendars				 									= (Ctrl_Calendars.Data) result;
			displayHeader();
			displayContents();
	        
//	        view.setOnItemClickListener((OnItemClickListener) this);	

		}
		else
		{
			Global.toaster("Data NOTNOTNOT received", true);
		}
	}
	public void displayHeader()
	{
	}
	public void displayContents()
	{
        AdapterView <Adapter_3_Calendars_Words> 	view						= (AdapterView) myContainer.findViewById(R.id.List_View);
		Adapter_3_Calendars_Words					adapter						= new Adapter_3_Calendars_Words(Global.actContext, R.id.List_View, Global.eRegCalendars.wordList);
		view.setAdapter(adapter);
		view.setOnItemClickListener(this);
	}
	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
     	System.out.println("position : " + position);
    	Ctrl_Calendars.Word							itemData					= Global.eRegCalendars.wordList.get(position);

    	Item_3_Calendars_Vocabulary					itemFragment				= new Item_3_Calendars_Vocabulary(itemData);
    	
//        ViewGroup 									viewGroup					= (ViewGroup) myActivity.findViewById(R.id.panel_container);
//        	View 										newView 					= myInflater.inflate(R.layout.detail_thermometer, viewGroup, true);
   	 			
    	FragmentTransaction 						fTransaction 				= myFragmentManager.beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
   	}
}

