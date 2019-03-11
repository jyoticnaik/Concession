package com.example.dell.concession;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DetailsDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "concession.db";
    public static final String TABLE_NAME1="details1";
    public static final String TABLE_NAME2="details2";

    public static final String NAME="name";
    public static final String GENDER="gender";
    public static final String DATE_OF_BIRTH="date_of_birth";
    public static final String ADDRESS="address";
    public static final String PINCODE="pincode";
    public static final String COURSE="course";
    public static final String YEAR="year";
    public static final String EMAIL="emailid";
    public static final String PASSINT="passint";
    public static final String DEST="dest";
    public static final String SOURCE="source";
    public static final String PPSD="ppsd";
    public static final String PPED="pped";
    public static final String COND="cond";

    public DetailsDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DetailsDb","INSIDE");
        String createTable1="create table " +TABLE_NAME1
                +   "("+NAME+" TEXT"
                +   ","+GENDER+" TEXT"
                +   ","+DATE_OF_BIRTH+" TEXT"
                +   ","+ADDRESS+" TEXT"
                +   ","+PINCODE+" TEXT"
                +   ","+COURSE+" TEXT"
                +   ","+YEAR+" TEXT"
                +   ","+EMAIL+" TEXT"
                +   ")";
        db.execSQL(createTable1);

        String createTable2="create table "+TABLE_NAME2
                +   "("+PASSINT+" TEXT"
                +   ","+DEST+" TEXT"
                +   ","+SOURCE+" TEXT"
                +   ","+PPSD+" TEXT"
                +   ","+PPED+" TEXT"
                +   ","+COND+" TEXT"
                +   ")";
        db.execSQL(createTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        onCreate(db);
    }

    public boolean insertData1(String name, String gender,String dob,String address,String pincode,String course,String year,String emailid){
        SQLiteDatabase db=this.getWritableDatabase();
        onCreate(db);

        ContentValues contentValues=new ContentValues();
        contentValues.put(NAME,name);
        contentValues.put(GENDER,gender);
        contentValues.put(DATE_OF_BIRTH,dob);
        contentValues.put(ADDRESS,address);
        contentValues.put(PINCODE,pincode);
        contentValues.put(COURSE,course);
        contentValues.put(YEAR,year);
        contentValues.put(EMAIL,emailid);


        long result = db.insert(TABLE_NAME1,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertData2(String passint,String dest,String source,String ppsd,String pped,String cond){
        SQLiteDatabase db=this.getWritableDatabase();
        onCreate(db);

        ContentValues contentValues=new ContentValues();
        contentValues.put(PASSINT,passint);
        contentValues.put(DEST,dest);
        contentValues.put(SOURCE,source);
        contentValues.put(PPSD,ppsd);
        contentValues.put(PPED,pped);
        contentValues.put(COND,cond);

        long result = db.insert(TABLE_NAME2,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData1(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res1=db.rawQuery("select * from "+TABLE_NAME1,null);
        return res1;
    }

    public Cursor getAllData2(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res2=db.rawQuery("select * from "+TABLE_NAME2,null);
        return res2;
    }

}
