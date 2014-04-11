package com.bapjg.hvac_client;

import HVAC_Messages.*;
import HVAC_Messages.Ctrl_Temperatures.Request;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Panel_5_Action 		extends 	Panel_0_Fragment  
									implements 	TCP_Response
{
	public Panel_5_Action()
	{
		super();
	}
    public Panel_5_Action(int menuLayout)
    {
		super(menuLayout);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.activity										= getActivity();
    	View							thisView			= inflater.inflate(R.layout.panel_5_actions_hotwater, container, false);
    	TCP_Task						task				= new TCP_Task();
    	task.callBack										= this;
//    	task.execute(new Ctrl_Actions_HotWater().new Request());

    	thisView.findViewById(R.id.buttonOk).setOnClickListener((OnClickListener) this);
        return thisView;
    }
	public void onClick(View myView) 
	{
    	Button 								myButton 			= (Button) myView;
    	String								myCaption			= myButton.getText().toString();
    	FragmentManager 					fManager			= getFragmentManager();
    	FragmentTransaction					fTransaction;
    	Fragment 							panelFragment;
    	
    	if (myCaption.equalsIgnoreCase("Ok"))
    	{
    		System.out.println("Action Hot Water Click");

 //   		Ctrl_Actions_HotWater.Execute	message_out			= new Ctrl_Actions_HotWater().new Execute();
 
			NumberPicker 					np 					= (NumberPicker) getActivity().findViewById(R.id.tempObjective);
//	   		message_out.tempObjective							= ((np.getValue() - 30) * 5 + 30) * 1000; // getValue returns an index
			
        	TCP_Task						task				= new TCP_Task();
        	task.callBack										= this;
//        	task.execute(message_out);
    	}

	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
	}
	public void processFinish(Ctrl_Abstract result) 
	{  
		Activity a							= getActivity();
		
		System.out.println("activity = " + a);
//		if (a == null) 
//		{
//			// Do nothing
//		}
//		else if (result instanceof Ctrl_Actions_HotWater.Data)
//		{
//			Ctrl_Actions_HotWater.Data msg_received 	= (Ctrl_Actions_HotWater.Data) result;
//			
//			if (msg_received.executionActive)
//			{
//				((TextView) a.findViewById(R.id.TimeStart)).setText("Current");
//				((TextView) a.findViewById(R.id.TargetTemp)).setText(((Integer) (msg_received.tempObjective/1000)).toString());
//			}
//			else if (msg_received.executionPlanned)
//			{
//				((TextView) a.findViewById(R.id.TimeStart)).setText(Global.displayTimeShort(msg_received.timeStart));
//				((TextView) a.findViewById(R.id.TargetTemp)).setText(((Integer) (msg_received.tempObjective/1000)).toString());
//			}
//			else
//			{
//				((TextView) a.findViewById(R.id.TimeStart)).setText("No Plan");
//				((TextView) a.findViewById(R.id.TargetTemp)).setText(" ");
//			}
//			
//			NumberPicker np = (NumberPicker) a.findViewById(R.id.tempObjective);
//		    String[] temps = new String[10];
//		    for(int i =0; i < temps.length; i++)
//		    {
//		    	temps[i] = Integer.toString(i*5 + 30);
//		    }
//
//		    np.setMinValue(30);
//		    np.setMaxValue(80);
//		    np.setWrapSelectorWheel(false);
//		    np.setDisplayedValues(temps);
//		    np.setValue(31);									// Min value + increment 5 = 35
//		}
//		else if (result instanceof Ctrl_Actions_HotWater.NoConnection)
//		{
//			Global.toast("No Connection established yet", true);
//		}
//		else if (result instanceof Ctrl_Actions_HotWater.Ack)
//		{
//			Global.toast("Command accepted", true);
//		}
//		else if (result instanceof Ctrl_Abstract.Ack)
//		{
//			Global.toast("Command accepted", true);
//		}
//		else
//		{
//			Global.toast("A Nack has been returned", true);
//		}		   
	}
}

