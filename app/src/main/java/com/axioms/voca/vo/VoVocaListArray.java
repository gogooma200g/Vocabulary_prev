package com.axioms.voca.vo;

import java.util.ArrayList;

/**
 * Created by kiel1 on 2018-10-19.
 */

public class VoVocaListArray extends VoBase {

    private volatile static VoVocaListArray instance;
    private ArrayList<VoVocaList> VOCALIST_LIST = new ArrayList<>();

    public VoVocaListArray() {
        instance = this;
    }

    public static VoVocaListArray getInstance() {
        if(instance == null) {
            synchronized (VoVocaListArray.class) {
                instance = new VoVocaListArray();
            }
        }
        return instance;
    }

    public ArrayList<VoVocaList> getVOCALIST_LIST() {
        return VOCALIST_LIST;
    }

    public void setVOCALIST_LIST(ArrayList<VoVocaList> VOCALIST_LIST) {
        this.VOCALIST_LIST = VOCALIST_LIST;
    }
}
