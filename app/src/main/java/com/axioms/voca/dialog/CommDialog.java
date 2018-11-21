package com.axioms.voca.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.axioms.voca.R;

/**
 * Created by kiel1 on 2018-10-30.
 */

public class CommDialog extends Dialog implements View.OnClickListener {

    private Builder builder;

    public CommDialog(Builder builder) {
        super(builder.context);
        this.builder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_comm);

        setCancelable(builder.isCancelable);
        setCanceledOnTouchOutside(builder.isCancelableTouchOutside);

        setTitle(builder.title);
        setConts(builder.conts);
        setGravity(builder.contsGravity);
        setButtons(builder.buttonTexts);
        setOnCancelListener(builder.onCancelListener);

        findViewById(R.id.btn_close).setOnClickListener(this);
    }

    public void setTitle(int title) {
        if(title == -1) return;
        TextView tv = (TextView) findViewById(R.id.tv_title);
        tv.setText(title);
        tv.setVisibility(View.VISIBLE);
    }

    public void setConts(int conts) {
        if(conts == -1) return;
        TextView tv = (TextView) findViewById(R.id.tv_conts);
        tv.setText(conts);
        tv.setVisibility(View.VISIBLE);
    }

    public void setGravity(int gravity) {
        TextView tv = (TextView) findViewById(R.id.tv_conts);
        tv.setGravity(gravity);
    }

    public void setButtons(int... text) {

        if(text.length == 1) {
            Button btn1 = (Button) findViewById(R.id.btn_first);
            btn1.setText(text[0]);
            btn1.setVisibility(View.VISIBLE);
            btn1.setOnClickListener(this);
            btn1.setBackgroundResource(R.drawable.btn_popup_left);
        }
        else if(text.length == 2) {
            Button btn1 = (Button) findViewById(R.id.btn_first);
            btn1.setText(text[0]);
            btn1.setVisibility(View.VISIBLE);
            btn1.setOnClickListener(this);
            btn1.setBackgroundResource(R.drawable.btn_popup_left);

            Button btn2 = (Button) findViewById(R.id.btn_second);
            btn2.setText(text[1]);
            btn2.setVisibility(View.VISIBLE);
            btn2.setOnClickListener(this);
            btn2.setBackgroundResource(R.drawable.btn_popup_right);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_close) {
            dismiss();
            return;
        }

        int type = -1;
        if(view.getId() == R.id.btn_first) {
            type = ListenerDialog.DIALOG_BTN_FIRST;
        }else if(view.getId() == R.id.btn_second) {
            type = ListenerDialog.DIALOG_BTN_SECOND;
        }
        builder.listenerDialog.onClick(this, type, view);
    }

    public interface ListenerDialog {
        int DIALOG_BTN_FIRST = 1;
        int DIALOG_BTN_SECOND = 2;

        void onClick(Dialog dialog, int result, Object object);
    }


    public static class Builder {
        private Context context;
        private boolean isCancelable = true;
        private boolean isCancelableTouchOutside = true;

        private int contsGravity = Gravity.CENTER;
        private int title = -1;
        private int conts = -1;
        private int[] buttonTexts;
        private Object[] buttonObjs;

        private ListenerDialog listenerDialog = new ListenerDialog() {
            @Override
            public void onClick(Dialog dialog, int result, Object object) {
            }
        };

        private OnCancelListener onCancelListener = new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        };

        public Builder(Context context) { this.context = context; }

        public CommDialog build() { return new CommDialog(this); }

        public Builder setTitle(int title) {
            this.title = title;
            return this;
        }

        public Builder setConts(int conts) {
            this.conts = conts;
            return this;
        }

        public Builder setContsGravity(int gravity) {
            this.contsGravity = gravity;
            return this;
        }

        public Builder setBtns(int... str) {
            this.buttonTexts = str;
            return this;
        }

        public Builder setCancelable(boolean flag) {
            this.isCancelable = flag;
            return this;
        }

        public Builder setCancelableTouchOutside(boolean flag) {
            this.isCancelableTouchOutside = flag;
            return this;
        }

        public Builder setListener(ListenerDialog listener) {
            this.listenerDialog = listener;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener listener) {
            this.onCancelListener = listener;
            return this;
        }
    }
}












