package com.example.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.HappiestConstants;
import com.example.alarms.BootReceiver;
import com.example.alarms.ComplimentService;

public class SettingsActivity extends Activity implements
		SharedPreferences.OnSharedPreferenceChangeListener, HappiestConstants {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences SP, String key) {

		// Enable notifications if set to true, cancel otherwise
		if (key.equals("Notifications")) {
			if (SP.getBoolean("Notifications", false)) {
				BootReceiver.enableBootReceiver(this);
				ComplimentService.initialize(this);
			} else {
				BootReceiver.disableBootReceiver(this);
				ComplimentService.cancelAlarms(this);
			}
		}

		// Update the alarms if the desired time changed and notifications are
		// enabled
		if (key.equals(time_pref_KEY) && SP.getBoolean("Notifications", false)) {
			ComplimentService.initialize(this);
		}
	}
}
