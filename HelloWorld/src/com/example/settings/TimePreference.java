package com.example.settings;

import java.util.Calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.example.Counter;

/**
 * This class is used to manage the time picker in the settings page
 * 
 * 
 */
public class TimePreference extends DialogPreference {
	private static int lastHour = 0; // previous hour
	private static int lastMinute = 0; // previous minute
	private TimePicker picker = null; // global time picker
	private static Counter counter;

	/**
	 * Returns the hour in the given time string
	 * 
	 * @param time
	 *            The current time
	 * @return Hour of the time given
	 */
	public static int getHour(String time) {
		String[] pieces = time.split(":");
		return (Integer.parseInt(pieces[0]));
	}

	/**
	 * Returns the minute in the given time string
	 * 
	 * @param time
	 *            The current time
	 * @return Minute of the time given
	 */
	public static int getMinute(String time) {
		String[] pieces = time.split(":");

		return (Integer.parseInt(pieces[1]));
	}

	/**
	 * Invokes super classes constructor then adds custom implementation
	 * 
	 * @param ctxt
	 *            Current Context
	 * @param attrs
	 *            Current Attribute Set
	 */
	public TimePreference(Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);
		// add buttons
		setPositiveButtonText("Set");
		setNegativeButtonText("Cancel");
	}

	/**
	 * Calculates the remaining time until the next notification
	 * 
	 * @return
	 */
	public static String getRemainingTime() {
		return null;
	}

	@Override
	protected View onCreateDialogView() {
		picker = new TimePicker(getContext()); // new picker view

		return (picker);
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);

		// set current time
		picker.setCurrentHour(lastHour);
		picker.setCurrentMinute(lastMinute);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		// if pressed set button, set time
		if (positiveResult) {
			lastHour = picker.getCurrentHour();
			lastMinute = picker.getCurrentMinute();

			String time = String.valueOf(lastHour) + ":"
					+ String.valueOf(lastMinute);

			if (callChangeListener(time)) {
				persistString(time);
			}
			// calculate time to next alarm
			int TotalTime = (((lastMinute / 60) + lastHour) * 3600000)
					- getCurrentTime();
			counter = new Counter(Math.abs(TotalTime), 1000);
			counter.start();
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return (a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		String time = null;

		if (restoreValue) {
			if (defaultValue == null) {
				time = getPersistedString("00:00");
			} else {
				time = getPersistedString(defaultValue.toString());
			}
		} else {
			time = defaultValue.toString();
		}

		lastHour = getHour(time);
		lastMinute = getMinute(time);
	}

	/**
	 * Gets the current time from a new calendar object
	 * 
	 * @return The current hour, minute, and second in milliseconds
	 */
	private static int getCurrentTime() {

		Calendar c = Calendar.getInstance();
		int secondsToMilli = c.get(Calendar.SECOND) * 1000; // 1000 milliseconds
															// in second
		int minutesToMilli = c.get(Calendar.MINUTE) * 60000; // 60000
																// milliseconds
																// in minute
		int hoursToMilli = (c.get(Calendar.HOUR) + 12) * 3600000; // add 12 cuz
																	// hour only
																	// goes from
																	// 1-12;
																	// 3600000
																	// milli in
																	// hr
		return secondsToMilli + minutesToMilli + hoursToMilli; // total time in
																// milliseconds
	}
}