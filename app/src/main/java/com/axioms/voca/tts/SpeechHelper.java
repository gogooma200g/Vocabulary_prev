package com.axioms.voca.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.axioms.voca.util.LogUtil;

import java.util.HashMap;
import java.util.Locale;

public class SpeechHelper implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;

    private boolean ready = false;
    private boolean allowed = false;
    private UtteranceProgressListener mProgressListener;

    public SpeechHelper(Context context, UtteranceProgressListener listener) {
        tts = new TextToSpeech(context, this);
        tts.setOnUtteranceProgressListener(listener);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public void onInit(int status) {
        LogUtil.i("onInit :  ");
        if(status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
            tts.setSpeechRate(0.8f);
            ready = true;
        }else{
            ready = false;
        }
    }

    public void speak(String text, Locale type, float rate) {
        if(ready && allowed) {
//            HashMap<String, String> hash = new HashMap<>();
//            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.setLanguage(type);
            tts.setSpeechRate(rate);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            tts.speak(text,TextToSpeech.QUEUE_FLUSH, map);
        }
    }

    public void pause(int duration) {
        tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
    }

    public void stop() {
        tts.stop();
    }

    public void destroy() {
        tts.shutdown();
    }
}







