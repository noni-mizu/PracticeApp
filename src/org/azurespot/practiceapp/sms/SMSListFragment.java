package org.azurespot.practiceapp.sms;

import org.azurespot.practiceapp.R;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SMSListFragment extends ListFragment{
	
	public static View viewList;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
		
      // Defines the XML file for the fragment
      viewList = inflater.inflate(R.layout.sms_messages_fragment, 
    		  											container, false);
      return viewList;
    }
	
	
	public static class SMSReceiver extends BroadcastReceiver{
		
		protected static final String SMS_RECEIVED = "android.provider.Telephony.SMS";

		@Override
		public void onReceive(final Context context, final Intent intent) {
			
			//---get the SMS message passed in---
	        Bundle bundle = intent.getExtras();        
	        SmsMessage[] messages = null;
	        String msgString = ""; 
	        
	        if (bundle != null)
	        {
	            //---retrieve the SMS message received---
	            Object[] pdus = (Object[]) bundle.get("pdus");
	            messages = new SmsMessage[pdus.length];            
	            for (int i = 0; i < messages.length; i++){
	            	messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
	            	msgString += "SMS From: " + messages[i].getOriginatingAddress();                     
	            	msgString += " :";
	            	msgString += messages[i].getMessageBody().toString();
	            	msgString += "\n"; 
	            }
	            
	            //---display the new SMS message---
	            SMSMain smsMain = SMSMain.instance();
	            smsMain.updateList(msgString);
	        }
		}

	}

}
