package com.example.notifications;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.helloworld.DisplayMessageActivity;
import com.example.helloworld.MainActivity;
import com.example.helloworld.R;

/**
 * Since this app really one has one function, here we have a centralized usage
 * of the notification system.
 * 
 * 
 */
public class NotificationPusher extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.helloworld.MESSAGE";
	private final static int mNotificationId = 001;

	/**
	 * If notifications are enabled, the given message is pushed as a
	 * notification
	 * 
	 * @param c
	 *            The context to send from
	 * @param message
	 *            The message to send
	 * @return true if notification is pushed, false otherwise
	 */
	public static boolean notify(Context c, String message) {
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(c);

		if (SP.getBoolean("Notifications", false)) {

			// Testing out proof of concept for notifications
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					c).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Happiest App In the World Notification!")
					.setContentText(message)
					.setPriority(NotificationCompat.PRIORITY_HIGH);

			// TODO for NotificationCompat
			// not sure why this didn't work .setVibrate(vibrate_pattern);
			// Use ".setNumber" to note the number of stacked notifications
			// For android 5.0, use .setCategory for category stuff

			// Add intents to open activity on click of notification
			Intent intent = new Intent(c, DisplayMessageActivity.class);
			intent.putExtra(EXTRA_MESSAGE, message);

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
			stackBuilder.addParentStack(MainActivity.class);
			stackBuilder.addNextIntent(intent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);

			mBuilder.setContentIntent(resultPendingIntent);

			// Gets an instance of the NotificationManager service
			NotificationManager mNotifyMgr = (NotificationManager) c
					.getSystemService(Context.NOTIFICATION_SERVICE);

			// Builds the notification and issues it.
			mNotifyMgr.notify(mNotificationId, mBuilder.build());
			Log.d("HappiestAppInTheWorld", "Notification sent: '" + message
					+ "'");
			return true;
		}
		return false;
	}

}
