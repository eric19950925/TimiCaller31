package com.eric.timicaller31.DailyEvents;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "EVENT.db";
    public EventHelper(Context context){
        this(context,"Event",null,1);
    }
    private EventHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE EVENT(" +
                "COL_id INTEGER PRIMARY KEY autoincrement,"+
                "COL_NAME VARCHAR NOT NULL,"+
                "COL_ACTIVE VARCHAR ,"+
                "COL_HOUR INTEGER NOT NULL,"+
                "COL_MIN INTEGER NOT NULL,"+
                "COL_YEAR INTEGER NOT NULL,"+
                "COL_MONTH INTEGER NOT NULL,"+
                "COL_DATE INTEGER NOT NULL,"+
                "COL_HINT VARCHAR ,"+
                "COL_PHONE VARCHAR,"+
                "COL_IMAGE BLOB)");

        sqLiteDatabase.execSQL("CREATE TABLE FAVORITE_ROOM(" +
                "COL_id INTEGER PRIMARY KEY autoincrement,"+
                "COL_NAME NOT NULL,"+
                "COL_KEY VARCHAR NOT NULL,"+
                "COL_BUILDER_NAME VARCHAR )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
