package com.bapjg.hvac_client;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Fragment_Configuration extends Fragment 
{
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuration, container, false);
    }
    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
        AbsListView 				layout_list							= (AbsListView) view.findViewById(R.id.List_View);
        LinearLayout.LayoutParams	params_list							= (LinearLayout.LayoutParams) layout_list.getLayoutParams();
        params_list.height												= 0;
        layout_list.setLayoutParams(params_list);
        
        LinearLayout 				layout_item							= (LinearLayout) view.findViewById(R.id.Item_View);
        LinearLayout.LayoutParams	params_item							= (LinearLayout.LayoutParams) layout_item.getLayoutParams();
        params_item.height												= 100;
        layout_item.setLayoutParams(params_item);
	}}

