package org.azurespot.practiceapp.location;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/***An AsyncTask class for accessing the GeoCoding Web Service****/

public class MyGeocoderTask extends AsyncTask<String, Void, List<Address>>{
	
	protected LatLng enteredAddress;
	protected MarkerOptions markerOptions;
	protected Context mainContext;
	
	public MyGeocoderTask(Context c){
		mainContext = c;
	}
	
    @Override
    protected List<Address> doInBackground(String... locationName) {
        // Creating an instance of Geocoder class
        Geocoder geocoder = new Geocoder(mainContext);
        List<Address> addresses = null;

        try {
            // Getting a maximum of 3 Address that matches the input text
            addresses = geocoder.getFromLocationName(locationName[0], 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }
    
    // part of the AsyncTask life cycle
    @Override
    protected void onPostExecute(List<Address> addresses) {

        if(addresses==null || addresses.size()==0){
            Toast.makeText(LocationMain.context, "No Location found", 
            								Toast.LENGTH_SHORT).show();
        }

        // Clears all the existing markers on the map
        LocationFragment.mMap.clear();

        // Adding Markers on Google Map for each matching address
        for(int i=0;i<addresses.size();i++){

            Address address = (Address) addresses.get(i);

            // Creating an instance of GeoPoint, to display in Google Map
            enteredAddress = new LatLng(address.getLatitude(), address.getLongitude());

            String addressText = String.format("%s, %s",
            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
            address.getCountryName());

            markerOptions = new MarkerOptions();
            markerOptions.position(enteredAddress);
            markerOptions.title(addressText);

            LocationFragment.mMap.addMarker(markerOptions);

            // Locate the first location
            if(i==0)
                LocationFragment.mMap.animateCamera
                		(CameraUpdateFactory.newLatLng(enteredAddress));
        }
    }
}