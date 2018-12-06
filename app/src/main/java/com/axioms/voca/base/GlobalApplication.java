package com.axioms.voca.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;

import com.androidnetworking.AndroidNetworking;
import com.axioms.voca.BuildConfig;
import com.axioms.voca.R;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by kiel1 on 2018-10-19.
 */

public class GlobalApplication extends Application {

    private static GlobalApplication singleton;
    public static volatile Activity currentActivity = null;

    private Context mContext;
    private Thread.UncaughtExceptionHandler mCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {

            LogUtil.d("fabric uncaughtException");

            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    ToastUtil.show(mContext, R.string.error_app);
                    Looper.loop();
                }
            }.start();


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /** Intro로 이동 **/
//            Intent i = new Intent(mContext, IntroActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//
//            System.exit(1);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;
        mContext = getApplicationContext();

        AndroidNetworking.initialize(getApplicationContext());

        if(!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(mCaughtExceptionHandler);
        }
    }

    private static Gson gson;

    public static Gson getGson() {
        if(gson != null) return gson;
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .serializeNulls()
                .create();
        return gson;
    }

}













