package com.lhanman.lhanweather.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 中药材数据库
 */
public class RedianHelper extends SQLiteOpenHelper {

    public static final String CREATE_USER ="create table Redian("
            + "id integer primary key autoincrement, "
            + "author text, "
            + "timest text, "
            + "biaoti text, "
            + "leixing text, "
            + "xiangqing text, "
            + "imag text)";


    public RedianHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
