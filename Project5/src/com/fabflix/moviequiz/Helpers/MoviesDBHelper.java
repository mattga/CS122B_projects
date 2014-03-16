package com.fabflix.moviequiz.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fabflix.moviequiz.Types.MovieQuestion;

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
		Log.d("INIT", "MoviesDBHelper.........");
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	// TODO: Create Methods to retrieve information....
	public MovieQuestion getQuestion() {
		MovieQuestion newQ = new MovieQuestion();
		int row;
		Cursor c;
		
		// Pick random question
		switch ((int)(Math.random()+10)) {
		case 0: // Who directed the movie %s?
			c = mDb.rawQuery("SELECT * FROM movies", null);
			row = (int)(Math.random()+c.getCount());
			c.moveToPosition(row);
			newQ.question = String.format(QuestionTemplates.QUESTIONS[0], c.getString(1));
			newQ.answers[0] = c.getString(3);
			
			// Fill answers with unique wrong values
			for(int i = 1; i < 4;) {
				row = (int)(Math.random()+c.getCount());
				c.moveToPosition(row);
				for(int j = 0; j < i; j++) {
					String s = newQ.answers[j];
					if(s.equals(c.getString(3)))
						continue;
					newQ.answers[i++] = c.getString(3);
				}
			}
			break;
		case 1: // When was the movie %s released?
			c = mDb.rawQuery("SELECT * FROM movies", null);
			row = (int)(Math.random()+c.getCount());
			c.moveToPosition(row);
			newQ.question = String.format(QuestionTemplates.QUESTIONS[1], c.getString(1));
			newQ.answers[0] = c.getString(2);
			
			// Fill answers with unique wrong values
			for(int i = 1; i < 4;) {
				row = (int)(Math.random()+c.getCount());
				c.moveToPosition(row);
				for(int j = 0; j < i; j++) {
					String s = newQ.answers[j];
					if(s.equals(c.getString(2)))
						continue;
					newQ.answers[i++] = c.getString(2);
				}
			}
			break;
		case 2: // Which star was in the movie %s?
			break;
		case 3: // Which star was not in the movie %s?
			break;
		case 4: // In which movie do the stars %s and %s appear together?
			break;
		case 5: // Who directed the movie %s?
			break;
		case 6: // Who did not direct the movie %s?
			// Dumb question...
//			break;
		case 7: // Which star appears in both movies %s and %s?
			break;
		case 8: // Which star did not appear in the same movie with the star %s?
			break;
		case 9: // Who directed the star %s in year %s?
			break;
		}
		
		return newQ;
	}
	
	private String randomMovie() {
		Cursor c = mDb.query(DatabaseHelper.TABLE_MOVIES, COLUMNS_MOVIES, null, null, null, null, null);
		c.moveToPosition((int)(Math.random()+c.getCount()));
		return c.getString(1);
	}
	
	private String randomStar() {
		Cursor c = mDb.query(DatabaseHelper.TABLE_STARS, COLUMNS_STARS, null, null, null, null, null);
		c.moveToPosition((int)(Math.random()+c.getCount()));
		return c.getString(1) + " " + c.getString(2);
	}
	
	private String randomDirector() {
		Cursor c = mDb.query(DatabaseHelper.TABLE_MOVIES, COLUMNS_MOVIES, "WHERE DISTINCT(director)", null, null, null, null);
		c.moveToPosition((int)(Math.random()+c.getCount()));
		return c.getString(3);
	}
}
