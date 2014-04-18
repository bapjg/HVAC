package com.bapjg.hvac_client;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import HVAC_Messages.*;


public class Detail_Thermometer_NotUsed_xxxxxxxxxxxxx extends Fragment implements View.OnClickListener
{
	public Ctrl_Configuration.Thermometer me;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
		View myView =  inflater.inflate(R.layout.detail_thermometer, container, false);
		
       	((EditText) myView.findViewById(R.id.name)).setText(me.name);
//		((EditText) myView.findViewById(R.id.friendlyName)).setText(me.friendlyName);
		((EditText) myView.findViewById(R.id.address)).setText(me.address);

        return myView;
    }

	@Override
	public void onClick(View v) 
	{
		System.out.println(((Button) v).getText()); //Ok or Cancel
	}
}
