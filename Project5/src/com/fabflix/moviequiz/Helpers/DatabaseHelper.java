package com.fabflix.moviequiz.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
		Log.d("INIT", "DatabaseHelper.........");
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
		String query, line;
		ContentValues values;
		AssetManager am = mContext.getAssets();

		Log.d("INIT", "DatabaseHelper onCreate.........");

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
			// Insert movies...
			fileStream = am.open("movies.csv");
			streamReader = new InputStreamReader(fileStream);
			buffReader = new BufferedReader(streamReader);

			try{
				db.beginTransaction();
				while ((line = buffReader.readLine()) != null) {
					String[] rowData = line.split(",");
					values = new ContentValues();
					values.put("id", rowData[0]);
					values.put("title", rowData[1]);
					values.put("year", rowData[2]);
					values.put("director", rowData[3]);
					values.put("banner_url", rowData[4]);
					values.put("trailer_url", rowData[5]);
					db.insert(TABLE_MOVIES, null, values);
				}
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				Log.e("TAG", e.getMessage());
			} finally {
				db.endTransaction();
				Log.i(TAG, "Movies populated.");
			}
			
			// Insert stars...
			fileStream = am.open("stars.csv");
			streamReader = new InputStreamReader(fileStream);
			buffReader = new BufferedReader(streamReader);

			try{
				db.beginTransaction();
				while ((line = buffReader.readLine()) != null) {
					String[] rowData = line.split(",");
					values = new ContentValues();
					values.put("id", rowData[0]);
					values.put("first_name", rowData[1]);
					values.put("last_name", rowData[2]);
					values.put("dob", rowData[3]);
					values.put("photo_url", rowData[4]);
					db.insert(TABLE_STARS, null, values);
				}
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				Log.e(TAG, e.getMessage());
			} finally {
				db.endTransaction();
				Log.i(TAG, "Stars populated.");
			}
			// Insert stars_in_movies...
			fileStream = am.open("stars_in_movies.csv");
			streamReader = new InputStreamReader(fileStream);
			buffReader = new BufferedReader(streamReader);

			try{
				db.beginTransaction();
				while ((line = buffReader.readLine()) != null) {
					String[] rowData = line.split(",");
					values = new ContentValues();
					values.put("star_id", rowData[0]);
					values.put("movie_id", rowData[1]);
					db.insert(TABLE_STARS_IN_MOVIES, null, values);
				}
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				Log.e("TAG", e.getMessage());
			} finally {
				db.endTransaction();
				Log.i(TAG, "Stars_in_movies populated.");
			}
		} catch (IOException e) {
			// e.printStackTrace();
			Log.i(TAG, "Error Opening csv: " + e.getMessage());
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