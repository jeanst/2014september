package com.example.carsagencydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eladlavi on 2/4/15.
 */
public class DBAdapter {

    static final String DATABASE_NAME = "CarAgency.db";
    static final String DATABASE_TABLE = "Cars";
    static final int DATABASE_VERSION = 1;
    static final String KEY_ID = "_id";
    static final String KEY_MANUF = "manufacturer";
    static final String KEY_LICENSE = "license";
    static final String KEY_OWNERNAME = "ownername";
    static final String KEY_OWNERTEL = "ownertel";
    static final String KEY_YEAR = "year";
    static final String KEY_KM = "km";
    static final String KEY_SEATS = "seats";
    static final String KEY_AIRBAG = "airbag";
    static final String KEY_LEASING = "leasing";
    static final String KEY_PRICE = "price";
    static final String KEY_LEVYPRICE = "levyprice";
    static final String KEY_MAXBID = "maxbid";
    static final String KEY_TELBID = "telbid";

    public Car c;
    public Owner owner;
    public Bid bid;

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + "(" +
                    KEY_MANUF + " text," +
                    KEY_LICENSE + " text," +
                    KEY_OWNERNAME + " text," +
                    KEY_OWNERTEL + " text," +
                    KEY_YEAR + " integer," +
                    KEY_KM + " integer," +
                    KEY_SEATS + " integer," +
                    KEY_AIRBAG + " integer," +
                    KEY_LEASING + " integer," +
                    KEY_PRICE + " integer," +
                    KEY_LEVYPRICE + " integer," +
                    KEY_MAXBID + " integer," +
                    KEY_TELBID + " text" +
                    ")");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public void open() {
        db = DBHelper.getWritableDatabase();
    }

    public void close() {
        DBHelper.close();
    }

    public long insertCar(Car c) {
        ContentValues values = new ContentValues();
        values.put(KEY_MANUF, c.getManufacturer().toString());
        values.put(KEY_LICENSE, c.getLicense().toString());
        values.put(KEY_OWNERNAME, c.getOwner().getName().toString());
        values.put(KEY_OWNERTEL, c.getOwner().getTelNumber().toString());
        values.put(KEY_YEAR, c.getYear());
        values.put(KEY_KM, c.getKm());
        values.put(KEY_SEATS, c.getSeats());
        values.put(KEY_AIRBAG, (c.hasAirbags() ? 1 : 0));
        values.put(KEY_LEASING, (c.isLeasingOrRental() ? 1 : 0));
        values.put(KEY_PRICE, c.getPrice());
        values.put(KEY_LEVYPRICE, c.getLevyPrice());
        values.put(KEY_MAXBID, c.getHighestBid().getBidPrice());
        values.put(KEY_TELBID, c.getHighestBid().getTelNum().toString());
        return db.insert(DATABASE_TABLE, null, values);


    }
    public long updateCar(Car c) {
        ContentValues values = new ContentValues();
        values.put(KEY_MAXBID, c.getHighestBid().getBidPrice());
        values.put(KEY_TELBID, c.getHighestBid().getTelNum().toString());
        return db.update(DATABASE_TABLE, values,KEY_LICENSE + "=?", new String[]{c.getLicense().toString()});
    }

    public long deleteCar(Car c) {
        return db.delete(DATABASE_TABLE, KEY_LICENSE + "=?", new String[]{c.getLicense().toString()});

    }

    public Cursor getAllCars() {
        return db.query(DATABASE_TABLE, new String[]{
                        KEY_MANUF,
                        KEY_LICENSE,
                        KEY_OWNERNAME,
                        KEY_OWNERTEL,
                        KEY_YEAR,
                        KEY_KM,
                        KEY_SEATS,
                        KEY_AIRBAG,
                        KEY_LEASING,
                        KEY_PRICE,
                        KEY_LEVYPRICE,
                        KEY_MAXBID,
                        KEY_TELBID
                },
                null, null, null, null, null);
    }

    public void deleteAllCars() {
        db.delete(DATABASE_TABLE, null, null);
    }
}
