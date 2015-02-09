package org.azurespot.practiceapp.calendar;


import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.azurespot.practiceapp.R;

public class CalendarShow extends Activity {
	
	public static EditText dateEdit;
	public EditText title;
	public String stringTitle;
	public android.widget.AnalogClock analogClock;
	public static final int REQUEST_CODE = 192837;
	Button reminderButton;
	Context context;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_show);
		
		title = (EditText)findViewById(R.id.title);
		reminderButton = (Button) findViewById(R.id.button_reminder);
		
		dateEdit = (EditText) findViewById(R.id.choose_time);
		dateEdit.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 
				 showTimePickerDialog(v);		 
			 }
		 });
		
		analogClock = (android.widget.AnalogClock) findViewById(R.id.analogClock1);
 	
	} // end onCreate

	
	public void showTimePickerDialog(View v) {
		 DialogFragment newFragment = new TimePickerFragment();
		 newFragment.show(getFragmentManager(), "timePicker");
	}
	
	// click of button, sets your alarm
	public void saveReminderDetails(View v) {
//		// sets button to pressed state of orange
//		reminderButton.setBackgroundColor(Color.parseColor("#ffd27f"));
		
		// save title
		stringTitle = title.getText().toString();
		
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		// use calendar instance to set user chosen date and time
		Calendar cal = Calendar.getInstance();
		
		  //---sets the time for the alarm to trigger---                
        cal.set(Calendar.HOUR_OF_DAY, TimePickerFragment.hour);
        cal.set(Calendar.MINUTE, TimePickerFragment.minute);
        cal.set(Calendar.SECOND, 0);
		
		// set up the Broadcast with Intent and Pending Intent.
		// FLAG_UPDATE_CURRENT allows you to re-use the alarm.	
		//If you are broadcasting these Intents, or if you are wrapping an 
		//Intent in a getBroadcast() PendingIntent, you do not need action strings. 
		//Use the Intent constructor that takes the Java class object as the second 
		//parameter, and use that:new Intent(this, MyReceiver.class)
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("reminder_message", "It's time to " + stringTitle + " !");
		PendingIntent sender = PendingIntent.getBroadcast(this.getApplicationContext(), REQUEST_CODE,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		// AlarmManager allows alarm to go off even if app is not running
		// RTC_WAKEUP will wake device if it's off
		
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);

	}
}
