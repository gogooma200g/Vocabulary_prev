package com.axioms.voca.vo;

import java.util.ArrayList;

/**
 * Created by kiel1 on 2018-10-19.
 */

public class VoVocaListArray extends VoBase {

    private ArrayList<VoVocaList> VOCALIST_LIST = new ArrayList<>();

    public ArrayList<VoVocaList> getVOCALIST_LIST() {
        return VOCALIST_LIST;
    }

    public void setVOCALIST_LIST(ArrayList<VoVocaList> VOCALIST_LIST) {
        this.VOCALIST_LIST = VOCALIST_LIST;
    }
}
