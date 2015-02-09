package org.azurespot.practiceapp.sms;

import java.util.Locale;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class Speaker implements OnInitListener{
	
	
	public static final String KEY_PARAM_UTTERANCE_ID = "Utterance ID";
	private TextToSpeech tts;
    private boolean ready = false;
    private boolean allowed = false;
     
    public Speaker(Context context){
        tts = new TextToSpeech(context, this);      
    }   
     
    public boolean isAllowed(){
        return allowed;
    }
     
    public void allow(boolean allowed){
        this.allowed = allowed;
    }

	@Override
	public void onInit(int status) {
		if(status == TextToSpeech.SUCCESS){
	        // Change this to match your
	        // locale
	        tts.setLanguage(Locale.US);
	        ready = true;
	    }else{
	        ready = false;
	    }
		
	}
	
	public void speak(CharSequence text){
		
		short streamNotification = (int)AudioManager.STREAM_NOTIFICATION;
	     
	    // Speak only if the TTS is ready
	    // and the user has allowed speech
	    if(ready && allowed) {
	        Bundle bundle = new Bundle();
	        bundle.putShort(TextToSpeech.Engine.KEY_PARAM_STREAM, 
	        		streamNotification);
	        tts.speak(text, TextToSpeech.QUEUE_ADD, 
	        		bundle, KEY_PARAM_UTTERANCE_ID);
	    }
	}
	
	// adds pauses to speech to make it more clear
	public void pause(long duration){
	    tts.playSilentUtterance(duration, TextToSpeech.QUEUE_ADD, 
	    					KEY_PARAM_UTTERANCE_ID);
	}
	
	// Free up resources when it's no longer needed
	public void destroy(){
	    tts.shutdown();
	}

}
