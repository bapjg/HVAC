package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.*;

public class CustomListAdapter extends ArrayAdapter
{
	 
    private ArrayList						listData;
    private LayoutInflater 					layoutInflater;
//    private Activity 						myActivity;
    private Context 						myContext;
 
    public CustomListAdapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
        
        System.out.println("CustomListAdapter construtor called");

        // this.myActivity					= activity;
        this.listData 					= listData;
        this.myContext 					= context;
        
        System.out.println("CustomListAdapter listData setup");
        
        layoutInflater 					= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater layoutInflater 	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        System.out.println("CustomListAdapter layoutInflater setup");
   }
 
    @Override
    public int getCount() 
    {
        System.out.println("getCount called");

        return listData.size();
    }
 
    @Override
    public NewsItem getItem(int position) 
    {
        System.out.println("getItem called");

        return (NewsItem) listData.get(position);
    }
 
    @Override
    public long getItemId(int position) 
    {
        System.out.println("getItemId called");

        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        System.out.println("getView called");
        
    	ViewHolder holder;
        if (convertView == null) 
        {
            convertView 				= layoutInflater.inflate(R.layout.list_row_layout, null);
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
}