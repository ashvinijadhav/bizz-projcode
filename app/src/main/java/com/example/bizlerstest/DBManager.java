package com.example.bizlerstest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class DBManager {

    private DBHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String number, String make,String model,String varient, String fueltype, byte[]  photo) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.VEHICLENO, number);
        contentValue.put(DBHelper.VEHICLEMAKE, make);
        contentValue.put(DBHelper.VEHICLEMODEL, model);
        contentValue.put(DBHelper.VEHICLEVARIENT,varient);
        contentValue.put(DBHelper.VEHICLEFUELTYPE, fueltype);
        contentValue.put(DBHelper.VEHICLEPHOTO, photo);
       long insertResult= database.insert(DBHelper.TABLE_NAME, null, contentValue);
       return insertResult;
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBHelper.VEHICLENO, DBHelper.VEHICLEMAKE,
                                          DBHelper.VEHICLEMODEL,DBHelper.VEHICLEVARIENT,
                                          DBHelper.VEHICLEFUELTYPE,DBHelper.VEHICLEPHOTO };
        String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
              //  Cursor cursor = database.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        System.out.println("Dddddddddddddddddd"+cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            System.out.println("Dddddddddddddddddd" );

        }
        else {
            System.out.println("Dddddddddddddddddd" );
        }

        return cursor;
    }

    public int update(String number, String make,String model,String varient, String fueltype, byte[] photo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.VEHICLEMAKE, make);
        contentValues.put(DBHelper.VEHICLEMODEL, model);
        contentValues.put(DBHelper.VEHICLEVARIENT,varient);
        contentValues.put(DBHelper.VEHICLEFUELTYPE, fueltype);
        contentValues.put(DBHelper.VEHICLEPHOTO, photo);
        int i = database.update(DBHelper.TABLE_NAME, contentValues, DBHelper.VEHICLENO + " = " +DBHelper.VEHICLENO, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBHelper.TABLE_NAME, DBHelper.VEHICLENO + "=" + _id, null);
    }

}
