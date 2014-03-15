package com.fabflix.moviequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class QuizActivity extends Activity {
    /**
     * Keys used for storing state between Activity Life Cycle.
     * Keys correspond to integer values inside a Bundle.
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


    // Keep reference to our view items
    private Button[] mButtons = new Button[4];
    private TextView mTextViewQuestion;
    private TextView mTextViewQuestionNumber;

    // Used to schedule events in the future.
    private Handler mScheduler = new Handler();

    /**
     * Method is called the first time the activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Build the References to our buttons for accecability later in out application
        int[] ids = new int[] {R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4};
        for (int i = 0; i < ids.length; i++) {
            mButtons[i] = (Button)(findViewById(ids[i]));  // store a reference
            mButtons[i].setOnClickListener(new AnswerEventListener()); // Set the event listener for the buttons
        }

        // Save references to our text views..
        mTextViewQuestion = (TextView)(findViewById(R.id.textViewQuestion));
        mTextViewQuestionNumber = (TextView)(findViewById(R.id.textViewQuestionNumber));

    }

    /**
     * Method is called when the actitivy goes out of focus
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Cancel all call-backs...
        mScheduler.removeCallbacksAndMessages(null);
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
     * Mothod is called after the activity is restarted..
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
        rescheduleTimeUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rescheduleTimeUp(); // Called on Create, and anytime
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void rescheduleTimeUp() {
        // Reschedule the Time-Up operation onCreate, onResume.
        mScheduler.postDelayed(new TimeUpAction(), TIME_MAX - mTimerValue);
    }
    /**
     * OnClickListener for every answer.
     */
    private class AnswerEventListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.w("BUTTON CLCIKED", "Answer Clicked.....");
            // update the timer....
            mTimerValue += (System.currentTimeMillis() - mStartTime);
            //Log.e("TIME ELAPSED", ""+ mTimerValue/1000);

            Button btn = (Button)view;
            String answer = btn.getText().toString();

            // Set a time-out to move to the next question...
            new Handler().postDelayed(new NewQuestionAction(), QUESTION_DELAY);
        }
    }

    /**
     * Class implements runnable, and is called whenever a new question needs to be generated.
     */
    private class NewQuestionAction implements Runnable {
        @Override
        public void run() {
            Log.w("DELAYED", "QUESTION RUNNIGN");
            // Update the view with a new question......

            // Reset styles of the buttons

            // Clear the status text box

            // Fetch a new Question....

            // Update the view with the new question and button options.
        }
    }


    private class TimeUpAction implements Runnable {
        @Override
        public void run() {
            Log.e("TIME UP", "SWITCHING TO STATS ACTIVITY");

            // Start a new activity with the quiz stats passed in....
            Bundle quizStats = new Bundle();
            quizStats.putInt(KEY_QUESTION_CORRECT, mQuestionsCorrect);
            quizStats.putInt(KEY_QUESTION_WRONG, mQuestionsWrong);

            // Create a new intent to show the stats, and pass in the info from this quiz.
            Intent statsActivity = new Intent(QuizActivity.this, StatsActivity.class);
            statsActivity.putExtras(quizStats);
            QuizActivity.this.startActivity(statsActivity);

            QuizActivity.this.finish(); // Close the quiz activity
        }
    }
}
