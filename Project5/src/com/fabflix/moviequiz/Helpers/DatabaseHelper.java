package com.fabflix.moviequiz.Helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Database Helper -- Used to fetch an instance of the database.
 *
 * onCreate() method is overriden to read assets and create a table and insert data based on
 * sql files.
 */
class DatabaseHelper extends SQLiteOpenHelper {

    public Context mContext;
    public static String TAG = "DatabaseHelper.class";
    // Database name:
    public static String DATABASE_NAME = "moviedb";
    public static int DATABASE_VERSION = 1;

    // Tables:
    public static String TABLE_MOVIES = "movies";
    public static String TABLE_STARS = "stars";
    public static String TABLE_STARS_IN_MOVIES = "stars_in_movies";
    public static String TABLE_GENRES = "genres";
    public static String TABLE_GENRES_IN_MOVIES = "genres_in_movies";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /**
     * Called When the Database is created for the first time.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        InputStream fileStream;
        InputStreamReader streamReader;
        BufferedReader buffReader;
        String query;
        AssetManager am = mContext.getAssets();

        try {
            // Lets Run the Table Creation Queries....s
            fileStream = am.open("createtable.sql");
            streamReader = new InputStreamReader(fileStream);
            buffReader = new BufferedReader(streamReader);

            query = buffReader.readLine();
            while (query != null) {
                db.execSQL(query);
                query = buffReader.readLine();
            }
        } catch (IOException e) {
            // e.printStackTrace();
            Log.i(TAG, "Error Opening createtable.sql.");
        }

        try {
            // Lets Run the data insertion queries....
            fileStream = am.open("data.sql");
            streamReader= new InputStreamReader(fileStream);
            buffReader = new BufferedReader(streamReader);

            query = buffReader.readLine();
            while (query != null) {
                db.execSQL(query);
                query = buffReader.readLine();
            }
        } catch (IOException e) {
            // e.printStackTrace();
            Log.i(TAG, "Error Opening data.sql.");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_STARS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_GENRES_IN_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_GENRES);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_STARS_IN_MOVIES);
        onCreate(db);
    }
}