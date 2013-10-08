package com.bapjg.hvac_client;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;


public class Detail_Thermometers extends Fragment implements View.OnClickListener
{
	public Mgmt_Msg_Configuration.Thermometer me;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
		View myView =  inflater.inflate(R.layout.detail_thermometer, container, false);
		
       	((EditText) myView.findViewById(R.id.name)).setText(me.name);
		((EditText) myView.findViewById(R.id.friendlyName)).setText(me.friendlyName);
		((EditText) myView.findViewById(R.id.thermoID)).setText(me.thermoID);

		((Button)   myView.findViewById(R.id.Ok)).setOnClickListener(this);
		((Button)   myView.findViewById(R.id.Cancel)).setOnClickListener(this);
        return myView;
    }

	@Override
	public void onClick(View v) 
	{
		System.out.println(((Button) v).getText()); //Ok or Cancel
	}
}
