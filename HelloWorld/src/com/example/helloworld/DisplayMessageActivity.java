package com.example.helloworld;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_display_message);
		// Show the Up button in the action bar.
		Intent intent = getIntent(); // get the intent
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE); // get
																			// the
																			// message
																			// using
																			// unique
																			// global
																			// variable
																			// id
		TextView textView = new TextView(this); // create new text view to
												// display message
		textView.setTextSize(40); // set text size (dur)
		textView.setText(message); // set text
		// add the TextView as the root view of the activity�s layout by passing
		// it to setContentView().
		setContentView(textView);

		// Testing out proof of concept for notifications
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Happiest App In the World Notification!")
				.setContentText(message); // use ".setNumber" to note the number
											// of stacked notifications

		// Sets an ID for the notification - so that we update previously sent
		// notifications
		int mNotificationId = 001;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
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