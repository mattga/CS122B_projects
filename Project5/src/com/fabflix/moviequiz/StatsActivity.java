package com.fabflix.moviequiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatsActivity extends Activity {
    private TextView mTextViewAnswered;
    private TextView mTextViewCorrect;
    private TextView mTextViewWrong;
    private TextView mTextViewAvgResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_quiz_stats);

        // Get the Text View References......
        bindTextViewsToVariables();

        // Calculate thea appropriate values from the information passed in.
        calculateStatsFromBundle();

        Button buttonFinish = (Button)(findViewById(R.id.buttonFinish));
        buttonFinish.setOnClickListener(new ExitEventListener());
    }

    private void calculateStatsFromBundle() {
        Bundle stats = getIntent().getExtras();
        int totalQuestions = stats.getInt(QuizActivity.KEY_QUESTION_CORRECT) + stats.getInt(QuizActivity.KEY_QUESTION_WRONG);
        mTextViewAnswered.setText("Questions Answered: "+ totalQuestions);
        mTextViewWrong.setText("Wrong Answers: " + stats.getInt(QuizActivity.KEY_QUESTION_WRONG));
        mTextViewCorrect.setText("Correct Answers: " + stats.getInt(QuizActivity.KEY_QUESTION_CORRECT));
        // TODO: There is something off with the Time Calculated.
        mTextViewAvgResponse.setText(String.format("Seconds Per Question: %.2f",  (1.0f * stats.getLong(QuizActivity.KEY_TIME_ELAPSED) / (totalQuestions * 1000))));
    }

    private void bindTextViewsToVariables() {
        mTextViewAnswered = (TextView)(findViewById(R.id.textViewAnswered));
        mTextViewCorrect = (TextView)(findViewById(R.id.textViewCorrect));
        mTextViewWrong = (TextView)(findViewById(R.id.textViewWrong));
        mTextViewAvgResponse = (TextView)(findViewById(R.id.textViewAvgResponse));
    }


    private class ExitEventListener implements View.OnClickListener {
        public void onClick(View v) {
            // TODO: UPDATE GLOBAL STATS DATASTORE
            StatsActivity.this.finish();
        }
    }
}
