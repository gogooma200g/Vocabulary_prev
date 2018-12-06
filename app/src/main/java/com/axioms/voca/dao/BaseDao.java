package com.axioms.voca.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kiel1 on 2018-11-30.
 */

class BaseDao {

    protected DBHelper dbHelper;
    protected String tableName;
    protected Context mContext;

    public BaseDao(Context context) {
        this.mContext = context;
        this.dbHelper = new DBHelper(context);
    }

    protected SQLiteDatabase getReadableConnection() {
        return dbHelper.getReadableDatabase();
    }

    protected SQLiteDatabase getWritableConnection() {
        return dbHelper.getWritableDatabase();
    }

    protected void closeConnection(SQLiteDatabase db) {
        db.close();
    }

    protected void executeSql(String sql) {
        SQLiteDatabase con = getWritableConnection();
        con.execSQL(sql);
        //closeConnection(con);
    }

    public void close() {
        dbHelper.close();
    }
}
