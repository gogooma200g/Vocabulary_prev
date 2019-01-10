package com.axioms.voca.permission;

import android.content.Context;

public class Permission extends PermissionBase {
    public static final String TAG = Permission.class.getSimpleName();

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder extends PermissionBuilder<Builder> {
        private Builder(Context context) {
            super(context);
        }

        public void check() {
            checkPermissions();
        }
    }
}
