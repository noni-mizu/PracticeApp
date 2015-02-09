package org.azurespot.practiceapp.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.azurespot.practiceapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class SMSMain extends Activity {
	
	protected EditText etPhoneNumber;
	protected static EditText etMessage;
	protected View dialogView;
	private static final int CONTACT_PICKER_RESULT = 1001;
	AlertDialog dialog;
	protected static final int RESULT_SPEECH = 1;
	private static SMSMain smsMain;
	ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter<String> arrayAdapter;
    BroadcastReceiver receiver;
    protected static Cursor smsInboxCursor;
    
    // Text-to-Speech
    TextToSpeech ttobj;
    String item;
    boolean on;
    boolean off;
    ToggleButton toggle;
    
	public static SMSMain instance() {
        return smsMain;
    }
	
	@Override
    public void onStart() {
        super.onStart();
        smsMain = this;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smsmain);
		
		// initialize ListView/adapter for displaying SMS messages
		smsListView = (ListView) findViewById(R.id.messages_list);
        arrayAdapter = new ArrayAdapter<String>(this, 
        		android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);
        
        // long click on ListView item will delete message
        smsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
	        public boolean onItemLongClick(AdapterView<?> parent, View view,
	                int pos, long arg3) {
				
	            //removes item at position you long-click on it
				smsMessagesList.remove(pos);//where pos is position of item you click
				arrayAdapter.notifyDataSetChanged();  
	                
	             return true;
			}
		}); 
        
        // Text-to-Speech initialize & set language
        ttobj = new TextToSpeech(getApplicationContext(), 
        						new TextToSpeech.OnInitListener() {
    	   @Override
    	   public void onInit(int status) {
    		   ttobj.setLanguage(Locale.US);
    	   
    	   }
        });
        

	} // end onCreate
	
	
	public void onResume(){
		super.onResume();
		// register receiver here, not in Manifest
		IntentFilter filter = new IntentFilter(SMSListFragment.SMSReceiver.SMS_RECEIVED);
		receiver = new SMSListFragment.SMSReceiver();
		registerReceiver(receiver, filter);
	}
	
	public void onPause() {
		// for Text-to_Speech, conserves resources when paused
		if(ttobj !=null){
	         ttobj.stop();
	         ttobj.shutdown();
	      }
		super.onPause();
		// recommended to unregister receiver here if using the onPause() & onResume() ways
		// as opposed to registering in onCreate()
		unregisterReceiver(receiver);
	}
	

	public void readSMS(View v){
		
		refreshSmsInbox();
	}
	
	// adds text messages to the ListView via the adapter
	public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        smsInboxCursor = contentResolver.query(Uri.parse
        			("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
    }
	
	public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }
	
	// speak method depreciated in API 21, so it's OK to use for now
	@SuppressWarnings("deprecation") 
	public void hearMessage(View v) {
		
		// set up toggle button
		toggle = (ToggleButton) findViewById(R.id.text_to_speech);
		on = toggle.isChecked();
		if (on) {
            toggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.speech_icon_on));
        } else {
            toggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.speech_icon));
            ttobj.stop();
        }
		
		// sets listener on the ListView items themselves
		smsListView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	if (on){
			    	item  = arrayAdapter.getItem(position);
			    	ttobj.speak(item, TextToSpeech.QUEUE_FLUSH, null);
		    	} else if (off)
		    		ttobj.stop(); 
		    }
		});

	}
   
	public void composeSMS(View v){
		
		// build your dialog box
		AlertDialog.Builder builder = new AlertDialog.Builder(SMSMain.this);
		// Get the layout inflater & inflate the box
		final LayoutInflater inflater = SMSMain.this.getLayoutInflater();
		dialogView = inflater.inflate(R.layout.dialog_compose_sms, null);
        builder.setView(dialogView);
        // get phone number and message IDs
        etPhoneNumber = (EditText)dialogView.findViewById(R.id.sms_phone);
	    etMessage = (EditText) dialogView.findViewById(R.id.sms_message);
        builder.setTitle("Send an SMS");
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
        	    
            	// Turns the entered phone number and message into String
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                String message = etMessage.getText().toString().trim(); 
                // error check to make sure there is input
                if (phoneNumber.length() > 0 && message.length() > 0) 
                	// sends the SMS
                    sendSMS(phoneNumber, message);                
                else
                    Toast.makeText(getBaseContext(), 
                        "Please enter a phone number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            	dialog.cancel();
            }
        });
        // put all your builders in one create command
        dialog = builder.create(); 
        // show the dialog box
        dialog.show();

	}
	
	// clicked the microphone button to start speech to text
	public void speechToText(View v) {

		Intent intent = new Intent(
                			RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        try {
            startActivityForResult(intent, RESULT_SPEECH);
            etMessage.setText("");
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Ooopps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
		
	}
	
	// method for sending the SMS
	private void sendSMS(String phoneNumber, String message)
	    {        
	        PendingIntent pi = PendingIntent.getActivity(this, 0,
	        						new Intent(this, SMSMain.class), 0);                
	        SmsManager sms = SmsManager.getDefault();
	        sms.sendTextMessage(phoneNumber, null, message, pi, null);        
	    }   
	
	public void addContact(View v) {
		
		// add a contact. using Android's ContactPicker
	    Intent i = new Intent(Intent.ACTION_PICK,
	    		ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
	    startActivityForResult(i, CONTACT_PICKER_RESULT);
	
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == RESULT_SPEECH){
			// speech to text result
			switch (requestCode) {
		        case RESULT_SPEECH: {
		            if (resultCode == RESULT_OK && null != data) {
		 
		                ArrayList<String> text = data
		                        .getStringArrayListExtra
		                        (RecognizerIntent.EXTRA_RESULTS);
		 
		                etMessage.setText(text.get(0));
		            }
		            break;
		        }
			}
        } // end if
		
		// picks a contact, puts into EditText
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case CONTACT_PICKER_RESULT:
            	Cursor cursor = null;
            	String phone = "";
            	try{
	            	Uri result = data.getData();
	                // get the contact id from the URI
	                String id = result.getLastPathSegment();
	                
	                // query for phone number
	                cursor = getContentResolver().query(
	                		ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
	                		null,
	                		ContactsContract.CommonDataKinds.Phone._ID + "=?",
	                        new String[]{id}, null);
	               
	                int phoneIdx = cursor.getColumnIndex(Phone.DATA);
	                
	                if (cursor.moveToFirst()) {
	                    phone = cursor.getString(phoneIdx);
	                } 
	             } catch (Exception e) {
	                	System.out.println("Getting contact failed.");
	             } finally {
	            	
                	etPhoneNumber.setText(phone, TextView.BufferType.EDITABLE);
	               
	            	if (phone.length() == 0) {
		                    Toast.makeText(this, "No phone number found for contact.",
		                    									Toast.LENGTH_LONG).show();
			        }
	            	 if (cursor != null){
		            		cursor.close();
		             }
	                
	             } // end finally
	             break;  
            }
        } 
         
    }
	
}
