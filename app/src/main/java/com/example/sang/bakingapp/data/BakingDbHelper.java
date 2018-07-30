package com.example.sang.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BakingDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "baking.db";
    private static final int DATABASE_VERSION = 12;

    private static final String CREATE_RECIPE_TABLE_SQL = "CREATE TABLE "
            + BakingContract.TABLE_RECIPE + "("
            + BakingContract.COLUMN_RECIPE_ID +" INTEGER PRIMARY KEY ,"
            + BakingContract.COLUMN_RECIPE_NAME + "TEXT NOT NULL ,"
            + BakingContract.COLUMN_RECIPE_IMAGE + "TEXT NOT NULL ,"
            + BakingContract.COLUMN_SERVING + "INT )" ;

    private static final String CREATE_RECIPE_INGREDIENT_TABLE_SQL = "CREATE TABLE "
            + BakingContract.TABLE_INGREDIENTS +"(" +BakingContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + BakingContract.COLUMN_QUANTITY +" INTEGER ,"
            + BakingContract.COLUMN_MEASURE + "TEXT NOT NULL ,"
            + BakingContract.COLUMN_INGREDIENT + "TEXT NOT NULL ,"
            + BakingContract.COLUMN_INGREDIENT_RECIPE_ID + "INTEGER ,"
            + "FOREIGN KEY( " + BakingContract.COLUMN_INGREDIENT_RECIPE_ID + ") REFERENCES "
            + BakingContract.TABLE_RECIPE + "( " + BakingContract.COLUMN_RECIPE_ID + ") ON DELETE CASCADE"+")";

    private static final String CREATE_RECIPE_STEPS_TABLE_SQL = "CREATE TABLE "
            + BakingContract.TABLE_RECIPE_STEPS + "("+ BakingContract.COLUMN_STEP_ID +"INT PRIMARY KEY,"
            + BakingContract.COLUMN_SHORT_DESCRIPTION + "TEXT NOT NULL ,"
            + BakingContract.COLUMN_DESCRIPTION + "TEXT NOT NULL ,"
            + BakingContract.COLUMN_VIDEO_URL + "TEXT NOT NULL,"+
            BakingContract.COLUMN_THUMBNAIL + "TEXT ,"+
            "FOREIGN KEY( " + BakingContract.COLUMN_STEP_RECIPE_ID + ") REFERENCES "
            + BakingContract.TABLE_RECIPE + "( " + BakingContract.COLUMN_RECIPE_ID + ") ON DELETE CASCADE"+")";




    public BakingDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECIPE_TABLE_SQL);
        db.execSQL(CREATE_RECIPE_INGREDIENT_TABLE_SQL);
        db.execSQL(CREATE_RECIPE_STEPS_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");

    }
}
