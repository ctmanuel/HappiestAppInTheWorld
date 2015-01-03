package com.example.alarms;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Receives notifications that the device has recently booted, the restarts the
 * alarm service because those get canceled when a device is turned off.
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Initialize the compliment alarm on boot
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			ComplimentService.initialize(context);
		}
	}

	// TODO make a preference state change listener to enable/disable this when
	// the notifications are turned on and off in the preferences menu

	/**
	 * Programmatically enable the BootReciever so it can run on bootup of the
	 * device
	 * 
	 * @param c
	 *            The current context
	 */
	static void enableBootReceiver(Context c) {
		setBootReceiverState(c, PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
	}

	/**
	 * Programmatically disable the BootReciever so it can't run on bootup of
	 * the device
	 * 
	 * @param c
	 *            The current context
	 */
	static void disableBootReceiver(Context c) {
		setBootReceiverState(c, PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
	}

	/**
	 * Helper function for setting the state of the BootReceiver
	 * 
	 * @param c
	 *            Current context
	 * @param state
	 *            State to set it to
	 */
	private static void setBootReceiverState(Context c, int state) {
		ComponentName receiver = new ComponentName(c, BootReceiver.class);
		PackageManager pm = c.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}

}
