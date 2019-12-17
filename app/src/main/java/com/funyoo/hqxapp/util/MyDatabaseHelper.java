package com.funyoo.hqxapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    // 收藏缓存表
    public static final String CREATE_COLLECTION_ARTICLS = "create table Collection_Articls ("
            + "id integer primary key,"
            + "title text,"
            + "type text,"
            + "htmlUrl text,"
            + "count integer)";
    // 文章缓存表
    public static final String CREATE_NEWS = "create table news ("
            + "id integer primary key,"
            + "title text,"
            + "type text,"
            + "htmlUrl text,"
            + "count integer,"
            + "recommend integer,"
            + "date date)";
    public static final String CREATE_HISTORY = "create table history ("
            + "id integer primary key,"
            + "title text,"
            + "type text,"
            + "htmlUrl text,"
            + "count integer,"
            + "recommend integer,"
            + "date date)";
    public static final String CREATE_MILITARY = "create table military ("
            + "id integer primary key,"
            + "title text,"
            + "type text,"
            + "htmlUrl text,"
            + "count integer,"
            + "recommend integer,"
            + "date date)";
    public static final String CREATE_CHARACTER = "create table character ("
            + "id integer primary key,"
            + "title text,"
            + "type text,"
            + "htmlUrl text,"
            + "count integer,"
            + "recommend integer,"
            + "date date)";
    public static final String CREATE_RECOMMEND = "create table recommend ("
            + "id integer primary key,"
            + "title text,"
            + "type text,"
            + "htmlUrl text,"
            + "count integer,"
            + "recommend integer,"
            + "date date)";


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_COLLECTION_ARTICLS);
        sqLiteDatabase.execSQL(CREATE_NEWS);
        sqLiteDatabase.execSQL(CREATE_HISTORY);
        sqLiteDatabase.execSQL(CREATE_MILITARY);
        sqLiteDatabase.execSQL(CREATE_RECOMMEND);
        sqLiteDatabase.execSQL(CREATE_CHARACTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
