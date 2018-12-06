package com.axioms.voca.dao;

import android.provider.BaseColumns;

/**
 * Created by kiel1 on 2018-11-26.
 */

public class DaoColumns {

    /**
     * Vocabulary List
     */
    public static class VocaListColumns implements BaseColumns {
        public static final String T_VOCABULARY_LIST = "VOCABULARY_LIST";

        public static final String C_ID = "ID";
        public static final String C_LIST_ID = "LIST_ID";
        public static final String C_TITLE = "TITLE";
        public static final String C_TYPE = "TYPE";
    }

    /**
     * Vocabulary
     */
    public static class VocaColumns implements BaseColumns {
        public static final String T_VOCABULARY = "VOCABULARY";

        public static final String C_ID = "ID";
        public static final String C_VOCAID = "VOCA_ID";
        public static final String C_LIST_ID = "LIST_ID";
        public static final String C_WORD = "WORD";
        public static final String C_MEAN = "MEAN";
        public static final String C_MEMO_YN = "MEMO_YN";
        public static final String C_TYPE = "TYPE";
    }

}
