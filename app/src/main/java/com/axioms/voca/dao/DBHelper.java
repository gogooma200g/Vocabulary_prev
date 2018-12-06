package com.axioms.voca.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.axioms.voca.util.LogUtil;

/**
 * Created by kiel1 on 2018-11-26.
 */

public class DBHelper extends SQLiteOpenHelper {

    //DB name
    private static final String DB_NAME = "vocabulary.db";

    //DB Version
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        LogUtil.d("SQLiteDB Create");
        createVocaTable(database);
        createVocaListTable(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.d("oldVersion : " + oldVersion);
        switch (oldVersion) {
            case 1 :
                db.execSQL(deleteVocaTable());
                db.execSQL(deleteVocaListTable());
                onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * create VocaTable
     * I : insert , U : update, D : delete
     */
    private void createVocaTable(SQLiteDatabase db) {
        LogUtil.d("createVocaDataTable");
        StringBuffer query = new StringBuffer();
        query.append("Create table IF NOT EXISTS " + DaoColumns.VocaColumns.T_VOCABULARY + " ( ");
        query.append(DaoColumns.VocaColumns.C_ID	    + " VARCHAR (10) PRIMARY KEY AUTOINCREMENT, ");
        query.append(DaoColumns.VocaColumns.C_VOCAID	+ " VARCHAR (10)        , ");
        query.append(DaoColumns.VocaColumns.C_LIST_ID   + " VARCHAR (10) NOT NULL, ");
        query.append(DaoColumns.VocaColumns.C_WORD	    + " VARCHAR (200) NOT NULL, ");
        query.append(DaoColumns.VocaColumns.C_MEAN      + " VARCHAR (200) NOT NULL, ");
        query.append(DaoColumns.VocaColumns.C_MEMO_YN   + " VARCHAR (1) NOT NULL, ");
        query.append(DaoColumns.VocaColumns.C_TYPE      + " VARCHAR (1) NOT NULL)");
        db.execSQL(query.toString());
    }

    private String deleteVocaTable() {
        return "DROP TABLE IF EXISTS " + DaoColumns.VocaColumns.T_VOCABULARY;
    }

    /**
     * create VocaListTable
     * I : insert , U : update, D : delete
     */
    private void createVocaListTable(SQLiteDatabase db) {
        LogUtil.d("create VocaListTable");
        StringBuffer query = new StringBuffer();
        query.append("Create table IF NOT EXISTS " + DaoColumns.VocaListColumns.T_VOCABULARY_LIST + " ( ");
        query.append(DaoColumns.VocaListColumns.C_ID	        + " VARCHAR (10) PRIMARY KEY AUTOINCREMENT, ");
        query.append(DaoColumns.VocaListColumns.C_LIST_ID       + " VARCHAR (10) , ");
        query.append(DaoColumns.VocaListColumns.C_TITLE	        + " VARCHAR (100) NOT NULL, ");
        query.append(DaoColumns.VocaListColumns.C_TYPE          + " VARCHAR (1) NOT NULL)");
        db.execSQL(query.toString());
    }

    private String deleteVocaListTable() {
        return "DROP TABLE IF EXISTS " + DaoColumns.VocaListColumns.T_VOCABULARY_LIST;
    }
}
