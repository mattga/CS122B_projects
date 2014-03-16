package com.fabflix.moviequiz.Helpers;

/**
 * Created by tellez on 3/13/14.
 */
public class QuestionTemplates {
	public static final String[] QUESTIONS = new String[] {	"Who directed the movie %s?",
															"When was the movie %s released?",
															"Which star was in the movie %s?",
															"Which star was not in the movie %s?",
															"In which movie do the stars %s and %s appear together?",
															"Who directed the star %s?",
															"Which star appears in both movies %s and %s?",
															"Which star did not appear in the same movie with the star %s?",
															"Who directed the star %s in the year %s?"};

	public static final int[] QUESTION_ARG_COUNT = new int[] {1 , 1, 1, 2, 1, 2, 1, 2};

	private QuestionTemplates(){}
}
