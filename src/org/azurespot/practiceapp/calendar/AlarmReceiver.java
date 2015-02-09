package org.azurespot.practiceapp.calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{
	
	@Override
	 public void onReceive(Context context, Intent intent) {
	   try {
	     Bundle bundle = intent.getExtras();
	     String message = bundle.getString("reminder_message");
	     Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	    } catch (Exception e) {
	     Toast.makeText(context, "There was an error, but "
	     		+ "we still received an alarm", Toast.LENGTH_SHORT).show();
	     e.printStackTrace();
	 
	    }
	   
	   // Vibrate the mobile phone
       Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
       vibrator.vibrate(2000);
	 }

}
