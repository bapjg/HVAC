package com.bapjg.hvac_client;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		System.out.println("step 1");
		HTTP_Request							httpRequest			= new HTTP_Request();
		
		Management_Calendar_Request_Data		messageSend 		= new Management_Calendar_Request_Data();

		httpRequest.execute(messageSend);
		
//		if (messageReceive instanceof Message_Calendar_Report)
//		{
//			Message_Calendar_Report				messageCalendar		= (Message_Calendar_Report) messageReceive;
//			System.out.println("step 5");
//			TextView							left1				= null;
//			TextView							left2				= null;
//			left1 													= (TextView) findViewById( R.id.left1 );
//			left1.setText(messageCalendar.dateTime);
//			left2 													= (TextView) findViewById( R.id.left2 );
//			left2.setText(messageCalendar.calendars);
//			System.out.println("step 6");
//			View relativeLayout =  findViewById(R.id.info);
//		    //LinearLayout layout = (LinearLayout) findViewById(R.id.info);
//			System.out.println("step 7");
//
//		    TextView valueTV = new TextView(this);
//		    valueTV.setText("hallo hallo");
//		    valueTV.setId(5);
//		    valueTV.setLayoutParams(new LayoutParams(
//		            LayoutParams.FILL_PARENT,
//		            LayoutParams.WRAP_CONTENT));
//
//			((RelativeLayout) relativeLayout).addView(valueTV);
//		}
//		else
//		{
//			System.out.println("step Not 5");
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
