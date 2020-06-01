package com.example.bizlerstest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    //DATABASE VERSION & NAME
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BIZLERVEHICLES.db";
    //TABLE NAME
    // Table Name
    public static final String TABLE_NAME = "VEHICLES";
    // Table columns
    public static final String VEHICLENO = "number";
    public static final String VEHICLEMAKE= "make";
    public static final String VEHICLEMODEL= "model";
    public static final String VEHICLEVARIENT = "variant";
    public static final String VEHICLEFUELTYPE = "fueltype";
    public static final String VEHICLEPHOTO = "photo";

       String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + "("
               + VEHICLENO + " TEXT,"
               + VEHICLEMAKE + " TEXT,"
               + VEHICLEMODEL + " TEXT,"
               + VEHICLEVARIENT + " TEXT,"
               + VEHICLEFUELTYPE + " TEXT,"
               + VEHICLEPHOTO + " BLOB"
               + ")";

        private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
}
