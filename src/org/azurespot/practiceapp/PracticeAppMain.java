package org.azurespot.practiceapp;

import org.azurespot.practiceapp.addressbook.AddressBook;
import org.azurespot.practiceapp.calendar.CalendarShow;
import org.azurespot.practiceapp.location.LocationMain;
import org.azurespot.practiceapp.sms.SMSMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PracticeAppMain extends Activity {
	
	Button call;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice_app_main);
		
		call = (Button)findViewById(R.id.button_call);
	}
	
	public void openDialer(View v) {
		
		Intent i = new Intent(this, MakeCall.class);
		startActivity(i);
	}
	
	public void toCalendar(View v) {
		
		Intent i = new Intent(this, CalendarShow.class);
		startActivity(i);
	}
	
	public void openBrowser(View v) {
		
		Intent i = new Intent(this, OpenBrowser.class);
		startActivity(i);
	}
	
	public void toContacts(View v) {
		
		Intent i = new Intent(this, AddressBook.class);
		startActivity(i);
		
	}
	
	public void toShowLocation(View v) {
		
		Intent i = new Intent(this, LocationMain.class);
		startActivity(i);
		
	}
	
	public void toSMS(View v) {
		
		Intent i = new Intent(this, SMSMain.class);
		startActivity(i);
		
	}

}
