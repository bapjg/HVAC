package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.activity_main);
		 
		        ArrayList image_details = getListData();
		        ListView lv1 = (ListView) findViewById(R.id.List_View);
		        
		        CustomListAdapter adapter	= new CustomListAdapter(this, R.id.List_View, image_details);
		        lv1.setAdapter(new CustomListAdapter(this, R.id.List_View, image_details));

//		        lv1.setOnItemClickListener
//		        (new OnItemClickListener() 
//		        {
//		 
//		            @Override
//		            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//		                Object o = lv1.getItemAtPosition(position);
//		                NewsItem newsData = (NewsItem) o;
//		                Toast.makeText(Activity_Configuration.this, "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
//		            }
//		 
//		        }
//		        );		
		
		
		
		
		
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_configuration);
		
//		TextView field	 											= (TextView) findViewById(R.id.Data);
//		field.setText(Global.configuration.thermometerList.get(0).thermoID);
//
//		field	 													= (TextView) findViewById(R.id.Datum);
//		field.setText(Global.configuration.thermometerList.get(1).thermoID);

		//ArrayAdapter adapter										= new ArrayAdapter <Mgmt_Msg_Configuration.Mgmt_Msg_Thermometer>(this, R.id.List_View, Global.configuration.thermometerList);
		//ListView list = (ListView) findViewById(R.id.List_View);
		//list.setAdapter(adapter);
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
