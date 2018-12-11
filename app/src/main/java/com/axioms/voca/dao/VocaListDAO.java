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

public class VocaListDAO extends BaseDao {

    private static VocaListDAO instance;

    public static VocaListDAO getInstance(Context context) {
        if(instance == null) {
            synchronized (VocaListDAO.class) {
                instance = new VocaListDAO(context);
            }
        }
        return instance;
    }

    public VocaListDAO(Context context) {
        super(context);
        this.tableName = DaoColumns.VocaListColumns.T_VOCABULARY_LIST;
    }

    /**
     * insert
     */
    public void insert(VoVocaList info) {
        SQLiteDatabase db = getWritableConnection();

        ContentValues values = new ContentValues();
        values.put(DaoColumns.VocaListColumns.C_TITLE, info.getTITLE());
        values.put(DaoColumns.VocaListColumns.C_TYPE, "I");

        long newRowId = db.insert(DaoColumns.VocaListColumns.T_VOCABULARY_LIST, null, values);
        if(newRowId == -1) {
            ToastUtil.show(mContext, "VocaList insert is Failed");
        }
    }

    /**
     * select All
     */
    public ArrayList<VoVocaList> selectAll() {

        SQLiteDatabase db = getReadableConnection();

        Cursor cursor = db.query(DaoColumns.VocaListColumns.T_VOCABULARY_LIST,
                null, null, null, null, null, null);

        ArrayList<VoVocaList> vocaList = new ArrayList<>();

        while (cursor.moveToNext()) {

            VoVocaList voVoca = new VoVocaList();
            for(int i = 0; i < cursor.getColumnCount(); i++) {
                switch (cursor.getColumnName(i)) {
                    case DaoColumns.VocaListColumns.C_LIST_ID :
                        voVoca.setLISTID(cursor.getString(i));
                        break;
                    case DaoColumns.VocaListColumns.C_TITLE :
                        voVoca.setTITLE(cursor.getString(i));
                        break;
                    case DaoColumns.VocaListColumns.C_TYPE :
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
    public void update(VoVocaList info) {

        boolean isInsert = false;

        if(!StringUtil.isNull(info.getTYPE())) {
            isInsert = "I".equals(info.getTYPE());
        }

        SQLiteDatabase db = getWritableConnection();

        ContentValues values = new ContentValues();
        values.put(DaoColumns.VocaListColumns.C_LIST_ID, info.getLISTID());
        values.put(DaoColumns.VocaListColumns.C_TITLE, info.getTITLE());
        values.put(DaoColumns.VocaListColumns.C_TYPE, isInsert ?  "I" : "U");

        String selection = (isInsert ? DaoColumns.VocaListColumns.C_ID : DaoColumns.VocaListColumns.C_LIST_ID ) + "= ?";
        String[] selectionArgs = {isInsert ? info.getID() : info.getLISTID()};

        int count = db.update(DaoColumns.VocaListColumns.T_VOCABULARY_LIST,
                values,
                selection,
                selectionArgs);

        LogUtil.i("update : " + count);
    }

    /**
     * delete All
     */
    public void deleteAll() {
        SQLiteDatabase db = getWritableConnection();
        db.delete(DaoColumns.VocaListColumns.T_VOCABULARY_LIST, null, null);
    }

    /**
     * delete
     */
    public void delete(VoVocaList voca) {
        SQLiteDatabase db = getWritableConnection();

        String selection = DaoColumns.VocaListColumns.C_ID + " = ?";
        String [] selectionArgs = {voca.getID()};
        int deletedRows = db.delete(DaoColumns.VocaListColumns.T_VOCABULARY_LIST, selection, selectionArgs);

        LogUtil.i("deleteRows : " + deletedRows);
    }


}
