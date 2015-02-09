package org.azurespot.practiceapp.location;

import org.azurespot.practiceapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationMain extends Activity {
	
	public static FragmentManager fragmentManager;
	public final int RQS_GooglePlayServices = 1;
	
	protected static Double latitude;
	protected static Double longitude;
	protected static Context context;
	protected static EditText addressInput;
	
	protected LatLng myPosition;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_main);
		
		fragmentManager = getFragmentManager();
	
	}
	
	// locates the user's location
	public void findMe(View v){
		
		if (LocationFragment.mMap == null){
			LocationFragment.setUpMapIfNeeded();
		}
		
		if (LocationFragment.mMap != null) {
			
		// Enabling MyLocation Layer of Google Map
        LocationFragment.mMap.setMyLocationEnabled(true);	
			
		// Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) 
        								getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        
        // Creating a LatLng object for the current location
        myPosition = new LatLng(latitude, longitude);

        LocationFragment.mMap.addMarker(new MarkerOptions().position
        										(myPosition).title("Start"));
	    
	    // For zooming automatically to the Dropped PIN Location
		LocationFragment.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
				(new LatLng(latitude, longitude), 12.0f));
		}
	}
	
	// enter address of your choosing into the Dialog box
	public void findThroughPopup(View v) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// set title
		builder.setTitle("Enter an address");
		
		// set EditText into your dialog box layout, using FrameLayout
		final FrameLayout frameView = new FrameLayout(this);
		builder.setView(frameView);
		// must inflate EditText separately
		AlertDialog alertDialog = builder.create();
		LayoutInflater inflater = alertDialog.getLayoutInflater();
		View etView = inflater.inflate(R.layout.alert_dialog_edit_text, frameView);
		// initialize EditText, use etView to pull from its specific XML
        addressInput = (EditText)etView.findViewById(R.id.address_field);
		
		// set Find button that finds address by showing on map
		builder.setPositiveButton("Find", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				
				// go through map setup if needed, otherwise process address input
				if (LocationFragment.mMap == null){
					
					LocationFragment.setUpMapIfNeeded();
				}
				
				if (LocationFragment.mMap != null) {
					
			        String userLocale = addressInput.getText().toString();
			        if(userLocale!=null && !userLocale.equals("")){
	                    new MyGeocoderTask(getApplicationContext()).execute(userLocale); 
			        }
				}
				// zooms in to wherever you map ends up,
				// the 15f is in a range of 2.0 - 21.0
				CameraUpdate camZoom = CameraUpdateFactory.zoomTo(12f);
				LocationFragment.mMap.animateCamera(camZoom);
			}
		});
		
		// set Cancel button
		builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// put all your builders in one create command
		alertDialog = builder.create();
		
		// show the dialog box
		alertDialog.show();
		
	} // end findThroughPopup()
	
	// resets map when back button is pressed
	 @Override
	    public void onBackPressed()
	    {
	        super.onBackPressed();
	        LocationFragment.onBackPressed();
	    }
	 

/*****use this to check if Google Play Services is available*****/
//	@Override
//	 protected void onResume() {
//	  super.onResume();
//
//	  int resultCode = GooglePlayServicesUtil
//			  .isGooglePlayServicesAvailable(getApplicationContext());
//	  
//	  if (resultCode == ConnectionResult.SUCCESS){
//	   Toast.makeText(getApplicationContext(), 
//	     "isGooglePlayServicesAvailable SUCCESS", 
//	     Toast.LENGTH_LONG).show();
//	  }else{
//	   GooglePlayServicesUtil.getErrorDialog(resultCode, 
//			   this, RQS_GooglePlayServices).show();
//	  }
//	}



}
