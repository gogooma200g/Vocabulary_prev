package com.axioms.voca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.axioms.voca.util.LogUtil;
import com.axioms.voca.util.StringUtil;
import com.axioms.voca.util.ToastUtil;
import com.axioms.voca.vo.VoVoca;
import com.axioms.voca.vo.VoVocaList;

import java.util.ArrayList;

/**
 * Created by kiel1 on 2018-11-26.
 */

public class VocaDAO extends BaseDao {

    private static VocaDAO instance;

    public static VocaDAO getInstance(Context context) {
        if(instance == null) {
            synchronized (VocaDAO.class) {
                instance = new VocaDAO(context);
            }
        }
        return instance;
    }

    public VocaDAO(Context context) {
        super(context);
        this.tableName = DaoColumns.VocaColumns.T_VOCABULARY;
    }

    /**
     * insert
     */
    public void insert(VoVoca info) {
        SQLiteDatabase db = getWritableConnection();

        ContentValues values = new ContentValues();
        values.put(DaoColumns.VocaColumns.C_LIST_ID, info.getLIST_ID());
        values.put(DaoColumns.VocaColumns.C_VOCAID, info.getVOCA_ID());
        values.put(DaoColumns.VocaColumns.C_WORD, info.getWORD());
        values.put(DaoColumns.VocaColumns.C_MEAN, info.getMEAN());
        values.put(DaoColumns.VocaColumns.C_MEMO_YN, info.getMEMO_YN());
        values.put(DaoColumns.VocaColumns.C_TYPE, info.getTYPE());

        long newRowId = db.insert(DaoColumns.VocaColumns.T_VOCABULARY, null, values);
        if(newRowId == -1) {
            ToastUtil.show(mContext, "VocaList insert is Failed");
        }
    }
//    public void insert(VoVoca info) {
//        String query = "INSERT INTO " + tableName + "("+ DaoColumns.VocaColumns.C_LIST_ID + ", "
//                                                    + DaoColumns.VocaColumns.C_WORD + ", "
//                                                    + DaoColumns.VocaColumns.C_MEAN + ", "
//                                                    + DaoColumns.VocaColumns.C_MEMO_YN + ", "
//                                                    + DaoColumns.VocaColumns.C_TYPE + ") VALUES('"
//                + info.getLIST_ID() + "', '" + info.getWORD() + "', '"
//                + info.getMEAN() + "', '" + info.getMEMO_YN() + "', 'I')";
//        executeSql(query);
//    }

    /**
     * select All
     */
    public ArrayList<VoVoca> selectAll() {

        SQLiteDatabase db = getReadableConnection();

        Cursor cursor = db.query(DaoColumns.VocaColumns.T_VOCABULARY,
                null, null, null, null, null, null);

        ArrayList<VoVoca> vocaList = new ArrayList<>();

        while (cursor.moveToNext()) {

            VoVoca voVoca = new VoVoca();

            for(int i = 0; i < cursor.getColumnCount(); i++) {

                switch (cursor.getColumnName(i)) {

                    case DaoColumns.VocaColumns.C_ID :
                        voVoca.setID(cursor.getString(i));
                        break;
                    case DaoColumns.VocaColumns.C_WORD :
                        voVoca.setWORD(cursor.getString(i));
                        break;
                    case DaoColumns.VocaColumns.C_MEAN :
                        voVoca.setMEAN(cursor.getString(i));
                        break;
                    case DaoColumns.VocaColumns.C_MEMO_YN :
                        voVoca.setMEMO_YN(cursor.getString(i));
                        break;
                    case DaoColumns.VocaColumns.C_TYPE :
                        voVoca.setTYPE(cursor.getString(i));
                        break;
                }
            }
            vocaList.add(voVoca);
        }
        cursor.close();
        return vocaList;
    }

    /**
     * update
     */
    public void update(VoVoca info) {

        boolean isInsert = false;

        if(!StringUtil.isNull(info.getTYPE())) {
            isInsert = "I".equals(info.getTYPE());
        }

        SQLiteDatabase db = getWritableConnection();

        ContentValues values = new ContentValues();
        values.put(DaoColumns.VocaColumns.C_VOCAID, info.getVOCA_ID());
        values.put(DaoColumns.VocaColumns.C_LIST_ID, info.getLIST_ID());
        values.put(DaoColumns.VocaColumns.C_WORD, info.getWORD());
        values.put(DaoColumns.VocaColumns.C_MEAN, info.getMEAN());
        values.put(DaoColumns.VocaColumns.C_MEMO_YN, info.getMEMO_YN());
        values.put(DaoColumns.VocaColumns.C_TYPE, isInsert ?  "I" : "U");

        String selection = (isInsert ? DaoColumns.VocaColumns.C_ID : DaoColumns.VocaColumns.C_VOCAID ) + "= ?";
        String[] selectionArgs = {isInsert ? info.getID() : info.getVOCA_ID()};

        int count = db.update(DaoColumns.VocaListColumns.T_VOCABULARY_LIST,
                values,
                selection,
                selectionArgs);

        LogUtil.i("update : " + count);
    }

//    public void updata(VoVoca voca) {
//        String query = "UPDATE " + tableName +
//                " SET " + DaoColumns.VocaColumns.C_WORD + " = '" + voca.getWORD() + "', " +
//                " SET " + DaoColumns.VocaColumns.C_MEAN +  " = '" + voca.getMEAN() + "', " +
//                " SET " + DaoColumns.VocaColumns.C_MEMO_YN +  " = '" + voca.getMEMO_YN() + "', " +
//                " SET " + DaoColumns.VocaColumns.C_TYPE + " = 'U'" +
//                " WHERE "  + DaoColumns.VocaColumns.C_ID + " = '" + voca.getID() + "' "+
//                            DaoColumns.VocaColumns.C_LIST_ID + " = '" + voca.getLIST_ID() + "'";
//        executeSql(query);
//    }

    /**
     * delete All
     */
    public void deleteAll() {
        SQLiteDatabase db = getWritableConnection();
        db.delete(DaoColumns.VocaColumns.T_VOCABULARY, null, null);
    }

    /**
     * delete
     */
    public void delete(VoVoca info) {
        SQLiteDatabase db = getWritableConnection();
        boolean isInsert = "I".equals(info.getTYPE());

        if(isInsert) {
            String selection = DaoColumns.VocaColumns.C_LIST_ID + " = ?";
            String [] selectionArgs = {info.getLIST_ID()};
            int deletedRows = db.delete(DaoColumns.VocaColumns.T_VOCABULARY, selection, selectionArgs);

            LogUtil.i("deleteRows : " + deletedRows);
            return;
        }

//        String selection = DaoColumns.VocaListColumns.C_LIST_ID + " = ?";
//        String [] selectionArgs = {info.getLIST_ID()};
//        int deletedRows = db.delete(DaoColumns.VocaColumns.T_VOCABULARY, selection, selectionArgs);

        ContentValues values = new ContentValues();
        values.put(DaoColumns.VocaColumns.C_LIST_ID, info.getLIST_ID());
        values.put(DaoColumns.VocaColumns.C_LIST_ID, info.getVOCA_ID());
        values.put(DaoColumns.VocaColumns.C_TYPE, "D");

        long newRowId = db.insert(DaoColumns.VocaColumns.T_VOCABULARY, null, values);

        if(newRowId == -1) {
            ToastUtil.show(mContext, "Voca delete fail");
        }
    }

    //public void update

//    public void delete(VoVoca voca) {
//        String query = "DELETE FROM " + tableName +
//                "WHERE "+ DaoColumns.VocaColumns.C_ID + " = '" + voca.getID() + "' AND " +
//                DaoColumns.VocaColumns.C_LIST_ID + " = '" + voca.getLIST_ID() + "'";
//        executeSql(query);
//    }

    /**
     * close
     */
    public void close() {
        dbHelper.close();
    }

}
