package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.Ctrl_Configuration;
import HVAC_Common.Ctrl_Thermo_List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Panel_5_Configuration_Thermo_List_Adapter 			extends 					Panel_0_Adapter
{
    public Panel_5_Configuration_Thermo_List_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	Ctrl_Thermo_List.Thermo									listItem					= (Ctrl_Thermo_List.Thermo) listData.get(position);
        
    	Element_Standard										adapterElement 					= new Element_Standard(listItem.name);
    	adapterElement.setValue(listItem.name);

        if (Global.deviceName == "lgPhone")
        {
        	((LinearLayout) adapterElement.findViewById(R.id.element)).setOrientation(LinearLayout.VERTICAL);
        }
        return adapterElement;
    }
}