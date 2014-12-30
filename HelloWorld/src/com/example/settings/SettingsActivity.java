package com.example.settings;



import android.app.Activity;
import android.os.Bundle;


public class SettingsActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
		
		/*timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
		time = (TextView) findViewById(R.id.ShowTime);
		calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		showTime(hour,min);*/
	}

}