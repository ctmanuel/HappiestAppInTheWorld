package com.example.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.HappiestConstants;
import com.example.notifications.NotificationPusher;
import com.example.settings.SettingsActivity;

public class MainActivity extends Activity {
	public static TextView countDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		countDown = (TextView) findViewById(R.id.textViewTime);
		countDown.setText("00:00:00");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {

		// Create a new intent that in turn will call the new activity to
		// display the message
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		/*
		 * An Intent can carry data types as key-value pairs called extras.The
		 * putExtra() method takes the key name in the first parameter and the
		 * value in the second
		 */
		intent.putExtra(HappiestConstants.EXTRA_MESSAGE, message);
		NotificationPusher.notify(this, message); // send notification
		startActivity(intent); // start the activity
	}

}
