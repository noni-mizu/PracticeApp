package org.azurespot.practiceapp.addressbook;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.azurespot.practiceapp.R;

/*
 * Partially from http://stackoverflow.com/questions/18199359/how-to-display-contacts-in-a-listview-in-android-for-android-api-11
 */

public class ContactsFragment extends ListFragment implements 
								LoaderCallbacks<Cursor>{
	
	private static CursorAdapter listAdapter;
	public Cursor cursor;
	private android.content.Context context;
	public View view;
	public static Uri uri;
	public static ListView listView;
	
	public static final String[] FROM = new String[]{ 
		ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
    	ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
    	ContactsContract.CommonDataKinds.Phone.NUMBER 
	};
	
	private static final int[] TO = { 
		R.id.contact_thumbnail, 
		R.id.contact_name, 
		R.id.contact_number 
	};
	
	// columns requested from the database
	private static final String[] PROJECTION = new String[]{
		ContactsContract.Contacts._ID,
		ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER ,
    };
	
	// FILTER will go in the CursorLoader parameter list, it filters
	// out only those contacts who have a phone number
	private static final String FILTER = 
			"(" + ContactsContract.Contacts.IN_VISIBLE_GROUP + 
			" = '1' AND (" + ContactsContract.CommonDataKinds.Phone.NUMBER  + " != 0 ))";

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,Bundle bundle) {
		
		// since using ListFragment, you must inflate your view this way
		View rootView = inflater.inflate(R.layout.fragment_list_view,
                container, false);
		
		return rootView;
		
	} // end onCreateView 
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        listView = (ListView)getListView();
	    
	    // create a new SimpleCursorAdapter adapter
	    context = getActivity();
	    Cursor c = null; // there is no cursor yet
	    int flags = 0; // no auto-re-query! (My loader re-queries.)
	    // put List UI row in adapter, create empty adapter
	    listAdapter = new SimpleCursorAdapter(context, 
	    		R.layout.fragment_list_item, c, FROM, TO, flags);
	    
	    // Initializes the CursorLoader. The URL_LOADER value is 
	    // eventually passed to onCreateLoader().
	    getLoaderManager().initLoader(0, null, this);
	    
	    // ListFragment then sets the adapter
	    setListAdapter(listAdapter);
	 
	}
		 
    // Empty public constructor, required by the system
    public ContactsFragment() {}

    /*
     * The 3 methods below are standard for the LoaderCallbacks<Cursor> interface.
     * Here, CursorLoader does a query in the background.
     */
   
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// load from the "Contacts table"
        Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        // the query
        return new CursorLoader(getActivity(),
                contentUri,
                PROJECTION,
                FILTER,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		 // Once cursor is loaded, give it to adapter
        listAdapter.swapCursor(data);
        
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// Delete the reference to the existing Cursor,
		// so it can recycle it
        listAdapter.swapCursor(null);
		
	}

}