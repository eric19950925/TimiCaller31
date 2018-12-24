package com.eric.timicaller31.DownloadRoom;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DRoomHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DRoom.db";
    public DRoomHelper(Context context){
        this(context,"DRoom",null,1);
    }
    private DRoomHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE DROOM(" +
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
