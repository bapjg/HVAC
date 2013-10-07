package com.bapjg.hvac_client;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Choices_Temperatures extends Fragment 
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
    	View result = inflater.inflate(R.layout.menu_temperatures, container, false);
    	    	
        return result;
    }
}

