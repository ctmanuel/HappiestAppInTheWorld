package com.example;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Constants for the happiest app in the world!
 * 
 * TODO Could use resource... but this is more fun
 *
 */
public interface HappiestConstants {
	String EXTRA_MESSAGE = "com.example.helloworld.MESSAGE";
	int alarmId = 346; // Arbitrary but unique ID
	int mNotificationId = 001;
	String last_day_key = "last_day_a_compliment_was_sent";
	String APP_TAG = "HappiestAppInTheWorld";

	/**
	 * File name for shared preferences... not used yet
	 */
	String PREFS = "HappiestPreferences";

	String time_pref_KEY = "timePrefA_Key";

	// Time stuff
	SimpleDateFormat hr_24_fmt = new SimpleDateFormat("HH:mm", Locale.US);
	SimpleDateFormat hr_min_fmt = new SimpleDateFormat("HH:mm", Locale.US);
	SimpleDateFormat date_fmt = new SimpleDateFormat("dd", Locale.US);
}
