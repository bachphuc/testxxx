package com.learn.mobile.library.dmobi.helper.DbHelperLib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.learn.mobile.library.dmobi.global.Constant;

/**
 * Created by 09520_000 on 1/10/2016.
 */
public class DbAdapter extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = Constant.DATABASE_OFFLINE;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SearchReader.SearchEntry.TABLE_NAME + " (" +
                    SearchReader.SearchEntry._ID + " INTEGER PRIMARY KEY," +
                    SearchReader.SearchEntry.COLUMN_ITEM_TYPE + TEXT_TYPE + COMMA_SEP +
                    SearchReader.SearchEntry.COLUMN_ITEM_ID + INT_TYPE + COMMA_SEP +
                    SearchReader.SearchEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    SearchReader.SearchEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SearchReader.SearchEntry.TABLE_NAME;

    public DbAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}