package com.fabflix.moviequiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
    public final static String KEY_QUESTION_COUNT = "COUNT";
    public final static String KEY_QUESTION_CORRECT = "CORRECT";
    public final static String KEY_QUESTION_WRONG = "WRONG";
    public final static String KEY_TIME_ELAPSED = "TIME_ELAPSED";

    private static int mCorrectAnswerIndex;
    private final static int QUESTION_DELAY = 1500; // 1.5 second delay.
//    private final static long TIME_MAX = 180000; // 3 minute max in milliseconds
    private final static long TIME_MAX = 10000; // 3 minute max in milliseconds
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

        // Initialize the first question....
        mScheduler.post(new NewQuestionAction());
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
        public void onClick(View view) {
            Log.w("BUTTON CLCIKED", "Answer Clicked.....");
            // update the timer....
            mTimerValue += (System.currentTimeMillis() - mStartTime);

            // Check if the question was answered correctly.
            Button btn = (Button)view;

            if (evaluateAnswer(btn))
                QuizActivity.this.mQuestionsCorrect++;
            else
                QuizActivity.this.mQuestionsWrong++;

            // Set a time-out to move to the next question...
            new Handler().postDelayed(new NewQuestionAction(), QUESTION_DELAY);
        }


        private boolean evaluateAnswer(Button btn) {
            int colorCorrect = Color.GREEN;
            int colorWrong = Color.RED;

            // Set the background color of the correct answer green
            mButtons[mCorrectAnswerIndex].setBackgroundColor(colorCorrect);

            // Did we click the same button?
            // The instance of the buttons is always the same, buttons are reused....
            if (mButtons[mCorrectAnswerIndex] != btn) {
                btn.setBackgroundColor(colorWrong);
                return false;
            }
            return true;
        }
    }

    /**
     * Class implements runnable, and is called whenever a new question needs to be generated.
     */
    private class NewQuestionAction implements Runnable {
        public void run() {
            Log.w("DELAYED", "QUESTION RUNNING");
            // Update view with new title....
            int totalQuestions = 1 + QuizActivity.this.mQuestionsCorrect + QuizActivity.this.mQuestionsWrong;
            QuizActivity.this.mTextViewQuestionNumber.setText("Question #"+  totalQuestions);

            // Fetch a new Question....
            // TODO: FETCH A NEW QUESTION.
            mCorrectAnswerIndex = (mCorrectAnswerIndex + 1) % 4; // Update the index of the correct index
            String question = "This is a new question: "+ totalQuestions;

            // Load New Questions...
            QuizActivity.this.mTextViewQuestion.setText(question);

            // Reset styles of the buttons
            for (Button btn : QuizActivity.this.mButtons)
                btn.setBackgroundColor(Color.DKGRAY);
        }
    }

    /**
     * Implements runnable, and is called whenever the time expires.
     */
    private class TimeUpAction implements Runnable {
        public void run() {
            Log.e("TIME UP", "SWITCHING TO STATS ACTIVITY");
            mTimerValue = System.currentTimeMillis() - mStartTime;
            // Start a new activity with the quiz stats passed in....
            Bundle quizStats = new Bundle();
            quizStats.putInt(KEY_QUESTION_CORRECT, mQuestionsCorrect);
            quizStats.putInt(KEY_QUESTION_WRONG, mQuestionsWrong);
            quizStats.putLong(KEY_TIME_ELAPSED, mTimerValue);

            // Create a new intent to show the stats, and pass in the info from this quiz.
            Intent statsActivity = new Intent(QuizActivity.this, StatsActivity.class);
            statsActivity.putExtras(quizStats);
            QuizActivity.this.startActivity(statsActivity);

            // NoHistory preference in Manifest calls finish automagically....
            // QuizActivity.this.finish(); // Close the quiz activity
        }
    }
}
