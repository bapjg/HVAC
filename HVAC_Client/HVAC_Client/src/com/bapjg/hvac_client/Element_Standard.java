package com.bapjg.hvac_client;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_Standard 									extends 					LinearLayout
																implements 					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public TextView 											textTopic;
	public TextView 											textValue;
	public String												units;
	public Element_Interface									listener;
	
	public Element_Standard(String labelTextLeft) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		this.units 																			= "";
		inflater.inflate(R.layout.element_standard, this, true);
		textTopic 																			= (TextView) this.findViewById(R.id.textTopic);
		textValue 																			= (TextView) this.findViewById(R.id.textValue);
		textTopic.setText(labelTextLeft);
	}
	public Element_Standard(String labelTextLeft, String units) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		this.units 																			= " " + units.trim();
		inflater.inflate(R.layout.element_standard, this, true);
		textTopic 																			= (TextView) this.findViewById(R.id.textTopic);
		textValue 																			= (TextView) this.findViewById(R.id.textValue);
		textTopic.setText(labelTextLeft);
	}
	public void setTopic(String text)
	{
		textTopic.setText(text);
	}
	public void setValue(String text)
	{
		if (text != null)		textValue.setText(text);
		else					textValue.setText("");
	}
	public void setValue(Integer number)
	{
		if (number != null)		textValue.setText(number + units);
		else					textValue.setText(0 + units);
	}
	public void setValue(Long number)
	{
		if (number != null)		textValue.setText(number + units);
		else					textValue.setText(0 + units);
	}
	public void setValue(Float number)
	{
		if (number != null)		textValue.setText(number + units);
		else					textValue.setText(0 + units);
	}
	public void setValue(Cmn_Time time)
	{
		if (time != null)		textValue.setText(time.displayShort());
		else					textValue.setText("");
	}
	public void setValue(Cmn_Temperature temperature)
	{
		if (temperature != null)	textValue.setText(temperature.displayDecimal() + units);
		else						textValue.setText("0" + units);
	}
	public void centerColumns()
	{
		textTopic.setGravity(Gravity.CENTER);
		textValue.setGravity(Gravity.CENTER);
	}
	public void setListener(Element_Interface listener)
	{
		this.listener																		= listener;
		textValue.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) this);
	}
//	ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
//	if (viewTreeObserver.isAlive()) {
//	  viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//	    @Override
//	    public void onGlobalLayout() {
//	      view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//	      viewWidth = view.getWidth();
//	      viewHeight = view.getHeight();
//	    }
//	  });
//	}
}

