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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
//Template										variable			= something
//Template										ext/imp				class
public class Panel_6_Actions_Test_Mail 			extends 			Panel_0_Fragment  
												implements 			TCP_Response
{
	public Panel_6_Actions_Test_Mail()
	{
		super();
	}
    public Panel_6_Actions_Test_Mail(int menuLayout)
    {
		super(menuLayout);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.activity												= getActivity();
    	View									thisView			= inflater.inflate(R.layout.panel_6_actions_test_mail, container, false);
 
    	thisView.findViewById(R.id.buttonSendMail).setOnClickListener(new View.OnClickListener() 	{@Override public void onClick(View v) {sendMail(v);	}});
   	
        return thisView;
    }
    public void sendMail(View v)
    {
    	TCP_Send(new Ctrl_Actions_Test_Mail().new Execute());
    }
    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
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

		if (result instanceof Ctrl_Actions_Test_Mail.Ack)
		{
			Global.toaster("eMail sent", true);
		}
		else
		{
			Global.toaster("eMail received " + result.getClass().toString(), true);
		}
	}
}

