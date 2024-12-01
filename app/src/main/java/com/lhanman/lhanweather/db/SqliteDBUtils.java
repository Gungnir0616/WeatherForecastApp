package com.lhanman.lhanweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户增删改查
 */
public class SqliteDBUtils {

    public static final String DB_NAME = "sqlite_dbname";

    public static final int VERSION = 1;
 
    private static SqliteDBUtils sqliteDB;
 
    private SQLiteDatabase db;
 
    private SqliteDBUtils(Context context) {
        OpenHelper dbHelper = new OpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }
 
    /**
     * ��ȡSqliteDBʵ��
     * @param context
     */
    public synchronized static SqliteDBUtils getInstance(Context context) {
        if (sqliteDB == null) {
            sqliteDB = new SqliteDBUtils(context);
        }
        return sqliteDB;
    }
 
    
    public void delete(Context context,String username) {
        OpenHelper dbHelper = new OpenHelper(context, DB_NAME, null, VERSION);
    	 db = dbHelper.getReadableDatabase();
    	 db.delete("User", "id=?", new String[] { username });
	}
    
    public void change(Context context, User user) {
    	   OpenHelper dbHelper = new OpenHelper(context, DB_NAME, null, VERSION);
      	 db = dbHelper.getWritableDatabase();
      	ContentValues values = new ContentValues();
      	values.put("id", user.getId());
        values.put("username", user.getUsername());
        values.put("userpwd", user.getUserpwd());
        values.put("head_url", user.getHead_url());
      	 db.update("User", values, "id=?", new String[]{user.getId()+""});
	}


    public int  saveUser(User user) {
        if (user != null) {
 
            Cursor cursor = db.rawQuery("select * from User where username=?", new String[]{user.getUsername().toString()});
            if (cursor.getCount() > 0) {
                return -1;
            } else {
                try {
                    db.execSQL("insert into User(username,head_url,userpwd) values(?,?,?) ", new String[]{
                            user.getUsername().toString(),
                            user.getHead_url(),
                            user.getUserpwd().toString()});
                } catch (Exception e) {
                    Log.d("����", e.getMessage().toString());
                }
                return 1;
            }
        }
        else {
            return 0;
        }
    }
 
    /**
     * �����ݿ��ȡUser��Ϣ��
     */
    public List<User> loadUser() {
        List<User> list = new ArrayList<User>();
        Cursor cursor = db
                .query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setUsername(cursor.getString(cursor
                        .getColumnIndex("username")));
                user.setUserpwd(cursor.getString(cursor
 
                        .getColumnIndex("userpwd")));
                list.add(user);
            } while (cursor.moveToNext());
        }
        return list;
    }
    public User loadUserByName(String name) {
        User list = null;
        Cursor cursor = db
                .query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (name.equals(cursor.getString(cursor.getColumnIndex("username")))){
                    list = new User();
                    list.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    list.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                    list.setHead_url(cursor.getString(cursor.getColumnIndex("head_url")));
                    list.setUserpwd(cursor.getString(cursor .getColumnIndex("userpwd")));
                    return list;
                }

            } while (cursor.moveToNext());
        }
        return list;
    }


    public int Quer(String pwd,String name)
    {
 
 
        HashMap<String,String> hashmap=new HashMap<String,String>();
        Cursor cursor =db.rawQuery("select * from User where username=?", new String[]{name});
 
       // hashmap.put("name",db.rawQuery("select * from User where name=?",new String[]{name}).toString());
        if (cursor.getCount()>0)
        {
            Cursor pwdcursor =db.rawQuery("select * from User where userpwd=? and username=?",new String[]{pwd,name});
            if (pwdcursor.getCount()>0)
            {
                return 1;
            }
            else {
                return -1;
            }
        }
        else {
            return 0;
        }
 
 
    }
    public int selectId(String pwd,String name)
    {

        int id = 0;
        Cursor cursor = db
                .query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if(name.equals(cursor.getString(cursor
                        .getColumnIndex("username"))) && pwd.equals(cursor

                        .getColumnIndex("userpwd"))){
                    return cursor.getInt(cursor.getColumnIndex("id"));
                }
            } while (cursor.moveToNext());
        }
        return id;

    }
}