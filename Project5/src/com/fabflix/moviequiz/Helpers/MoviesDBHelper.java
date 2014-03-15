package com.fabflix.moviequiz.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MoviesDBHelper {

    // Column Names to be returned by our queries.
    public static String[] COLUMNS_MOVIES = new String[]{"_id", "title", "year", "director", "banner_url", "trailer_url"};
    public static String[] COLUMNS_STARS = new String[]{"_id", "first_name", "last_name", "dob", "photo_url"};
    public static String[] COLUMNS_STARS_IN_MOVIES = new String[]{"star_id", "movie_id"};
    public static String[] COLUMNS_GENRES = new String[]{"_id", "name"};
    public static String[] COLUMNS_GENRES_IN_MOVIES = new String[]{"genre_id", "movie_id"};

    private Context mContext;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public MoviesDBHelper(Context context) {
        mContext = context;

    }

    public MoviesDBHelper open() throws SQLException {
        // Create an instance of our DB Helper to create, or get an existing instance.
        // this creates the database if it does not exist, or returns the isntance.
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    // TODO: Create Methods to retreive information....
    public Cursor fetchItems() {
        return mDb.query(DatabaseHelper.TABLE_GENRES, COLUMNS_GENRES, null, null, null, null, null);
    }
}
