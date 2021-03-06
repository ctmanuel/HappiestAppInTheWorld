package com.example.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.HappiestConstants;

public class DisplayMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_display_message);
		// Show the Up button in the action bar.
		Intent intent = getIntent(); // get the intent
		String message = intent.getStringExtra(HappiestConstants.EXTRA_MESSAGE);
		// get the message using unique global variable id
		TextView textView = new TextView(this); // create new text view to
												// display message
		textView.setTextSize(40); // set text size (dur)
		textView.setText(message); // set text
		// add the TextView as the root view of the activity�s layout by passing
		// it to setContentView().
		setContentView(textView);

	}

	/*
	 * @Override DONT REALLY NEED THIS FOR NOW public boolean
	 * onCreateOptionsMenu(Menu menu) { // Inflate the menu; this adds items to
	 * the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.display_message, menu); return true; }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
