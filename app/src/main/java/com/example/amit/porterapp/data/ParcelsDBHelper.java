package com.example.amit.porterapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.amit.porterapp.data.ParcelsDBContract.ParcelsEntry;

/**
* Created by amit on 7/20/2015.
*/
public class ParcelsDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "porter.db";

    public ParcelsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_PARCELS_TABLE = "CREATE TABLE " + ParcelsEntry.TABLE_NAME + " (" +
                ParcelsEntry._ID + " INTEGER PRIMARY KEY," +
                ParcelsEntry.COLUMN_PARCEL_NAME + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_IMAGE_URL + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_DATE + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_TYPE + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_WEIGHT + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_PHONE + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_PRICE + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_QUANTITY + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_COLOR + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_LINK + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_LOC_LAT + " TEXT NOT NULL, " +
                ParcelsEntry.COLUMN_PARCEL_LOC_LONG + " TEXT NOT NULL, " +
                " UNIQUE (" + ParcelsEntry.COLUMN_PARCEL_NAME + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_PARCELS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ParcelsEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
