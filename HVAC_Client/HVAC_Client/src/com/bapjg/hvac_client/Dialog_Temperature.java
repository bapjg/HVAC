package com.bapjg.hvac_client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.*;
import android.widget.NumberPicker;
import android.widget.TextView;

public class Dialog_Temperature extends DialogFragment 
{
	public NumberPicker 			np;
	public Dialog_Response			callBack;
	public Integer	tempMin;
	public Integer  step;
	public Integer  steps;
	public Integer  tempInitial;
	public TextView	writeBack;
	
	
	public Dialog_Temperature() 
    {
    }
	public interface OnTemperatureSelectedListener 
    {
        public void onTemperatureSelected(Integer temperature);
    }

    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 	builder 						= new AlertDialog.Builder(getActivity());
        LayoutInflater 			inflater 						= getActivity().getLayoutInflater();
        
        View					dialogView						= inflater.inflate(R.layout.dialog_temperature, null);
        builder.setView(dialogView);

        
        builder.setTitle("Select temperature");
        
        
		np 														= (NumberPicker) dialogView.findViewById(R.id.tempObjective);
	    String[] 				temps 							= new String[steps + 1];
	    for(int i = 0; i < steps + 1; i++)
	    {
	    	temps[i] 											= Integer.toString(i*step + tempMin);
	    }

	    np.setMinValue(tempMin);
	    np.setMaxValue(tempMin + steps);
	    np.setWrapSelectorWheel(false);
	    np.setDisplayedValues(temps);
	    np.setValue(tempMin + (tempInitial - tempMin)/step);		// Min + index
       
//        builder.setMessage("Are you sure?");
        //null should be your on click listener
        
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) 
            {
             	Integer temperature =(np.getValue() - tempMin) * step + tempMin;
//            	callBack.onTemperatureChange((np.getValue() - tempMin) * step + tempMin);
             	writeBack.setText(temperature.toString());
            	dialog.dismiss();
            }
        }
        );
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
        {
           @Override
            public void onClick(DialogInterface dialog, int which) 
            {
                dialog.dismiss();
            }
        }
        );
        return builder.create();
    }
}
