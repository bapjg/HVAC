package com.bapjg.hvac_client;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class Menu_0_Fragment extends Fragment // implements View.OnClickListener
{
	private Fragment			panelFragment;
	private int					menuLayout;
	
	public Menu_0_Fragment(Fragment panelFragment, int menuLayout)
	{
		// fragment : object which maintains the panel fragment			eg : panelTemperatures = new Panel_1_Temperatures();
		// layout   : id of the layout file								eg : R.layout.menu_1_temperatures
		
		super();
		this.panelFragment								= panelFragment;
		this.menuLayout									= menuLayout;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	// Inflate the menuLayout into container (menu_container)
    	View result = inflater.inflate(this.menuLayout, container, false);
    	// The menuLayout contains only buttons. Set the OnClickListener to the panelFragment 
		for (int i = 0; i < ((ViewGroup) result).getChildCount(); i++)
		{
			Button				buttonChild 			= (Button) ((ViewGroup) result).getChildAt(i);
			buttonChild.setOnClickListener((OnClickListener) this.panelFragment);
		}
		((OnClickListener) this.panelFragment).onClick((Button) ((ViewGroup) result).getChildAt(0));
    	return result;
    }
}
