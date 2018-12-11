package com.axioms.voca.vo;

import java.util.ArrayList;

/**
 * Created by kiel1 on 2018-10-19.
 */

public class VoVocaList extends VoBase {

    private String ID;
    private String LISTID;
    private String TITLE;
    private int TOTAL_CNT;
    private String TYPE;
    private ArrayList<VoVoca> VOCA_LIST = new ArrayList<>();
    private boolean isModifying = false;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLISTID() {
        return LISTID;
    }

    public void setLISTID(String LISTID) {
        this.LISTID = LISTID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public int getTOTAL_CNT() {
        return TOTAL_CNT;
    }

    public void setTOTAL_CNT(int TOTAL_CNT) {
        this.TOTAL_CNT = TOTAL_CNT;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public ArrayList<VoVoca> getVOCA_LIST() {
        return VOCA_LIST;
    }

    public void setVOCA_LIST(ArrayList<VoVoca> VOCA_LIST) {
        this.VOCA_LIST = VOCA_LIST;
    }

    public boolean isModifying() {
        return isModifying;
    }

    public void setModifying(boolean modifying) {
        isModifying = modifying;
    }
}
