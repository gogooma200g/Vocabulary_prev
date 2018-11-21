package com.axioms.voca.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 김민정 on 2018-04-23.
 */

public class NetworkState {
    public static final int NET_TYPE_NONE = 0x00;
    public static final int NET_TYPE_WIFI = 0x01;
    public static final int NET_TYPE_MOBILE = 0x02;
    public static final int NET_TYPE_DUMMY = 0x03;

    private static NetworkState current = null;
    private Context context;

    public static NetworkState getInstance(Context context) {
        if(current == null) {
            current = new NetworkState(context);
        }
        return current;
    }

    public NetworkState(Context context) {
        this.context = context;
    }

    /**
     * WIFI
     */
    private boolean getWifiState() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isConnt = ni.isConnected();
        return isConnt;
    }

    /**
     * 3G/LTE
     */
    private boolean get3GState() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnt = ni.isConnected();
        return isConnt;
    }


    /**
     * check Network
     * @return
     */
    public boolean checkNetworkState() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if(networkInfo != null) {
            String networkName = networkInfo.getTypeName();
            LogUtil.d("networkName : " + networkName);
        }

        return isConnected;
    }

}





























