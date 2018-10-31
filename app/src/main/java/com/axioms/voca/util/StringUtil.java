package com.axioms.voca.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kiel1 on 2018-10-30.
 */

public class StringUtil {

    public static boolean isNull(String str){
        if(str == null || "".equals(str.trim())){
            return true;
        }
        return false;
    }

    public static boolean isNull(EditText view){
        String txt = view.getText().toString().trim();
        if(txt == null || "".equals(txt)){
            return true;
        }
        return false;
    }

    public static String getString(TextView v){
        return v.getText().toString().trim();
    }
    public static String getString(EditText v){
        return v.getText().toString().trim();
    }

    /**
     * text에 color 입히기
     */
    public static void setTextByResColor(View view, String text, String colorStr, int color) {

        int start = text.indexOf(colorStr);
        int end = start + colorStr.length();

        if(start == -1) return;

        Spannable spannable = new SpannableStringBuilder(text);
        spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if(view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setText(spannable);
        }else if(view instanceof  EditText) {
            EditText et = (EditText) view;
            et.setText(spannable);
        }
    }
}
