package com.axioms.voca.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.axioms.voca.R;
import com.axioms.voca.dialog.CommDialog;
import com.axioms.voca.dialog.LoadingDialog;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.util.NetworkState;
import com.axioms.voca.util.ToastUtil;

/**
 * Created by kiel1 on 2018-10-30.
 */

public class BaseActivity extends AppCompatActivity {

    public static final int PERMISSIONS_REQUEST_PERMISSION = 1001;

    private LoadingDialog mLoadingDialog = null;

    private RetryListener retryListener = null;

    public interface RetryListener {
        void onRetry();
    }

    public void setRetryListener(RetryListener retryListener) {
        this.retryListener = retryListener;
    }

    /**
     * LoadingBar show
     */
    public void showDialog() {
        LogUtil.i("showDialog");
        if(mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.show();
        }else{
            if(!mLoadingDialog.isShowing())
                mLoadingDialog.show();
        }
    }

    /**
     * LoadingBar hide
     */
    public void hideDialog() {
        LogUtil.i("hideDialog");
        if(mLoadingDialog == null) return;
        if (mLoadingDialog.isShowing()) mLoadingDialog.dismiss();
    }

    /**
     * Connect Network (before connect to network)
     */
    public boolean checkNetwork() {
        if(!NetworkState.getInstance(this).checkNetworkState()) {
            LogUtil.d("네트워크 연결 안됨");
            showNetworkDialog();
            connectingNetwork();
            return false;
        }
        LogUtil.d("네트워크 연결 됨");
        return true;
    }

    /**
     * check Permission
     */
    public boolean checkPermission(final String[] permissions) {

        int permissionCheck = 0;

        for(String permission : permissions) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if(permissionCheck == PackageManager.PERMISSION_DENIED) break;
        }
        LogUtil.d("permissionCheck : " + permissionCheck);

        // No Permission
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {

            new CommDialog.Builder(this)
                    .setBtns(R.string.str_confirm)
                    .setTitle(R.string.dialog_permission_title)
                    .setConts(R.string.dialog_permission_conts)
                    .setContsGravity(Gravity.LEFT)
                    .setCancelable(false)
                    .setCancelableTouchOutside(false)
                    .setListener(new CommDialog.ListenerDialog() {
                        @Override
                        public void onClick(Dialog dialog, int result, Object object) {

                            if(result == CommDialog.ListenerDialog.DIALOG_BTN_FIRST) {
                                ActivityCompat.requestPermissions(BaseActivity.this, permissions, PERMISSIONS_REQUEST_PERMISSION);
                                dialog.dismiss();
                            }
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            LogUtil.i("permission Dialog onCancel");
                        }
                    })
                    .build().show();

            return false;
        }else{
            // Do Permission
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.i("onRequestPermissionsResult : 수락");
                }else{
                    LogUtil.i("onRequestPermissionsResult : 거절");
                }
                break;
        }
    }

    /**
     * Network Check Pop
     */
    private CommDialog networkDialog;
    private void showNetworkDialog() {

        if(networkDialog != null) {
            networkDialog.show();
            return;
        }

        networkDialog = new CommDialog.Builder(this)
                .setConts(R.string.dialog_describe)
                .setBtns(R.string.dialog_end, R.string.dialog_setting)
                .setListener(new CommDialog.ListenerDialog() {
                    @Override
                    public void onClick(Dialog dialog, int result, Object obj) {
                        if(CommDialog.ListenerDialog.DIALOG_BTN_FIRST == result) {
                            LogUtil.i("DIALOG_BTN_FIRST");
                            isRun = false;
                            networkDialog.dismiss();
                            networkDialog = null;
                            finish();
                        }else if(CommDialog.ListenerDialog.DIALOG_BTN_SECOND == result){
                            LogUtil.i("DIALOG_BTN_SECOND");
                            launchWifiSetting();
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        LogUtil.i("OnBackPressed");
                        isRun = false;
                        networkDialog.dismiss();
                        networkDialog = null;
                        finish();
                    }
                }).setCancelableTouchOutside(false).build();
        networkDialog.show();
    }

    /**
     * Connecting Network
     */
    boolean isRun = false;
    private void connectingNetwork() {
        isRun = true;
        LogUtil.i("checkingNetworks");
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(isRun) {
                    if(NetworkState.getInstance(BaseActivity.this).checkNetworkState()) {
                        LogUtil.i("connectingNetwork 연결됨");
                        setNetwordAvaiable();
                        isRun = false;
                    }
                    else{
                        LogUtil.i("connectingNetwork 연결안됨");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).start();
    }

    private Handler mHander = new Handler();

    /**
     * Connected Netword
     */
    private void setNetwordAvaiable() {
        if(null != networkDialog &&  networkDialog.isShowing())
            networkDialog.dismiss();

        LogUtil.d("네트워크가 연결 됐습니다.");

        mHander.post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show(BaseActivity.this, R.string.dialog_network_connected);
                if(null != retryListener) retryListener.onRetry();
            }
        });
    }

    /**
     * go to setting
     */
    private void launchWifiSetting(){
        Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
