package com.axioms.voca.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.axioms.voca.util.ObjectUtils;

public class PermissionBuilder<T extends PermissionBuilder> {

    private static final String PREFS_NAME_PERMISSION = "PREFS_NAME_PERMISSION";

    private PermissionListener listener;
    private String[] permissions;
    private int title;
    private int message;

    private Context context;

    public PermissionBuilder(Context context) {
        this.context = context;
    }

    protected void checkPermissions() {
        if(listener == null) {
            throw new IllegalArgumentException("You must setPermissionListener()");
        }else if(ObjectUtils.isEmpty(permissions)) {
            throw new IllegalArgumentException("You must setPermissions()");
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.onPermissionGranted();
            return;
        }

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(PermissionActivity.EXTRA_PERMISSIONS, permissions);
        intent.putExtra(PermissionActivity.EXTRA_TITLE, title);
        intent.putExtra(PermissionActivity.EXTRA_MESSAGE, message);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PermissionActivity.startActivity(context, intent, listener);
    }

    public T setPermissionListener(PermissionListener listener) {
        this.listener = listener;
        return (T) this;
    }

    public T setPermissions(String... permissions) {
        this.permissions = permissions;
        return (T) this;
    }

    public T setTitle(int title) {
        this.title = title;
        return (T) this;
    }

    public T setMessage(int message) {
        this.message = message;
        return (T) this;
    }
}















