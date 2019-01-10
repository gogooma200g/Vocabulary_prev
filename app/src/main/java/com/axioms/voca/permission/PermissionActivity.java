package com.axioms.voca.permission;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;

import com.axioms.voca.R;
import com.axioms.voca.dialog.CommDialog;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    public static final int REQ_CODE_PERMISSION_REQUEST = 10;

    public static final String EXTRA_PERMISSIONS = "permissions";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    //private static Deque<PermissionListener> permissionListenerStack;
    private static PermissionListener permissionListener;

    String[] permissions;

    int dialogTitle;
    int dialogMessage;

    boolean isShownDialog = false;

    public static void startActivity(Context context, Intent intent, PermissionListener listener) {
        permissionListener = listener;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        setupFromSavedInstanceState(savedInstanceState);

        LogUtil.i("PermissionActivity onCreate");
        checkPermissions();
    }

    private void setupFromSavedInstanceState(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            permissions = savedInstanceState.getStringArray(EXTRA_PERMISSIONS);
            dialogTitle = savedInstanceState.getInt(EXTRA_TITLE);
            dialogMessage = savedInstanceState.getInt(EXTRA_MESSAGE);
        }else{
            Intent intent = getIntent();
            permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS);
            dialogTitle = intent.getIntExtra(EXTRA_TITLE, -1);
            dialogMessage = intent.getIntExtra(EXTRA_MESSAGE, -1);
        }
    }

    private void checkPermissions() {

        List<String> needPermission = new ArrayList<>();

        for(String permission : permissions) {
            if(!PermissionBase.isGranted(this, permission)) {
                needPermission.add(permission);
            }
        }

        if(needPermission.isEmpty()) {
            permissionResult(null);
        }else{
            showPermissionDialog(needPermission);
        }

    }

    private void permissionResult(List<String> deniedPermissions) {

        finish();
        if(permissionListener != null) {
            if(ObjectUtils.isEmpty(deniedPermissions)) {
                permissionListener.onPermissionGranted();
            }else{
                permissionListener.onPermissionDenied(deniedPermissions);
            }
        }
    }

    public void requestPermissions(List<String> needPermissions) {
        ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]),
                REQ_CODE_PERMISSION_REQUEST);
    }

    private void showPermissionDialog(final List<String> needPermission) {

        new CommDialog.Builder(this)
                .setTitle(dialogTitle)
                .setConts(dialogMessage)
                .setBtns(R.string.str_confirm)
                .setContsGravity(Gravity.LEFT)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                        requestPermissions(needPermission);
                        isShownDialog = false;
                    }
                })
                .setListener(new CommDialog.ListenerDialog() {
                    @Override
                    public void onClick(Dialog dialog, int result, Object object) {
                        dialog.dismiss();
                        requestPermissions(needPermission);
                        isShownDialog = false;
                    }
                }).build().show();

        isShownDialog = true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        for(String permission : permissions) {
            LogUtil.i("permission :: " + permission);
        }

        for(int grant : grantResults) {
            LogUtil.i("grant :: " + grant);
        }

        finish();
    }
}
