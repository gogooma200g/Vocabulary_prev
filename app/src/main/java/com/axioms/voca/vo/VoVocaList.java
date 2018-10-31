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

    public class VoVoca {
        private int NUM;
        private String WORD;
        private String MEAN;
        private String MEMO_YN;
        private String CONTS;

        public int getNUM() {
            return NUM;
        }

        public void setNUM(int NUM) {
            this.NUM = NUM;
        }

        public String getWORD() {
            return WORD;
        }

        public void setWORD(String WORD) {
            this.WORD = WORD;
        }

        public String getMEAN() {
            return MEAN;
        }

        public void setMEAN(String MEAN) {
            this.MEAN = MEAN;
        }

        public String getMEMO_YN() {
            return MEMO_YN;
        }

        public void setMEMO_YN(String MEMO_YN) {
            this.MEMO_YN = MEMO_YN;
        }

        public String getCONTS() {
            return CONTS;
        }

        public void setCONTS(String CONTS) {
            this.CONTS = CONTS;
        }
    }
}
