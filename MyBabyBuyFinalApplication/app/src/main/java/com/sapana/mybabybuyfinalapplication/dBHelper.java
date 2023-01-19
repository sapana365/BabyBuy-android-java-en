package com.sapana.mybabybuyfinalapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dBHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String TABLENAME = "Items";
    public dBHelper(Context context) {
        super(context, "babyBuyFinall.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table users(inputUsername TEXT primary key, inputPassword TEXT, inputEmail TEXT )");
        db.execSQL ("create Table " + TABLENAME + "(id integer primary key autoincrement, name text, price varchar, location varchar, details varchar,isPurchased varchar, avatar blob)");
        db.execSQL("create Table feedbacks(id integer primary key, email TEXT, messsage TEXT )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists users");
        db.execSQL( "drop table if exists " + TABLENAME + "");
        db.execSQL("drop Table if exists feedbacks");
        onCreate(db);
    }
    public Boolean insertfeedbackdata(String email, String message)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("messsage", message);
        long result=DB.insert("feedbacks", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public boolean insertData(String inputUsername,  String inputEmail,String inputPassword){
        SQLiteDatabase MyDB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("inputUsername", inputUsername);

        contentValues.put("inputEmail", inputEmail);
        contentValues.put("inputPassword", inputPassword);

        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;

    }

    public Boolean checkusername(String inputUsername){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where inputUsername = ?", new String[] {inputUsername});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public Boolean checkemail(String inputEmail){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where inputEmail = ?", new String[] {inputEmail});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public  Boolean checkpasswordemail(String inputEmail, String inputPassword){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor =MyDB.rawQuery("Select * from users where  inputEmail = ? and inputPassword = ?", new String[] {inputEmail,inputPassword});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
