package com.sunzhongyang.sjd.lab_project_eight;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDB extends SQLiteOpenHelper
{
    //设置数据库的基本信息
    private static final String DB_NAME = "database";
    private static final String TABLE_NAME = "birthday";
    private static final int DB_VERSION = 1;

    //默认初始化
    public myDB(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //创建数据库
        String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME + " (_id INTEGER PRIMARY KEY,name TEXT,birth TEXT,gift TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int il)
    {

    }

    public SQLiteDatabase  get_db()
    {
        //返回一个可写的数据库
        SQLiteDatabase db = getWritableDatabase();
        return db;
    }
}