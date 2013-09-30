package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Configuration_ThrowAway extends FragmentActivity 
{
	private Adapter_Thermometers 		adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
 
        Mgmt_Msg_Configuration.Data		message_in					= getMessage_In();
        ArrayList 						data		 				= message_in.thermometerList;
        AdapterView 					view						= (AdapterView) findViewById(R.id.List_View);
        
        Adapter_Thermometers 			adapter						= new Adapter_Thermometers(this, R.id.List_View, data);
        
        view.setAdapter(adapter);
        
        
        view.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{	@Override	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) 
			{
				onClick(arg0, view, position, arg3);
			}
	    });		
	}
	public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
		Toast.makeText(Activity_Configuration_ThrowAway.this, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
        AbsListView 				layout_list							= (AbsListView) findViewById(R.id.List_View);
        LinearLayout.LayoutParams	params_list							= (LinearLayout.LayoutParams) layout_list.getLayoutParams();
        params_list.height												= 0;
        layout_list.setLayoutParams(params_list);
        
        LinearLayout 				layout_item							= (LinearLayout) findViewById(R.id.Item_View);
        LinearLayout.LayoutParams	params_item							= (LinearLayout.LayoutParams) layout_item.getLayoutParams();
        params_item.height												= 100;
        layout_item.setLayoutParams(params_item);
	}
    public         Mgmt_Msg_Configuration.Data		getMessage_In()
    {
    	Mgmt_Msg_Configuration x = new Mgmt_Msg_Configuration();
    	
    	return x.new Data();
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	protected View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflator.inflate(R.layout.fragment_list, container, false);
		setContentView(R.layout.activity_configuration);
		
		// TextView field	 						= (TextView) findViewById(R.id.Data);
		// field.setText(Global.henry);
		return view;
	}
}
