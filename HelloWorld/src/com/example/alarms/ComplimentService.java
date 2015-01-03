package com.example.alarms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
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

import com.example.HappiestConstants;
import com.example.helloworld.R;
import com.example.notifications.NotificationPusher;
import com.example.settings.TimePreference;

/**
 * A background service to push notifications based on preference timers.
 */
public class ComplimentService extends Service implements HappiestConstants {
	private Set<Integer> numSet = new TreeSet<Integer>();

	/**
	 * Initializes the compliment service using the times specified in the
	 * settings. This should be called each time the "preferences" are changed.
	 * 
	 * If the "Notifications" setting is not set, this will simply cancel the
	 * background service.
	 * 
	 * @param c
	 *            The current context
	 * 
	 * @param compliments
	 *            The list of compliments generated in the MainActivity class
	 * 
	 * @return true if service is started, false otherwise
	 */
	public static boolean initialize(Context c) {
		final Intent intent = new Intent(c, ComplimentService.class);
		// The FLAG_UPDATE_CURRENT flag and consistent ID should overwrite all
		// previous alarms set by this service.
		final PendingIntent pending = PendingIntent.getService(c,
				HappiestConstants.alarmId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		final AlarmManager alarm = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pending);

		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(c);
		if (SP.getBoolean("Notifications", false)) {
			Calendar calendar = Calendar.getInstance();

			// Sending today's first notification
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.setTimeZone(TimeZone.getDefault());
			try { // TODO: Enforce a valid input for notification_hr preference.
					// The user can set the preference to blank. Handled with a
					// hack to catch the exception....
				String time = SP.getString("timePrefA_Key", "12:00");
				calendar.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(time));
				calendar.set(Calendar.MINUTE, TimePreference.getMinute(time));
				Log.d(APP_TAG, "Time of next alarm: " + time);
			} catch (NumberFormatException nfe) {
				calendar.set(Calendar.HOUR_OF_DAY, 12);
				calendar.set(Calendar.MINUTE, 12);
				Log.w(APP_TAG,
						"Invalid preference values for notification hour or minutes.");
			}
			alarm.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
					AlarmManager.INTERVAL_DAY, pending);
			Log.i(APP_TAG, "Started the timed Notifications service.");

			return true;
		} else {
			Log.i(APP_TAG, "Canceled the timed Notification service.");
		}
		return false;
	}

	@Override
	public void onStart(Intent i, int startId) {
		// Check to make sure we haven't sent a notification today
		Calendar calendar = Calendar.getInstance();
		SharedPreferences SP = PreferenceManager
				.getDefaultSharedPreferences(this);
		String today = new SimpleDateFormat("dd", Locale.US).format(calendar
				.getTime());
		Log.d(APP_TAG, "Today's date: " + today);

		// Return early if an alarm already went off today
		if (today.equals(SP.getString(HappiestConstants.last_day,
				"not set yet..."))) {
			Log.w(APP_TAG, "Already sent a compliment today...");
			return;
		}

		// Save today as the last day that a compliment was sent
		SharedPreferences.Editor editor = SP.edit();
		editor.putString(HappiestConstants.last_day, today);
		editor.commit();

		int rand = randInt(
				0,
				getResources().getStringArray(R.array.compliments_arr).length - 1);

		// if its in the set, re-random until not. else add to set
		if (numSet.contains(rand)) {
			while (numSet.contains(rand)) {
				rand = randInt(
						0,
						getResources().getStringArray(R.array.compliments_arr).length - 1);
			}
		}

		numSet.add(rand);

		String message = getResources().getStringArray(R.array.compliments_arr)[rand];
		NotificationPusher.notify(getBaseContext(), message);
		Log.d(APP_TAG, "Notification service called: " + message);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Currently has no binding functionality.
		return null;
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 * 
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
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