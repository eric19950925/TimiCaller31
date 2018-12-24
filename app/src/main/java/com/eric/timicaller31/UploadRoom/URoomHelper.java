package com.eric.timicaller31.UploadRoom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class URoomHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Room.db";
    public URoomHelper(Context context){
        this(context,"Room",null,1);
    }
    private URoomHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE ROOM(" +
                "COL_id INTEGER PRIMARY KEY autoincrement,"+
                "COL_NAME VARCHAR NOT NULL,"+
                "COL_URI VARCHAR )");
//        sqLiteDatabase.execSQL("CREATE TABLE DROOM(" +
//                "COL_id INTEGER PRIMARY KEY autoincrement,"+
//                "COL_NAME VARCHAR NOT NULL,"+
//                "COL_URI VARCHAR )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
