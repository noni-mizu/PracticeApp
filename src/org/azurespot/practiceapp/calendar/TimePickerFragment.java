package org.azurespot.practiceapp.calendar;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
TimePickerDialog.OnTimeSetListener {
	
	public static int hour;
	public static int minute;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
	// Use the current time as the default values for the picker
	final Calendar c = Calendar.getInstance();
	int hour = c.get(Calendar.HOUR_OF_DAY);
	int minute = c.get(Calendar.MINUTE);
	
	// Create a new instance of TimePickerDialog and return it
	return new TimePickerDialog(getActivity(), this, hour, minute, false);
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String am_pm = "";
		
		TimePickerFragment.hour = hourOfDay;
		TimePickerFragment.minute = minute;

	    Calendar datetime = Calendar.getInstance();
	    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
	    datetime.set(Calendar.MINUTE, minute);

	    if (datetime.get(Calendar.AM_PM) == Calendar.AM)
	        am_pm = "AM";
	    else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
	        am_pm = "PM";
	    
	    String strHrsToShow = (datetime.get(Calendar.HOUR) == 0)
	    		?"12":datetime.get(Calendar.HOUR)+"";
	    
		// Do something with the time chosen by the user
		CalendarShow.dateEdit.setText(strHrsToShow + ":" + 
				datetime.get(Calendar.MINUTE) +" "+ am_pm);
	}
}	

