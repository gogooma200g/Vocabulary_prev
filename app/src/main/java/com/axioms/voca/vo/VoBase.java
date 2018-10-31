package com.axioms.voca.vo;


/**
 * Created by 김민정 on 2018-03-28.
 */

public class VoBase {
    public static final int RES_SERVER_ERROR = -1;     //서버 장애
    public static final int RES_SYSTEM = 0;            //시스템 점검
    public static final int RES_SUCCESS = 1;           //성공

    public String RES_MSG;
    public int RES_CODE = 1;
    public String RSLT_COMMAND;

    public String getRES_MSG() {
        return RES_MSG;
    }

    public void setRES_MSG(String RES_MSG) {
        this.RES_MSG = RES_MSG;
    }

    public int getRES_CODE() {
        return RES_CODE;
    }

    public void setRES_CODE(int RES_CODE) {
        this.RES_CODE = RES_CODE;
    }

    public String getRSLT_COMMAND() { return RSLT_COMMAND; }

    public void setRSLT_COMMAND(String RSLT_COMMAND) { this.RSLT_COMMAND = RSLT_COMMAND; }

    public boolean isSuccess() {
        if(RES_CODE == RES_SUCCESS) return true;
        return false;
    }

    public boolean isSystemCheck() {
        if(RES_CODE == RES_SYSTEM) return true;
        return false;
    }

    public boolean isServerError() {
        if(RES_CODE == RES_SERVER_ERROR) return true;
        return false;
    }
}
