package com.fabflix.moviequiz.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
		boolean okToAdd = false;

		// Pick random question
		switch ((int)(Math.random()*9)) {
		case 0: // Who directed the movie %s?
			c = mDb.rawQuery("SELECT * FROM movies", null);
			row = (int)(Math.random()*c.getCount());
			c.moveToPosition(row);
			newQ.question = String.format(QuestionTemplates.QUESTIONS[0], c.getString(1));
			newQ.answers[0] = c.getString(3);
			
			Log.i("NewQuestion", "Correct:" + c.getString(3));
			
			// Fill answers with unique wrong values
			for(int i = 1; i < 4;) {
				row = (int)(Math.random()*c.getCount());
				c.moveToPosition(row);
				Log.i("NewQuestion", "Wrong:" + c.getString(3));
				// i < 4, so we do not increment indefinitely.
				for(int j = 0; j < i && i < 4; j++) {
					if(newQ.answers[j].equals(c.getString(3))) {
						okToAdd = false;
						break;
					}
					okToAdd = true;
				}
				if(okToAdd)
					newQ.answers[i++] = c.getString(3);
			}
			break;
		case 1: // When was the movie %s released?
			c = mDb.rawQuery("SELECT * FROM movies", null);
			row = (int)(Math.random()*c.getCount());
			c.moveToPosition(row);
			newQ.question = String.format(QuestionTemplates.QUESTIONS[1], c.getString(1));
			newQ.answers[0] = c.getString(2);

			// Fill answers with unique wrong values
			for(int i = 1; i < 4;) {
				int wrongYear = Integer.parseInt(newQ.answers[0]) + (int)((Math.random()-.5)*50); // +/- [1-25] years
				if (wrongYear <= 2014 && wrongYear != Integer.parseInt(newQ.answers[0]))
					newQ.answers[i++] = "" + wrongYear;
			}
			break;
		case 2: // Which star was in the movie %s?
			c = mDb.rawQuery("SELECT DISTINCT m.`title`, m.`director`, s.`first_name`, s.`last_name` FROM `movies` m, `stars` s, `stars_in_movies` sm WHERE m.id = sm.movie_id AND s.id = sm.star_id AND s.id GROUP BY m.`id`, s.`first_name`, s.`last_name` ORDER BY RANDOM() LIMIT 4", null);
			row = (int)(Math.random()*c.getCount());
			c.moveToFirst(); // only 4 results are returned.
			newQ.question = String.format(QuestionTemplates.QUESTIONS[2], c.getString(0));
			newQ.answers[0] = c.getString(2) +" "+ c.getString(3); 
			newQ.correctAnswerIndex = 0;
			for (int j = 1; j < 4; j++) {
				c.moveToNext();
				newQ.answers[j] =  c.getString(2) +" "+ c.getString(3);
			}
			break;
		case 3: // Which star was not in the movie %s?
			// The lone star, the left out one...the answer
			String singleActorQuery = "SELECT mov.`title`, star.`first_name`, star.`last_name`, star.`id` FROM `movies` mov, `stars` star, "+
					"(SELECT movie_id, star_id FROM `stars_in_movies` GROUP BY movie_id  HAVING COUNT(star_id) <= 1 ORDER BY RANDOM() LIMIT 1) mult "+ 
					"WHERE mov.`id` = mult.`movie_id` AND star.`id`= mult.`star_id`";
			c = mDb.rawQuery(singleActorQuery, null);
			c.moveToFirst();
			newQ.correctAnswerIndex = 0;
			newQ.answers[0] = c.getString(1) +" "+c.getString(2);

			// The cool kids: A group of actors in the same movie....excluding the lone star.
//			String starId = c.getString(3);
			String multipleActorQuery = "SELECT mov.`title`, star.`first_name`, star.`last_name` " +
					"FROM `movies` mov, `stars` star, `stars_in_movies` sim, " +
					"(SELECT movie_id, star_id FROM `stars_in_movies` GROUP BY movie_id  HAVING COUNT(star_id) > 2 ORDER BY RANDOM() LIMIT 1) mult" +
					" WHERE mov.`id` = mult.`movie_id` AND star.`id` = sim.`star_id` AND mult.`movie_id` = sim.`movie_id` " +
					"LIMIT 3";
			c = mDb.rawQuery(multipleActorQuery, null);
			c.moveToFirst();
			newQ.question = String.format(QuestionTemplates.QUESTIONS[3], c.getString(0));
			for (int i = 1; i < 4; i++) {
				newQ.answers[i] = c.getString(1) +" "+c.getString(2);
				c.moveToNext();
			}

			break;
		case 4: // In which movie do the stars %s and %s appear together?
			// Get a movie they both star in
			c = mDb.rawQuery("SELECT m.`title`, s.`first_name`, s.`last_name`, s2.`first_name`, s2.`last_name`" +
					"FROM `movies` m, `stars` s, `stars` s2, `stars_in_movies` sm, `stars_in_movies` sm2 " +
					"WHERE m.id = sm.movie_id AND m.id = sm2.movie_id AND s.id = sm.star_id AND s2.id = sm2.star_id AND s.id != s2.id " +
					"GROUP BY m.`id`, s.`first_name`, s.`last_name` ORDER BY RANDOM() LIMIT 4", null);
			row = (int)(Math.random()*c.getCount());
			c.moveToFirst();
			newQ.question = String.format(QuestionTemplates.QUESTIONS[4], c.getString(1)+" "+c.getString(2), c.getString(3)+" "+c.getString(4));
			newQ.answers[0] = c.getString(0); 
			newQ.correctAnswerIndex = 0;
			for (int j = 1; j < 4; j++) {
				c.moveToNext();
				newQ.answers[j] =  c.getString(0);
			}
			break;
		case 5: // Who directed the star %s?
			c = mDb.rawQuery("SELECT DISTINCT m.`year`, m.`director`, s.`first_name`, s.`last_name` FROM `movies` m, `stars` s, `stars_in_movies` sm WHERE m.id = sm.movie_id AND s.id = sm.star_id AND s.id GROUP BY m.`id`, s.`first_name`, s.`last_name` ORDER BY RANDOM() LIMIT 4", null);
			row = (int)(Math.random()*c.getCount());
			c.moveToFirst(); // only 4 results are returned.
			newQ.question = String.format(QuestionTemplates.QUESTIONS[5], c.getString(2) +" "+ c.getString(3));
			newQ.answers[0] = c.getString(1); 
			newQ.correctAnswerIndex = 0;
			for (int j = 1; j < 4; j++) {
				c.moveToNext();
				newQ.answers[j] =  c.getString(1);
			}
			break;
		case 6: // Which star appears in both movies %s and %s?
			// Get the 2 movies with the same actor...
			c = mDb.rawQuery("SELECT mov.`id`, mov.`title`, star.`first_name`, star.`last_name`, star.`id` FROM `movies` mov, `stars_in_movies` sim, `stars` star, (SELECT movie_id, star_id, COUNT(movie_id) as cnt FROM `stars_in_movies` GROUP BY star_id HAVING COUNT(movie_id) > 3 ORDER BY RANDOM() LIMIT 1) as popular WHERE sim.`movie_id` = mov.`id` AND popular.`star_id` = sim.`star_id` AND star.`id` = sim.`star_id` LIMIT 2", null);
			c.moveToFirst();
			String id = c.getString(4);
			
			String[] movies = new String[2];
			movies[0] = c.getString(1);
			c.moveToNext();
			movies[1] = c.getString(1);
			
			newQ.question = String.format(QuestionTemplates.QUESTIONS[6], movies[0], movies[1]);
			newQ.answers[0] = c.getString(2) +" "+ c.getString(3);
			
			newQ.correctAnswerIndex = 0;
			
			// Pick 3 alternatives...
			c = mDb.rawQuery("SELECT s.first_name, s.last_name FROM stars s WHERE s.id != "+id+" GROUP BY s.id ORDER BY RANDOM() LIMIT 3", null);
			c.moveToFirst();
			for (int i = 1; i < 4; i++) {
				newQ.answers[i] = c.getString(0) +" "+ c.getString(1);
				c.moveToNext();
			}
			break;
		case 7: // Which star did not appear in the same movie with the star %s?
			// The lone star, the left out one...the answer
			String singleActorQueryII = "SELECT mov.`title`, star.`first_name`, star.`last_name`, star.`id` FROM `movies` mov, `stars` star, "+
					"(SELECT movie_id, star_id FROM `stars_in_movies` GROUP BY movie_id  HAVING COUNT(star_id) <= 1 ORDER BY RANDOM() LIMIT 1) mult "+ 
					"WHERE mov.`id` = mult.`movie_id` AND star.`id`= mult.`star_id`";
			c = mDb.rawQuery(singleActorQueryII, null);
			c.moveToFirst();
			newQ.correctAnswerIndex = 0;
			newQ.answers[0] = c.getString(1) +" "+c.getString(2);

			// The cool kids: A group of actors in the same movie....excluding the lone star.
			String multipleActorQueryII = "SELECT mov.`title`, star.`first_name`, star.`last_name` " +
					"FROM `movies` mov, `stars` star, `stars_in_movies` sim, " +
					"(SELECT movie_id, star_id FROM `stars_in_movies` GROUP BY movie_id  HAVING COUNT(star_id) > 3 ORDER BY RANDOM() LIMIT 1) mult" +
					" WHERE mov.`id` = mult.`movie_id` AND star.`id` = sim.`star_id` AND mult.`movie_id` = sim.`movie_id` " +
					" GROUP BY star.`id`"+
					" LIMIT 4";
			c = mDb.rawQuery(multipleActorQueryII, null);
			c.moveToFirst();
			// Take the first of the group and put his name in the question
			newQ.question = String.format(QuestionTemplates.QUESTIONS[7], c.getString(1) +" "+c.getString(2));
			c.moveToNext();
			for (int i = 1; i < 4; i++) {
				newQ.answers[i] = c.getString(1) +" "+c.getString(2);
				c.moveToNext();
			}
			break;
		default: // Who directed the star %s in year %s?
			c = mDb.rawQuery("SELECT DISTINCT m.`year`, m.`director`, s.`first_name`, s.`last_name` FROM `movies` m, `stars` s, `stars_in_movies` sm WHERE m.id = sm.movie_id AND s.id = sm.star_id AND s.id GROUP BY m.`id`, s.`first_name`, s.`last_name` ORDER BY RANDOM() LIMIT 4", null);
			row = (int)(Math.random()*c.getCount());
			c.moveToFirst(); // only 4 results are returned.
			newQ.question = String.format(QuestionTemplates.QUESTIONS[8], c.getString(2) +" "+ c.getString(3), c.getString(0));
			newQ.answers[0] = c.getString(1); 
			newQ.correctAnswerIndex = 0;
			for (int j = 1; j < 4; j++) {
				c.moveToNext();
				newQ.answers[j] =  c.getString(1);
			}
			break;
		}

		return newQ;
	}
}
