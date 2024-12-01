package com.lhanman.lhanweather.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



import java.util.ArrayList;
import java.util.List;

/**
 * 中药材增删改查
 */
public class RedianDbutils {
   public static final String DB_NAME="Redian_dbname";
   public static final int VERSION=1;
   private static RedianDbutils sqliteDB;
   private SQLiteDatabase db;

   private RedianDbutils(Context context){
      RedianHelper openHelper=new RedianHelper(context,DB_NAME,null,VERSION);
       db=openHelper.getWritableDatabase();
   }
   public synchronized static RedianDbutils getInstance(Context context){
       if(sqliteDB==null){
        sqliteDB=new RedianDbutils(context);
       }
       return sqliteDB;
   }

   public void delete(Context context,String id){
       RedianHelper openHelper=new RedianHelper(context,DB_NAME,null,VERSION);
       db=openHelper.getReadableDatabase();
       db.delete("Redian","id=?",new String[]{id});
   }

    public void change(Context context, Redian medic){
        RedianHelper openHelper=new RedianHelper(context,DB_NAME,null,VERSION);
     db=openHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",medic.getId());
        values.put("author",medic.getAuthor());
        values.put("timest",medic.getTimest());
        values.put("biaoti",medic.getBiaoti());
        values.put("leixing",medic.getLeixing());
        values.put("xiangqing", medic.getXiangqing());
        values.put("imag", medic.getImag());
        db.update("Redian",values,"id=?",new String[]{medic.getId()+""});
    }

   public int insert(Redian medic){
       try {
           db.execSQL("insert into Redian(author,timest,biaoti,leixing,xiangqing,imag) values(?,?,?,?,?,?)",new String[]{
                   medic.getAuthor(),
                   medic.getTimest(),
                   medic.getBiaoti(),
                   medic.getLeixing(),
                   medic.getXiangqing(),
                   medic.getImag()
           });
           return 0;
       }catch (Exception e){
           Log.d("����", e.getMessage().toString());
           return -1;
       }
   }
    /**
     * 查询所有数据
     * @return
     */
    public List<Redian> load(){
          List<Redian>list=new ArrayList<Redian>();
          Cursor cursor=db.query("Redian",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
               Redian medic=new Redian();
               medic.setId(cursor.getInt(cursor.getColumnIndex("id")));
                medic.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                medic.setTimest(cursor.getString(cursor.getColumnIndex("timest")));
                medic.setBiaoti(cursor.getString(cursor.getColumnIndex("biaoti")));
                medic.setLeixing(cursor.getString(cursor.getColumnIndex("leixing")));
                medic.setXiangqing(cursor.getString(cursor.getColumnIndex("xiangqing")));
                medic.setImag(cursor.getString(cursor.getColumnIndex("imag")));
               Log.e("app", "medic = " + medic.toString());
               list.add(medic);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public List<Redian> loadByName(String name){
        List<Redian>list=new ArrayList<Redian>();
        Cursor cursor=db.query("Redian",null,null,null,null,null,null);
       if(cursor.moveToFirst()){
           do {
           if(cursor.getString(cursor.getColumnIndex("leixing")).indexOf(name)!=-1){
              Redian medic=new Redian();
              medic.setId(cursor.getInt(cursor.getColumnIndex("id")));
               medic.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
               medic.setTimest(cursor.getString(cursor.getColumnIndex("timest")));
               medic.setBiaoti(cursor.getString(cursor.getColumnIndex("biaoti")));
               medic.setLeixing(cursor.getString(cursor.getColumnIndex("leixing")));
               medic.setXiangqing(cursor.getString(cursor.getColumnIndex("xiangqing")));
               medic.setImag(cursor.getString(cursor.getColumnIndex("imag")));
              list.add(medic);
           }
          }while (cursor.moveToNext());
       }
        cursor.close();
       return list;
    }
    public List<Redian> loadsousuo(String name){
        List<Redian>list=new ArrayList<Redian>();
        Cursor cursor=db.query("Redian",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                if(cursor.getString(cursor.getColumnIndex("biaoti")).indexOf(name)!=-1){
                    Redian medic=new Redian();
                    medic.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    medic.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                    medic.setTimest(cursor.getString(cursor.getColumnIndex("timest")));
                    medic.setBiaoti(cursor.getString(cursor.getColumnIndex("biaoti")));
                    medic.setLeixing(cursor.getString(cursor.getColumnIndex("leixing")));
                    medic.setXiangqing(cursor.getString(cursor.getColumnIndex("xiangqing")));
                    medic.setImag(cursor.getString(cursor.getColumnIndex("imag")));
                    list.add(medic);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
