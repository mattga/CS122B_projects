package com.fabflix.moviequiz.Helpers;

import java.util.Random;

import android.database.Cursor;
import android.util.Log;

import com.fabflix.moviequiz.Types.MovieQuestion;

public class QuestionGenerator {

	private MoviesDBHelper mDbHelper;
	public QuestionGenerator(MoviesDBHelper dbHelper) {
		mDbHelper = dbHelper;
	}
	
	public  MovieQuestion generateMovieReleaseYearQuestion() {
		String qString = "When was \"%s\" released?";
		MovieQuestion question = new MovieQuestion();
		question.answerIndex = (new Random()).nextInt(4); // Candidate the subject of our question. 
		
		Cursor rs = mDbHelper.fetchMovieItems(); // fetches 4 movie items.
		if (rs.moveToFirst()) {
			for (int i=0; !rs.isAfterLast(); i++) {
				question.answers[i] = rs.getString(2); // years
				
				if (i == question.answerIndex)
					question.question = String.format(qString, rs.getString(1)); // title
				rs.moveToNext();
			}
		} else {
			Log.e("CURSOR ERROR", "NO RESULTS");
			question.question = "NO QUESTION GENERATED....";
			question.answers = new String[] {"a","b","c","d"};
			question.answerIndex = 0;
		}
		return question;
	}
	
	
	
	public  MovieQuestion generateMovieDirectorQuestion() {
		String qString = "When directed the movie: \"%s\"?";
		MovieQuestion question = new MovieQuestion();
		question.answerIndex = (new Random()).nextInt(4); // Candidate the subject of our question. 
		
		Cursor rs = mDbHelper.fetchMovieItems(); // fetches 4 movie items.
		if (rs.moveToFirst()) {
			for (int i=0; !rs.isAfterLast(); i++) {
				question.answers[i] = rs.getString(3); // years
				
				if (i == question.answerIndex)
					question.question = String.format(qString, rs.getString(1)); // title
				rs.moveToNext();
			}
		} else {
			Log.e("CURSOR ERROR", "NO RESULTS");
			question.question = "NO QUESTION GENERATED....";
			question.answers = new String[] {"a","b","c","d"};
			question.answerIndex = 0;
		}
		return question;
	}

}
