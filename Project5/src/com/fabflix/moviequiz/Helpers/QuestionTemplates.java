package com.fabflix.moviequiz.Helpers;

/**
 * Created by tellez on 3/13/14.
 */
public class QuestionTemplates {
    static {
        final String[] QUESTIONS = new String[] {"Who directed the movie %s?",
                                                            "When was the movie %s released?",
                                                            "Which star was in the movie %s?",
                                                            "In which movie the stars %s and %s appear together?",
                                                            "Who directed direct the star %s?",
                                                            "Which star appears in both movies %s and %s?",
                                                            "Which star did not appear in the same movie with the star %s?",
                                                            "Who directed the star %s in year %s?"};

        final int[] QUESTION_ARG_COUNT = new int[] {1 , 1, 1, 2, 1, 2, 1, 2};
    }

    private QuestionTemplates(){}
}
