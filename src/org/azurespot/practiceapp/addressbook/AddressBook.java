package org.azurespot.practiceapp.addressbook;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import org.azurespot.practiceapp.R;

// don't need FragmentActivity if higher than API 11, Activity is fine
public class AddressBook extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_book);
        
		// Begin the transaction
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // Replace the container with the new fragment
        ft.replace(R.id.contacts_fragment, new ContactsFragment());
        // Execute the changes specified
        ft.commit();
	}

}
