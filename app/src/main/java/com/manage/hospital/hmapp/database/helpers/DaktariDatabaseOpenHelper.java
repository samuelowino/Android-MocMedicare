package com.manage.hospital.hmapp.database.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.manage.hospital.hmapp.database.contracts.DatabaseContract;

public class DaktariDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Daktari_Online.db";
    private static final int DATABASE_VERSION = 1;

    public DaktariDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.SQL_CREATE_USER_TABLE);
        db.execSQL(DatabaseContract.SQL_CREATE_PATIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.SQL_DELETE_USER_TABLE);
        db.execSQL(DatabaseContract.SQL_DELETE_PATIENT_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
