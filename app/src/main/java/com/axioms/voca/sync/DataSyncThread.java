package com.axioms.voca.sync;

import android.content.Context;
import android.net.Uri;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.axioms.voca.base.ConstCommURL;
import com.axioms.voca.base.GlobalApplication;
import com.axioms.voca.dao.DaoColumns;
import com.axioms.voca.dao.VocaDAO;
import com.axioms.voca.dao.VocaListDAO;
import com.axioms.voca.network.AndroidNetworkRequest;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.vo.VoBase;
import com.axioms.voca.vo.VoVoca;
import com.axioms.voca.vo.VoVocaList;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by kiel1 on 2018-12-06.
 */

public class DataSyncThread extends Thread {

    /**
     * DataSync Listener
     */
    public interface DataSyncListener {
        void success(String response);
        void fail();
    }

    private Context context;

    private DataSyncListener dataSyncListener = new DataSyncListener() {
        @Override
        public void success(String response) {
            LogUtil.i("Save success");
        }

        @Override
        public void fail() {
            LogUtil.i("Save fail");
        }
    };

    public DataSyncThread(Context context) {
        this.context = context;
    }

    public DataSyncThread(Context context, DataSyncListener listener) {
        this.context = context;
        this.dataSyncListener = listener;
    }

    @Override
    public void run() {
//        setVocaList();
//        setVoca();
        testRequest();
    }

    /**
     * setVocaList
     */
    private void setVocaList() {

        final VocaListDAO vocaDAO = new VocaListDAO(context);
        ArrayList<VoVocaList> vocaLists =  vocaDAO.selectAll();

        if(vocaLists.size() < 0) return;

        String jsonData = GlobalApplication.getGson().toJson(vocaLists);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonData);
        }catch (Exception e){
            LogUtil.e(e.toString());
        }

        AndroidNetworkRequest.getInstance(context).postJSONArrayRequest(ConstCommURL.REQUEST_SET_VOCALIST,
                ConstCommURL.getApiUrl(ConstCommURL.REQUEST_SET_VOCALIST),
                jsonArray,
                new AndroidNetworkRequest.ListenerAndroidResponse() {

                    @Override
                    public void success(String response) {
                        LogUtil.i("response : " +  response);
                        vocaDAO.deleteAll();
                        dataSyncListener.success(response);
                    }

                    @Override
                    public void systemcheck(String response) {
                        dataSyncListener.fail();
                    }

                    @Override
                    public void fail(VoBase base) {
                        dataSyncListener.fail();
                    }

                    @Override
                    public void exception(ANError error) {
                        dataSyncListener.fail();
                    }
                });
    }

    /**
     * setVoca
     */
    private void setVoca() {

        final VocaDAO vocaDAO = new VocaDAO(context);
        ArrayList<VoVoca> vocaLists =  vocaDAO.selectAll();

        if(vocaLists.size() < 0) return;

        String jsonData = GlobalApplication.getGson().toJson(vocaLists);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonData);
        }catch (Exception e){
            LogUtil.e(e.toString());
        }

        AndroidNetworkRequest.getInstance(context).postJSONArrayRequest(ConstCommURL.REQUEST_SET_VOCA,
                ConstCommURL.getApiUrl(ConstCommURL.REQUEST_SET_VOCA),
                jsonArray,
                new AndroidNetworkRequest.ListenerAndroidResponse() {

                    @Override
                    public void success(String response) {
                        LogUtil.i("response : " +  response);
                        vocaDAO.deleteAll();
                        dataSyncListener.success(response);
                    }

                    @Override
                    public void systemcheck(String response) {
                        dataSyncListener.fail();
                    }

                    @Override
                    public void fail(VoBase base) {
                        dataSyncListener.fail();
                    }

                    @Override
                    public void exception(ANError error) {
                        dataSyncListener.fail();
                    }
                });
    }

    private void testRequest() {
        String url = ConstCommURL.getAppUrl(ConstCommURL.REQUEST_GET_MAXAPPVER);

        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("APPID","1");
        AndroidNetworkRequest.getInstance(context).StringRequest(ConstCommURL.REQUEST_TAG_MAXAPPVER,
                builder.toString(),
                new AndroidNetworkRequest.ListenerAndroidResponse() {

                    @Override
                    public void success(String response) {
                        dataSyncListener.success(response);
                    }

                    @Override
                    public void systemcheck(String response) {
                        dataSyncListener.fail();
                    }

                    @Override
                    public void fail(VoBase base) {
                        dataSyncListener.fail();
                    }

                    @Override
                    public void exception(ANError error) {
                        dataSyncListener.fail();
                    }
                });

    }
}













