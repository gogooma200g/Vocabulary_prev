package com.axioms.voca.base;

/**
 * Created by 김민정 on 2018-04-02.
 */

public class ConstCommURL {

    public static boolean ISTEST = false;

    //DOMAIN
    public static final String APP_DOMAIN = "http://api.axioms.co.kr/";
    public static final String TEST_DOMAIN = "http://dev.api.axioms.co.kr/";

    //URL
    public final static String URL_API = "api/";
    public final static String URL_USER = "User/";
    public final static String URL_STD = "Std/";
    public final static String URL_APP= "App/";
    public final static String URL_PAY= "Pay/";

    public final static String USL_UPLOAD= "Upload/";


    //================================== 파일 관련 ==================================//
    public static final String DATA_PATH = "data/data/";
    public static final String DATA_FILE = "/files/";


    //==================================== API ====================================//
    //공통
    public final static String REQUEST_GET_MAXAPPVER = "GetMaxAppVer";                  //앱버전 체크


    public final static String REQUEST_SET_VOCALIST = "SetVocaList";               //단어장 리스트 저장
    public final static String REQUEST_SET_VOCA     = "SetVocaList";               //단어 리스트 저장



    //==================================== TAG ====================================//
    //공통
    public final static String REQUEST_TAG_MAXAPPVER = "TagMaxAppVer";                      //앱버전 체크

    public final static String REQUEST_TAG_VOCALIST = "TagMaxAppVer";               //단어장 리스트
    public final static String REQUEST_TAG_VOCA     = "TagMaxAppVer";               //단어 리스트 저장



    public static String getApiUrl(String request){
        return (ISTEST ? TEST_DOMAIN : APP_DOMAIN) + URL_API  + request;
    }

    /** 회원 **/
    public static String getUserUrl(String request){
        return (ISTEST ? TEST_DOMAIN : APP_DOMAIN) + URL_USER  + request;
    }

    /** 학습 **/
    public static String getStdUrl(String request){
        return (ISTEST ? TEST_DOMAIN : APP_DOMAIN) + URL_STD  + request;
    }

    /** 앱 **/
    public static String getAppUrl(String request){
        return (ISTEST ? TEST_DOMAIN : APP_DOMAIN) + URL_APP  + request;
    }

    /** 결제 **/
    public static String getPayUrl(String request){
        return (ISTEST ? TEST_DOMAIN : APP_DOMAIN) + URL_APP + request;
    }
    /** 업로드 **/
    public static String getUploadUrl(String request){
        return (ISTEST ? TEST_DOMAIN : APP_DOMAIN) + USL_UPLOAD  + request;
    }

    public static String getFilePath(String path){
        return DATA_PATH + GlobalApplication.getInstance().getPackageName() + DATA_FILE + path;
    }

}
