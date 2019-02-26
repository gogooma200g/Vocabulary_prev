package com.axioms.voca.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.axioms.voca.R;
import com.axioms.voca.util.LogUtil;

public class CustomEditeText extends AppCompatEditText {

    public interface onEditTextListener {
        void onBackPress();
        void onFocusMoved();
    }

    private onEditTextListener onEditTextListener;
    private InputMethodManager inputMethodManager;

    private boolean showKeyboard = false;

    public CustomEditeText(Context context) {
        super(context);
        init();
    }

    public CustomEditeText(Context context, AttributeSet attributes) {
        super(context, attributes);
        init();
        getAttrs(attributes);
    }

    public CustomEditeText(Context context, AttributeSet attributes, int defStyleAttr) {
        super(context, attributes, defStyleAttr);
        init();
        getAttrs(attributes, defStyleAttr);
    }

    private void init() {
        this.inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        final CustomEditeText editText = this;
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(showKeyboard) {
                    showKeyboard = !(inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED));
                }
            }
        });
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomEditeText);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomEditeText, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        boolean focusRequest = typedArray.getBoolean(R.styleable.CustomEditeText_setRequestFocus, false);
        int selection = typedArray.getInteger(R.styleable.CustomEditeText_setSelection, 0);

        LogUtil.i("focusRequest : " + focusRequest);
        LogUtil.i("selection : " + selection);
        if(focusRequest) this.requestFocus();
        this.setSelection(selection);
    }



    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        try {
            if(!focused) this.showKeyboard = false;
            if(focused) onEditTextListener.onFocusMoved();
        }catch (Exception e) {
            LogUtil.e(e.toString());
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }


    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        boolean result = super.requestFocus(direction, previouslyFocusedRect);
        this.showKeyboard = true;
        final CustomEditeText editeText = this;
        this.post(new Runnable() {
            @Override
            public void run() {
                showKeyboard = !(inputMethodManager.showSoftInput(editeText, InputMethodManager.SHOW_FORCED));
            }
        });
        return result;
    }

    public void setOnBackPressListener(onEditTextListener listener) {
        onEditTextListener = listener;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER) return false;
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        LogUtil.i("onKeyPreIme");
        if (keyCode == KeyEvent.KEYCODE_BACK && onEditTextListener != null) {
            onEditTextListener.onBackPress();
        }

        return super.onKeyPreIme(keyCode, event);
    }
}














