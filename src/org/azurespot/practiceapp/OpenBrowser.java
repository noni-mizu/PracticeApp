package org.azurespot.practiceapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.azurespot.practiceapp.R;

@SuppressLint("SetJavaScriptEnabled")
public class OpenBrowser extends Activity {
	
	public static WebView wView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_browser);
		
		// loads a webpage into WebView widget
		wView = (WebView) findViewById(R.id.webview);
		wView.loadUrl("http://cuteoverload.com/");
		
		// enables javascript if a website uses it, only use if certain you need
		// javascript enabled, otherwise can present security issues.
		WebSettings webSettings = wView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		// this makes any links clicked on in your WebView
		// able to be opened in the WebView, instead of opening in an
		// external browser
		wView.setWebViewClient(new WebViewClient());
		
		// this makes the website fit into your WebView size.
		// prevents a huge website from loading as is.
		wView.getSettings().setUseWideViewPort(true);
	    wView.getSettings().setLoadWithOverviewMode(true);
	    
	    // enabling the size adjustment can kill your pinch zoom
	    // controls, this code enables it to come back!
	    wView.getSettings().setBuiltInZoomControls(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// remove (disable) below line to get rid of "settings" in the menu, 
		// or you can remove the widget from res/menu/activity_name.xml
//		getMenuInflater().inflate(R.menu.open_browser, menu);
		
		// Add menu items, second value is the id, use this in the onCreateOptionsMenu
	    menu.add(0, 1, 0, "Back");
	    menu.add(0, 2, 0, "Refresh");
	    menu.add(0, 3, 0, "Forward");
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// Called when you tap a menu item
	    switch (item.getItemId()){
	        case 1: //If the ID equals 1, go back
	            wView.goBack();
	        return true;
	        case 2 : //If the ID equals 2, refresh
	            wView.reload();
	        return true;
	        case 3: //If the ID equals 3, go forward
	            wView.goForward();
	        return true;
	        }
	    return false;
	    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { // Enables browsing to previous pages with the hardware back button
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && wView.canGoBack()) { // Check if the key event was the BACK key and if there's history
	        wView.goBack();
	        return true;
	    }   // If it wasn't the BACK key or there's no web page history, bubble up to the default
	        // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}
}
