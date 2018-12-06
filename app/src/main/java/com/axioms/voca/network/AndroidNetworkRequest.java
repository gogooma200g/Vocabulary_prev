package com.axioms.voca.network;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.axioms.voca.R;
import com.axioms.voca.base.GlobalApplication;
import com.axioms.voca.dialog.SystemCheckingActivity;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.util.ToastUtil;
import com.axioms.voca.vo.VoBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by 김민정 on 2018-03-28.
 */

public class AndroidNetworkRequest {

    private volatile static AndroidNetworkRequest mInstance;
    private static final int TIMEOUT_MS = 5000;
    private OkHttpClient okHttpClient;
    private Context mContext;

    public AndroidNetworkRequest(Context context) {
        mContext = context;
    }

    public static synchronized AndroidNetworkRequest getInstance(Context context) {
        if(mInstance == null) {
            synchronized (AndroidNetworkRequest.class) {
                mInstance = new AndroidNetworkRequest(context);
            }
        }
        mInstance.mContext = context;
        return mInstance;
    }

    public interface ListenerAndroidResponse {
        void success(String response);
        void systemcheck(String response);
        void fail(VoBase base);
        void exception(ANError error);
    }

    public interface ListenerDownloadResponse {
        void onComplete();
        void onError(ANError anError);
        void onProgress(long byteDownloaded, long totalBytes);
    }

    private void createHttpClient() {
        if(okHttpClient == null) {
            okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(TIMEOUT_MS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_MS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_MS, TimeUnit.SECONDS)
                    .build();
        }
    }

    public void StringRequest(String tag, String url, final ListenerAndroidResponse listener) {

        LogUtil.w("URL : " + url);
        createHttpClient();

        AndroidNetworking.get(url)
                .setTag(tag)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        LogUtil.w("Response : " + response);

                        VoBase result = null;

                        try {
                            result = GlobalApplication.getGson().fromJson(response, VoBase.class);
                        }catch (Exception e) {
                            LogUtil.e(e.toString());
                            result = new VoBase();
                            result.setRES_CODE(-1);
                            result.setRES_MSG("JSON POSING 오류");
                            String resultStr = GlobalApplication.getGson().toJson(result);
                            listener.fail(result);
                        }

//                        if (!response.contains("APPVER") && !response.contains("USERTYPE")) {
//                            result.RES_CODE = -2;
//                            result.setRES_MSG("ORA-00936: 누락된 표현식-1-1-1");
//                        }

                        if(result.isSuccess()) {
                            listener.success(response);
                            return;
                        }

                        if(result.isSystemCheck()) {
                            LogUtil.e("systemcheck");
                            listener.systemcheck(response);
                            startSystemCheckActivity(result.getRES_MSG());
                            return;
                        }

                        LogUtil.e("fail");

                        if (result.isServerError()) {
                            result = addServerErrMsg(result);
                        }

                        ToastUtil.show(mContext, result.getRES_MSG());
                        listener.fail(result);
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogUtil.e("Error : " + anError.getErrorDetail());
                        ToastUtil.show(mContext, anError.getErrorCode()+anError.getErrorDetail());
                        listener.exception(anError);
                    }
                });
    }

    public void StringRequest(String tag, String url, Map<String, String> quryParamMap, final ListenerAndroidResponse listener) {

        LogUtil.w("URL : " + url);
        createHttpClient();

        AndroidNetworking.get(url)
                .addQueryParameter(quryParamMap)
                .setTag(tag)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        LogUtil.w("Response : " + response);

                        VoBase result = null;

                        try {
                            result = GlobalApplication.getGson().fromJson(response, VoBase.class);
                        }catch (Exception e) {
                            result = new VoBase();
                            result.setRES_CODE(-1);
                            result.setRES_MSG("JSON POSING 오류");
                        }

                        if(result.isSuccess()) {
                            listener.success(response);
                            return;
                        }

                        if(result.isSystemCheck()) {
                            listener.systemcheck(response);
                            startSystemCheckActivity(result.getRES_MSG());
                            return;
                        }

                        if (result.isServerError()) {
                            result = addServerErrMsg(result);
                        }

                        ToastUtil.show(mContext, result.getRES_MSG());
                        listener.fail(result);
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogUtil.e("Error : " + anError.getErrorDetail());
                        ToastUtil.show(mContext, anError.getErrorCode()+anError.getErrorDetail());
                        listener.exception(anError);
//                        networkError();
                    }
                });
    }

    public void postJSONArrayRequest(String tag, String url, JSONArray param, final ListenerAndroidResponse listener) {

        LogUtil.d("URL : " + url);
        LogUtil.d("param : " + param);
        createHttpClient();

        AndroidNetworking.post(url)
                .addJSONArrayBody(param)
                .setTag(tag)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        LogUtil.d("Response : " + response);
                        VoBase result = null;

                        try {
                            result = GlobalApplication.getGson().fromJson(response, VoBase.class);
                        }catch (Exception e) {
                            result = new VoBase();
                            result.setRES_CODE(-1);
                            result.setRES_MSG("JSON POSING 오류");
                        }

                        if(result.isSuccess()) {
                            listener.success(response);
                            return;
                        }

                        if(result.isSystemCheck()) {
                            LogUtil.e("systemcheck");
                            listener.systemcheck(response);
                            startSystemCheckActivity(result.getRES_MSG());
                            return;
                        }

                        LogUtil.e("fail");

                        if (result.isServerError()) {
                            result = addServerErrMsg(result);
                        }

                        ToastUtil.show(mContext, result.getRES_MSG());
                        listener.fail(result);
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogUtil.e("ANError : " + anError.toString());
                        LogUtil.e("ANError : " + anError.getErrorDetail());
                        ToastUtil.show(mContext, anError.getErrorCode()+anError.getErrorDetail());
                        listener.exception(anError);
                    }
                });


    }

    public void postRequest(String tag, String url, Map<String, String> bodyParamMap, final ListenerAndroidResponse listener) {

        JSONObject obj=new JSONObject(bodyParamMap);

        LogUtil.d("URL : " + url);
        LogUtil.d("param : " + bodyParamMap);
        createHttpClient();

        AndroidNetworking.post(url)
                .addBodyParameter(bodyParamMap)
                .setTag(tag)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        LogUtil.d("Response : " + response);
                        VoBase result = null;

                        try {
                            result = GlobalApplication.getGson().fromJson(response, VoBase.class);
                        }catch (Exception e) {
                            result = new VoBase();
                            result.setRES_CODE(-1);
                            result.setRES_MSG("JSON POSING 오류");
                        }

                        if(result.isSuccess()) {
                            listener.success(response);
                            return;
                        }

                        LogUtil.e("fail");

                        if (result.isServerError()) {
                            result = addServerErrMsg(result);
                        }

                        ToastUtil.show(mContext, result.getRES_MSG());
                        listener.fail(result);
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogUtil.e("ANError : " + anError.toString());
                        LogUtil.e("ANError : " + anError.getErrorDetail());
                        LogUtil.e("ANError : " + anError.fillInStackTrace());
                        LogUtil.e("ANError : " + anError.getResponse());
                        ToastUtil.show(mContext, anError.getErrorCode()+anError.getErrorDetail());
                        listener.exception(anError);
                    }
                });


    }

    public void multipartRequest(String tag, String url, Map<String, String> params, Map<String, File> file, final ListenerAndroidResponse listener) {

        LogUtil.d("URL : " + url);
        createHttpClient();

        AndroidNetworking.upload(url)
                .addMultipartFile(file)
                .addMultipartParameter(params)
                .setTag(tag)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        LogUtil.d("Response : " + response);

                        VoBase result = null;

                        try {
                            result = GlobalApplication.getGson().fromJson(response, VoBase.class);
                        }catch (Exception e) {
                            result = new VoBase();
                            result.setRES_CODE(-1);
                            result.setRES_MSG("JSON POSING 오류");
                        }

                        if(result.isSuccess()) {
                            listener.success(response);
                            return;
                        }

                        if(result.isSystemCheck()) {
                            LogUtil.e("systemcheck");
                            listener.systemcheck(response);
                            startSystemCheckActivity(result.getRES_MSG());
                            return;
                        }

                        LogUtil.e("fail");

                        if (result.isServerError()) {
                            result = addServerErrMsg(result);
                        }

                        ToastUtil.show(mContext, result.getRES_MSG());
                        listener.fail(result);
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogUtil.e("Error : " + anError.getErrorDetail());
                        ToastUtil.show(mContext, anError.getErrorCode()+anError.getErrorDetail());
                        listener.exception(anError);
//                        networkError();
                    }
                });
    }


    /**
     * download
     */
    public void downloadRequest(String tag, String url, String dirPath, String fileName, final ListenerDownloadResponse listener) {

        LogUtil.d("URL : " + url);
        createHttpClient();

        AndroidNetworking.download(url, dirPath, fileName)
                .setTag(tag)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        listener.onProgress(bytesDownloaded, totalBytes);
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        LogUtil.d("onDownloadComplete");
                        listener.onComplete();
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogUtil.e("anError");
                        listener.onError(anError);
                        ToastUtil.show(mContext, anError.getErrorCode()+anError.getErrorDetail());
//                        networkError();
                    }
                });
    }

    public void cancel(String tag) {
        LogUtil.d("cancel AndroidNetworking : " + tag);
        AndroidNetworking.cancel(tag);
    }

    public void cancelAll() {
        LogUtil.d("isRequestRunning before cancel : " + AndroidNetworking.isRequestRunning(this));
        AndroidNetworking.cancel(this);
        LogUtil.d("isRequestRunning after cancel : " + AndroidNetworking.isRequestRunning(this));
    }

    private void startSystemCheckActivity(String msg) {
        Intent intent = new Intent(mContext, SystemCheckingActivity.class);
        intent.putExtra("msg", msg);
        mContext.startActivity(intent);

    }

    private VoBase addServerErrMsg(VoBase result) {
        //result.setRES_MSG(mContext.getResources().getString(R.string.msg_server_error)+result.getRES_MSG());
        return result;
    }
}
