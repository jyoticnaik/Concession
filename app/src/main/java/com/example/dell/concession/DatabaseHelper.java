package com.example.dell.concession;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "concession.db";
    public static final String TABLE_NAME="aadhaar_info";
    public static final String UID="uid";
    public static final String NAME="name";
    public static final String GENDER="gender";
    public static final String YEAR_OF_BIRTH="year_of_birth";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="create table " +TABLE_NAME
                +   "("+UID
                +   ","+NAME
                +   ","+GENDER
                +   ","+YEAR_OF_BIRTH
                +   ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String uid,String name, String gender,String yob){
        SQLiteDatabase db=this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

        ContentValues contentValues=new ContentValues();
        contentValues.put(UID,uid);
        contentValues.put(NAME,name);
        contentValues.put(GENDER,gender);
        contentValues.put(YEAR_OF_BIRTH,yob);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res1=db.rawQuery("select * from "+TABLE_NAME,null);
        return res1;
    }

    public String getUID(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res2 = db.rawQuery("select "+UID+" from "+TABLE_NAME,null);
        return res2.getString(0);
    }
}
