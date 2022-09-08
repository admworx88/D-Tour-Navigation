package com.admoliva.metrobusticketing.SQLiteDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jUniX on 12/27/2015.
 */
public class SelfAuditDatabase extends SQLiteOpenHelper {
    private static SelfAuditDatabase mInstance = null;
    public static final String LOG = "DatabaseHelper";

    public static final String DATABASE_NAME = "MetroBusTicketing";

    private static final int DATABASE_VERSION = 1;


    public static SelfAuditDatabase getInstance(Context ctx)
    {
        if(mInstance == null)
        {
            mInstance = new SelfAuditDatabase(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public SelfAuditDatabase(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
