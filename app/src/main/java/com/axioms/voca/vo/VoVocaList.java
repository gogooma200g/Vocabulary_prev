package com.axioms.voca.vo;

import java.util.ArrayList;

/**
 * Created by kiel1 on 2018-10-19.
 */

public class VoVocaList extends VoBase {

    private ArrayList<VoVoca> VOCA_LIST = new ArrayList<>();

    public ArrayList<VoVoca> getVOCA_LIST() {
        return VOCA_LIST;
    }

    public void setVOCA_LIST(ArrayList<VoVoca> VOCA_LIST) {
        this.VOCA_LIST = VOCA_LIST;
    }
}
