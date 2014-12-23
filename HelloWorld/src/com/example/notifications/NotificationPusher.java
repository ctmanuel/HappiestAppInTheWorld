package com.example.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.example.helloworld.R;

/**
 * Since this app really one has one function, here we have a centralized usage
 * of the notification system.
 * 
 *
 */
public class NotificationPusher {

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
					.setPriority(NotificationCompat.PRIORITY_MIN);

			// TODO for NotificationCompat
			// not sure why this didn't work .setVibrate(vibrate_pattern);
			// Use ".setNumber" to note the number of stacked notifications
			// For android 5.0, use .setCategory for category stuff

			// Sets an ID for the notification - so that we update previously
			// sent
			// notifications
			int mNotificationId = 001;

			// Gets an instance of the NotificationManager service
			NotificationManager mNotifyMgr = (NotificationManager) c
					.getSystemService(Context.NOTIFICATION_SERVICE);

			// Builds the notification and issues it.
			mNotifyMgr.notify(mNotificationId, mBuilder.build());
			return true;
		}
		return false;
	}

	// TODO:
	// A timer based notification system? do it daily?
}
