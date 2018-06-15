package com.zqy.sqlitedemo.db;

import android.content.Context;

/**
 * Created by zhaoqy on 2018/6/14.
 */

public class DBManager {

    public static final String DB_NAME = "sqlite_demo";
    public static final String TABLE_NAME = "user";

    public static SqliteHelper getSqliteHelper(Context context) {
        SqliteHelper helper = new SqliteHelper(context, DB_NAME);
        return helper;
    }

    public static SqliteHelper getSqliteHelper(Context context, int version) {
        SqliteHelper helper = new SqliteHelper(context, DB_NAME, version);
        return helper;
    }
}
