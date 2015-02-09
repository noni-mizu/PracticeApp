package org.azurespot.practiceapp.location;

import org.azurespot.practiceapp.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class LocationFragment extends Fragment {

	private static View view;
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */

	// GoogleMap is the class that shows the map
	public static GoogleMap mMap;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {

	    view = (RelativeLayout) inflater.inflate(R.layout.activity_location_main,
	    													container, false);
        
        setUpMapIfNeeded(); // For setting up the MapFragment
	           
	    return view;
	}

	/***** Sets up the map if it is possible to do so *****/
	public static void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        // Try to obtain the map from the MapFragment, using getMap()
	        mMap = ((MapFragment) LocationMain.fragmentManager
	                .findFragmentById(R.id.location_map)).getMap();
	    }
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

	    if (mMap == null) {
	        // Try to obtain the map from the MapFragment.
	        mMap = ((MapFragment) LocationMain.fragmentManager
	                .findFragmentById(R.id.location_map)).getMap();

	    }
	}
	
	// resets map when you use the back button
	public static void onBackPressed()
    {
		if (mMap != null) {
	        LocationMain.fragmentManager.beginTransaction()
	            .remove(LocationMain.fragmentManager.findFragmentById(R.id.location_map)).commit();
	        mMap = null;
	    }
    }

	/**** The mapfragment's id must be removed from the FragmentManager
	 **** or else if the same it is passed on the next time then 
	 **** app will crash ****/
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	    if (mMap != null) {
	        LocationMain.fragmentManager.beginTransaction()
	            .remove(LocationMain.fragmentManager.findFragmentById(R.id.location_map)).commit();
	        mMap = null;
	    }
	}
	}
