package com.example.helloworld;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
	
	/**
	 * Initializes the compliment service using the times specified in the
	 * settings. This should be called each time the "preferences" are changed.
	 * 
	 * If the "Notifications" setting is not set, this will simply cancel the
	 * background service.
	 * 
	 * @return true if service is started, false otherwise
	 */
	static boolean initialize(Context c) {
		final Intent intent = new Intent(c, ComplimentService.class);
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
		Calendar cal = new GregorianCalendar();

		// TODO: Make this message be a compliment!
		String message = "Poop @ " + cal.get(Calendar.SECOND);
		NotificationPusher.notify(getBaseContext(), message);
		Log.d("HappiestAppInTheWorld", "Notification service called.");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Currently has no binding functionality.
		return null;
	}

}