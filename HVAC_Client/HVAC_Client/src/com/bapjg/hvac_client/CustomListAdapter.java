package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.*;

public class CustomListAdapter extends ArrayAdapter
{
	 
    private ArrayList						listData;
    private LayoutInflater 					myInflater;
    private Context 						myContext;
 
    public CustomListAdapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
        
        this.listData 					= listData;
        this.myContext 					= context;
        this.myInflater					= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() 
    {
        return listData.size();
    }
    @Override
    public NewsItem getItem(int position) 
    {
        return (NewsItem) listData.get(position);
    }
    @Override
    public long getItemId(int position) 
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	ViewHolder holder;
    	
        if (convertView == null) 
        {
            
        	convertView 				= myInflater.inflate(R.layout.list_row_layout, null);
            holder 						= new ViewHolder();
            holder.headlineView 		= (TextView) convertView.findViewById(R.id.title);
            holder.reporterNameView 	= (TextView) convertView.findViewById(R.id.reporter);
            holder.reportedDateView 	= (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } 
        else 
        {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.headlineView.setText(((NewsItem) listData.get(position)).getHeadline());
        holder.reporterNameView.setText("By, " + ((NewsItem) listData.get(position)).getReporterName());
        holder.reportedDateView.setText(((NewsItem) listData.get(position)).getDate());
 
        return convertView;
    }
    static class ViewHolder 
    {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
    }
    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
    {
        // Object 		o 								= view.getItemAtPosition(position);
        // NewsItem 	newsData 						= (NewsItem) o;
		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
    }
}