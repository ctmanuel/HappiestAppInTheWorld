package com.example.alarms;

import java.util.Calendar;
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
import com.example.R;
import com.example.notifications.NotificationPusher;
import com.example.settings.TimePreference;

/**
 * A background service to push notifications based on preference timers.
 */
public class ComplimentService extends Service implements HappiestConstants {
	private Set<Integer> numSet = new TreeSet<Integer>();
	static Random rand = new Random();

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
		final PendingIntent pending = getAlarmIntent(c);
		final AlarmManager alarm = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pending);

		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(c);
		if (SP.getBoolean("Notifications", false)) {
			Calendar cal = Calendar.getInstance();

			// Sending today's first notification
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.setTimeZone(TimeZone.getDefault());

			String time = SP.getString(time_pref_KEY, "12:00");
			cal.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(time));
			cal.set(Calendar.MINUTE, TimePreference.getMinute(time));
			Log.i(APP_TAG, "Time of next alarm: " + time);

			alarm.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(),
					AlarmManager.INTERVAL_DAY, pending);
			Log.i(APP_TAG, "Started the timed Notifications service.");

			return true;
		} else {
			Log.i(APP_TAG, "Canceled the timed Notification service.");
		}
		return false;
	}

	/**
	 * Makes a pending intent for an alarm
	 * 
	 * @param c
	 * @return
	 */
	private static PendingIntent getAlarmIntent(Context c) {
		final Intent intent = new Intent(c, ComplimentService.class);
		intent.putExtra("alarmId", alarmId);
		// The FLAG_UPDATE_CURRENT flag and consistent ID should overwrite all
		// previous alarms set by this service.
		return PendingIntent.getService(c, alarmId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	/**
	 * Cancel alarms previously set
	 * 
	 * @param c
	 */
	public static void cancelAlarms(Context c) {
		final AlarmManager alarm = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(getAlarmIntent(c));
		Log.i(APP_TAG, "Canceled the timed Notification service.");
	}

	@Override
	public int onStartCommand(Intent i, int flags, int startId) {
		Log.d(APP_TAG, "Compliment service started...");
		Log.d(APP_TAG, "Num set size" + numSet.size());

		// TODO a better Intent filter could probably be made...
		if (i != null && i.getIntExtra("alarmId", alarmId - 1) == alarmId) {
			Calendar cal = Calendar.getInstance();
			SharedPreferences SP = PreferenceManager
					.getDefaultSharedPreferences(this);

			// Check to make sure the alarm is going off on time
			String time = hr_24_fmt.format(cal.getTime());
			String desired = SP.getString(time_pref_KEY, "12:00");
			int time_minutes = TimePreference.getMinutesInDay(time);
			int desired_minutes = TimePreference.getMinutesInDay(desired);

			// If the time of day in minutes is within 8 minutes, let the alarm
			// activate.
			// TODO Find a fix for the bug around midnight...
			if (Math.abs(time_minutes - desired_minutes) > 8) {
				Log.w(APP_TAG, "Wrong time for alarm!" + "\n    Desired: "
						+ desired + "\n     Actual: " + time);
				return START_STICKY;
			}

			// Check to make sure we haven't sent a notification today
			String today = date_fmt.format(cal.getTime());
			Log.d(APP_TAG, "Today's date: " + today);

			// Return early if an alarm already went off today
			if (today.equals(SP.getString(last_day_key, "not set yet..."))
					&& !SP.getBoolean("dev_override_daily", false)) {
				Log.w(APP_TAG, "Already sent a compliment today...");
				return START_STICKY;
			}

			// Save today as the last day that a compliment was sent
			SharedPreferences.Editor editor = SP.edit();
			editor.putString(last_day_key, today);
			editor.commit();

			int num_compliments = getResources().getStringArray(
					R.array.compliments_arr).length - 1;
			int rand = randInt(0, num_compliments);
			// if its in the set, re-random until not.
			while (numSet.contains(rand)) {
				rand = randInt(0, num_compliments);
			}
			numSet.add(rand);

			String msg = getResources().getStringArray(R.array.compliments_arr)[rand];
			NotificationPusher.notify(getBaseContext(), msg);
		} else {
			Log.d(APP_TAG, "Not sure why this got called...");
		}
		return START_STICKY;

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
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		return rand.nextInt((max - min) + 1) + min;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Figure out what onBind is for?
		return null;
	}

}