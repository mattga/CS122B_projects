package com.fabflix.moviequiz;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.fabflix.moviequiz.Helpers.MoviesDBHelper;


public class QuizActivity extends Activity {
    /**
     * Keys used for storing state between Activity Life Cycle.
     */
    private final static String KEY_QUESTION_COUNT = "COUNT";
    private final static String KEY_QUESTION_CORRECT = "CORRECT";
    private final static String KEY_QUESTION_WRONG = "WRONG";
    private final static String KEY_TIME_ELAPSED = "TIME_ELAPSED";

    private final static int QUESTION_DELAY = 2000; // 2second delay.
    private final static long TIME_MAX = 180000; // 3 minute max in milliseconds
    private long mStartTime = System.currentTimeMillis();
    private long mTimerValue = 0;
    private int mQuestionsCorrect = 0;
    private int mQuestionsWrong = 0;

    /**
     * Method is called the first time the activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }


    /**
     * Method is called when the actitivy goes out of focus
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Method is called before the activity is shut down.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the state of the current quiz.
        outState.putInt(KEY_QUESTION_CORRECT, mQuestionsCorrect);
        outState.putInt(KEY_QUESTION_WRONG, mQuestionsWrong);
        outState.putLong(KEY_TIME_ELAPSED, mTimerValue);
    }

    /**
     * Moethod is called after the activity is restarted..
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null)
            return;// No saved instance exists.
        // Restore the values if they exists....
        mQuestionsCorrect = savedInstanceState.getInt(KEY_QUESTION_CORRECT);
        mQuestionsWrong = savedInstanceState.getInt(KEY_QUESTION_WRONG);
        mTimerValue = savedInstanceState.getLong(KEY_TIME_ELAPSED);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * OnClickListener for every answer.
     */
    private class AnswerEventListener implements View.OnClickListener {
        public void onClick(View btn) {
            // update the timer....
            mTimerValue += (System.currentTimeMillis() - mStartTime);
            String answer = ((Button)btn).getText().toString();
            // Evaluate the
            // Set a time-out to move to the next question...
            new Handler().postDelayed(new NewQuestion(), QUESTION_DELAY);
        }
    }

    /**
     * Class implements runnable, and is called whenever a new question needs to be generated.
     */
    private class NewQuestion implements Runnable {
        public void run() {
            if (mTimerValue > TIME_MAX) {
                showQuizStats(); // We are overdue, show the stats....
                return;          // do not generate a new question....
            }

            // Update the view with a new question......

            // Reset styles of the buttons

            // Clear the status text box

            // Fetch a new Question....

            // Update the view with the new question and button options.
        }

        public void showQuizStats() {
            // Start a new activity with the quiz stats passed in....
            Bundle quizStats = new Bundle();
            quizStats.putInt(KEY_QUESTION_CORRECT, mQuestionsCorrect);
            quizStats.putInt(KEY_QUESTION_WRONG, mQuestionsWrong);
            // more to do....


            QuizActivity.this.finish(); // Close the quiz activity
        }
    }
}
