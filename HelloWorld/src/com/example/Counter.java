package com.example;

import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;

import com.example.helloworld.MainActivity;

@SuppressLint("DefaultLocale")
public class Counter extends CountDownTimer {
	

	public Counter(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		MainActivity.countDown.setText("ITS COMING");
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		long millis = millisUntilFinished;
		String hms = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
		// set timer in main activity
		MainActivity.countDown.setText(hms);
	}

}
