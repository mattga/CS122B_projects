package com.fabflix.moviequiz.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MoviesDBHelper {

    // Column Names to be returned by our queries.
    public static String[] COLUMNS_MOVIES = new String[]{"id", "title", "year", "director", "banner_url", "trailer_url"};
    public static String[] COLUMNS_STARS = new String[]{"id", "first_name", "last_name", "dob", "photo_url"};
    public static String[] COLUMNS_STARS_IN_MOVIES = new String[]{"star_id", "movie_id"};
    public static String[] COLUMNS_GENRES = new String[]{"id", "name"};
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
    public Cursor fetchMovieItems() {
    	// Query we are running:
    	// SELECT DISTINCT * FROM `movies` GROUP BY `year` ORDER BY RAND() LIMIT 4
    	
    	String groupBy = "year";
    	String orderBy = "RANDOM()";
    	String limit = "4";
        return mDb.query(DatabaseHelper.TABLE_MOVIES, COLUMNS_MOVIES, null, null, orderBy, null, groupBy, limit);
    }
}
