package com.bapjg.hvac_client;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

import HVAC_Messages.*;

public class Adapter_0_Abstract 							extends 					ArrayAdapter
{
    public ArrayList										listData;
    public LayoutInflater 									inflater;
 
    public Adapter_0_Abstract(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
        
        this.listData 																= listData;
        this.inflater																= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() 
    {
        return listData.size();
    }
    public Ctrl_Abstract getItem(int position) 
    {
        return (Ctrl_Abstract) listData.get(position);
    }
    public long getItemId(int position) 
    {
        return position;
    }
 }