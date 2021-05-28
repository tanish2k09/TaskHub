package com.tanish2k09.taskhub.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper{

    public SQLiteDatabase db;
    private static final String DATABASE_NAME = "Notes.db";
    public static final String TABLE_NAME = "main_table";
    public static final String ID = "ID";
    private static final String TITLE = "TITLE";
    private static final String TEXT = "TEXT";
    private Cursor cursor = null;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = getWritableDatabase();
        db.execSQL("create table if not exists "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE VARCHAR(20),TEXT VARCHAR)");
    }

    public int writeVal(String title, String text)
    {
        int id = -1;
        db.execSQL("INSERT INTO "+TABLE_NAME+" ("+TITLE+","+TEXT+") VALUES ('" + title + "','" + text + "');");
        cursor = db.rawQuery("select "+ID+" from "+TABLE_NAME+" where "+ID+" = (select max(" + ID + ") from "+TABLE_NAME+");", null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
                id = cursor.getInt(cursor.getColumnIndex(ID));
            cursor.close();
        }
        return id;
    }

    public void writeInitVal(int id, String title, String text) {
        db.execSQL("INSERT INTO "+TABLE_NAME+" ("+ID+","+TITLE+","+TEXT+") VALUES ("+id+",'" + title + "','" + text + "');");
    }

    public void updateVal(int id, String title, String text) {
        //db.execSQL("UPDATE "+TABLE_NAME+" SET "+TITLE+" = '" + title + "',"+TEXT+" = '" + text + "' WHERE "+ID+" = " + id + ";");
        ContentValues cv = new ContentValues();
        cv.put(TITLE,title);
        cv.put(TEXT,text);
        db.update(TABLE_NAME,cv,ID+"="+id,null);
    }

    public String getTitleFromID(int id)
    {
        String title = "";
        cursor = db.rawQuery("select "+TITLE+" from "+TABLE_NAME+" where "+ID+" = " + id + ";", null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                title = cursor.getString(cursor.getColumnIndex(TITLE));
            }
            cursor.close();
        }
        return title;
    }

    public String getTextFromID(int id) {
        String text = "";
        cursor = db.rawQuery("select "+TEXT+" from "+TABLE_NAME+" where "+ID+" = " + id + ";", null);
        if (cursor != null) {
            if (cursor.moveToFirst())
            {
                text = cursor.getString(cursor.getColumnIndex(TEXT));
            }
            cursor.close();
        }
        return text;
    }

    public void removeNote(int id)
    {
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+ID+" = "+id+";");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
