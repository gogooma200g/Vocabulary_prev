package com.axioms.voca.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	static int DEFAULT_DURATION = Toast.LENGTH_SHORT;

	static Toast toast = null;

	private static boolean isCanShow(Context context, String msg){
		if (context == null)
			return false;
		
		if(context instanceof Activity){
			if(((Activity)context).isFinishing()){
				return false;
			}
		}
		if (msg == null) {
			return false;
		}
		return true;
	}
	private static boolean isCanShow(Context context){
		if(context == null){
			return false;
		}

		if(context instanceof Activity){
			if(((Activity)context).isFinishing()){
				return false;
			}
		}
		return true;
	}

	public static void show(Context context, final String msg) {
		if(!isCanShow(context, msg)){
			return;
		}

		if (toast != null)
			toast.cancel();
		
		if (context instanceof Activity) {
			final Activity activity = (Activity)context;
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					toast = Toast.makeText(activity, msg, DEFAULT_DURATION);
					toast.show();
				}
			});
		} else {
			toast = Toast.makeText(context, msg, DEFAULT_DURATION);
			toast.show();
		}
		
	}

	public static void show(Context context, String msg, int duration) {
		if(!isCanShow(context, msg)){
			return;
		}

		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(context, msg, duration);
		toast.show();
	}

	public static void show(Context context, int msgId) {
		if(!isCanShow(context)){
			return;
		}

		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(context, msgId, DEFAULT_DURATION);
		toast.show();
	}

	public static void show(Context context, int msgId, int duration) {
		if(!isCanShow(context)){
			return;
		}
		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(context, msgId, duration);
		toast.show();
	}

}
