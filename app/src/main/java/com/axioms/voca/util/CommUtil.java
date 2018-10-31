package com.axioms.voca.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 김민정 on 2018-04-03.
 */

public class CommUtil {

    public static boolean isTablet(Context context) {
        int screenLarge = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        LogUtil.i("screenLarge" + screenLarge);
        return (screenLarge > Configuration.SCREENLAYOUT_SIZE_NORMAL);
    }

    public static int dpToPx(Resources resources, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static int spToPx(Resources resources, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, resources.getDisplayMetrics());
    }


    public static String getData(Context context, String fileName) throws IOException {

        String result = "";

        AssetManager am = context.getResources().getAssets();
        InputStream is = null;

        try {
            is = am.open(fileName);
            int size = is.available();

            if (size > 0) {
                byte[] data = new byte[size];
                is.read(data);
                result = new String(data);

                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (Exception e) {
                }
            }
        }

        return result;
    }

    public static void setWidth(Activity activity, int layoutWidth, View view) {

        if (view == null)
            return;

        ViewGroup.LayoutParams params = null;

        if (view.getLayoutParams() instanceof android.widget.RelativeLayout.LayoutParams) {
            params = view.getLayoutParams();
        } else if (view.getLayoutParams() instanceof android.widget.LinearLayout.LayoutParams) {
            params = view.getLayoutParams();
        } else if (view.getLayoutParams() instanceof android.widget.FrameLayout.LayoutParams) {
            params = view.getLayoutParams();
        }

        if (params == null)
            return;

        params.width = layoutWidth;

        view.setLayoutParams(params);
    }

    public static void showKeyboard(final Context context, final View view) {
        if (view == null)
            return;

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.requestFocus();
                LogUtil.d("context : " + context);
                if (context == null)
                    return;

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

}
