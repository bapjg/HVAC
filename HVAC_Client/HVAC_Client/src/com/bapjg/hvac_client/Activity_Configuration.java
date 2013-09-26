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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Configuration extends FragmentActivity 
{
	private CustomListAdapter 		adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
 
        ArrayList 					data		 					= getListData();
        AdapterView 				view							= (AdapterView) findViewById(R.id.List_View);
        
        CustomListAdapter 			adapter							= new CustomListAdapter(this, R.id.List_View, data);
        
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
		Toast.makeText(Activity_Configuration.this, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();		
	}
    private ArrayList getListData() 
    {
        ArrayList results = new ArrayList();
        NewsItem newsData = new NewsItem();
        newsData.setHeadline("Dance of Democracy");
        newsData.setReporterName("Pankaj Gupta");
        newsData.setDate("May 26, 2013, 13:35");
        results.add(newsData);
 
        newsData = new NewsItem();
        newsData.setHeadline("Major Naxal attacks in the past");
        newsData.setReporterName("Pankaj Gupta");
        newsData.setDate("May 26, 2013, 13:35");
        results.add(newsData);
 
        newsData = new NewsItem();
        newsData.setHeadline("BCCI suspends Gurunath pending inquiry ");
        newsData.setReporterName("Rajiv Chandan");
        newsData.setDate("May 26, 2013, 13:35");
        results.add(newsData);
 
        newsData = new NewsItem();
        newsData.setHeadline("Life convict can`t claim freedom after 14 yrs: SC");
        newsData.setReporterName("Pankaj Gupta");
        newsData.setDate("May 26, 2013, 13:35");
        results.add(newsData);
 
        newsData = new NewsItem();
        newsData.setHeadline("Indian Army refuses to share info on soldiers mutilated at LoC");
        newsData.setReporterName("Pankaj Gupta");
        newsData.setDate("May 26, 2013, 13:35");
        results.add(newsData);
 
        newsData = new NewsItem();
        newsData.setHeadline("French soldier stabbed; link to Woolwich attack being probed");
        newsData.setReporterName("Sudeep Nanda");
        newsData.setDate("May 26, 2013, 13:35");
        results.add(newsData);
 
        return results;
    }	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	protected View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflator.inflate(R.layout.fragment_list, container, false);
		setContentView(R.layout.activity_configuration);
		
		// TextView field	 						= (TextView) findViewById(R.id.Data);
		// field.setText(Global.henry);
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuration, menu);
		return true;
	}

}
