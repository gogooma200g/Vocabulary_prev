package com.axioms.voca.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

public class PermissionBase {

//    public static boolean isDenied(Context context, @NonNull String permission) {
//        return !isGranted(context, permission);
//    }

    private static boolean isGranted(Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isGranted(Context context, @NonNull String... permissions) {
        for(String permission : permissions) {
            if(!isGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }



}
