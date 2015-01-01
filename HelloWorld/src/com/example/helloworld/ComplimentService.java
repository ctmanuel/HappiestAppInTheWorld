package com.example.helloworld;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.Set;
import java.util.TreeSet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.notifications.NotificationPusher;

/**
 * A background service to push notifications based on preference timers.
 */
public class ComplimentService extends Service {
	private static String [] listOfCompliments;
	private Set<Integer> numSet = new TreeSet<Integer>();
	/**
	 * Initializes the compliment service using the times specified in the
	 * settings. This should be called each time the "preferences" are changed.
	 * 
	 * If the "Notifications" setting is not set, this will simply cancel the
	 * background service.
	 * 
	 * @param c The current context
	 * 
	 * @param compliments The list of compliments generated in the MainActivity class
	 * 
	 * @return true if service is started, false otherwise
	 */
	static boolean initialize(Context c, String[] compliments) {
		final Intent intent = new Intent(c, ComplimentService.class);
		listOfCompliments = compliments;
		final PendingIntent pending = PendingIntent.getService(c, 0, intent, 0);
		final AlarmManager alarm = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pending);

		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(c);
		if (SP.getBoolean("Notifications", false)) {
			// On a repeating timer
			// long interval = 1000;// milliseconds
			// alarm.setInexactRepeating(AlarmManager.RTC,
			// SystemClock.elapsedRealtime(), interval, pending);

			// Set a daily alarm to the given time from preferences
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.setTimeZone(TimeZone.getDefault());
			try { // TODO: Enforce a valid input for notification_hr preference.
					// The user can set the preference to blank. Handled with a
					// hack to catch the exception....
				calendar.set(Calendar.HOUR_OF_DAY,
						Integer.parseInt(SP.getString("notification_hr", "12")));
				calendar.set(Calendar.MINUTE, Integer.parseInt(SP.getString(
						"notification_min", "12")));
			} catch (NumberFormatException nfe) {
				calendar.set(Calendar.HOUR_OF_DAY, 12);
				calendar.set(Calendar.MINUTE, 12);
				Log.w("HappiestAppInTheWorld",
						"Invalid preference values for notification hour or minutes.");
			}
			alarm.setInexactRepeating(AlarmManager.RTC,
					calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
					pending);
			Log.i("HappiestAppInTheWorld",
					"Started the timed Notifications service.");

			return true;
		} else {
			Log.i("HappiestAppInTheWorld",
					"Canceled the timed Notification service.");
		}
		return false;	
	}

	@Override
	public void onStart(Intent i, int startId) {
		//Calendar cal = new GregorianCalendar()
		int rand = randInt(0,listOfCompliments.length-1); //seed used to pull compliment from string array
		
		//if its in the set, re-random until not. else add to set
		if(numSet.contains(rand)){
			while(numSet.contains(rand)){
				rand = randInt(0,listOfCompliments.length-1);
			}
		}
		else{
			numSet.add(rand);
		}
		
		String message = listOfCompliments[rand];
		NotificationPusher.notify(getBaseContext(), message);
		Log.d("HappiestAppInTheWorld", "Notification service called.");
		Log.d("HappiestAppInTheWorld", message);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Currently has no binding functionality.
		return null;
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

}