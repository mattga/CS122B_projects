package com.fabflix.moviequiz;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatsActivity extends Activity {
    private TextView mTextViewTaken;
	private TextView mTextViewAnswered;
    private TextView mTextViewCorrect;
    private TextView mTextViewWrong;
    private TextView mTextViewAvgResponse;
    
    public static final String KEY_IS_LIFETIME = "LIFETIME_STATS";
    private boolean mIsLifeTime = false;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle stats = getIntent().getExtras();
        if (stats.getBoolean(KEY_IS_LIFETIME)) {
        	mIsLifeTime = true;
        	setContentView(R.layout.activity_lifetime_stats);
        } else {
        	mIsLifeTime = false;
        	setContentView(R.layout.activity_single_quiz_stats);
        }
        
        // Get the Text View References......
        bindTextViewsToVariables();

        // Calculate thea appropriate values from the information passed in.
        calculateStatsFromBundle();

        Button buttonFinish;
        if (mIsLifeTime)
        	buttonFinish = (Button)(findViewById(R.id.lifetime_buttonFinish));
        else 
        	buttonFinish = (Button)(findViewById(R.id.single_buttonFinish));
        
        buttonFinish.setOnClickListener(new ExitEventListener());
    }

    private void calculateStatsFromBundle() {
        int totalQuestions;
    	if (mIsLifeTime) {
    		// Load information from the lifetime data stores in main memory.
        	LifetimeData ld = new LifetimeData();
        	totalQuestions = ld.totalCorrect + ld.totalWrong;
        	mTextViewTaken.setText("Total Quizes Taken: "+ ld.totalQuizes);
            mTextViewAnswered.setText("Questions Answered: "+ totalQuestions);
            mTextViewWrong.setText("Wrong Answers: " + ld.totalWrong);
            mTextViewCorrect.setText("Correct Answers: " + ld.totalCorrect);
            mTextViewAvgResponse.setText(String.format("Seconds Per Question: %.2f",  (1.0f * ld.totalTime / (totalQuestions * 1000))));
        } else {
        	// Load information from the bundle passed to the activity.
        	Bundle stats = getIntent().getExtras();
            totalQuestions = stats.getInt(QuizActivity.KEY_QUESTION_CORRECT) + stats.getInt(QuizActivity.KEY_QUESTION_WRONG);
            mTextViewAnswered.setText("Questions Answered: "+ totalQuestions);
            mTextViewWrong.setText("Wrong Answers: " + stats.getInt(QuizActivity.KEY_QUESTION_WRONG));
            mTextViewCorrect.setText("Correct Answers: " + stats.getInt(QuizActivity.KEY_QUESTION_CORRECT));
            mTextViewAvgResponse.setText(String.format("Seconds Per Question: %.2f",  (1.0f * stats.getLong(QuizActivity.KEY_TIME_ELAPSED) / (totalQuestions * 1000))));
        }

    }

    private void bindTextViewsToVariables() {
    	if (mIsLifeTime) {
    		mTextViewTaken = (TextView)(findViewById(R.id.lifetime_textViewTaken));
    		mTextViewAnswered = (TextView)(findViewById(R.id.lifetime_textViewAnswered));
	        mTextViewCorrect = (TextView)(findViewById(R.id.lifetime_textViewCorrect));
	        mTextViewWrong = (TextView)(findViewById(R.id.lifetime_textViewWrong));
	        mTextViewAvgResponse = (TextView)(findViewById(R.id.lifetime_textViewAvgResponse));
    	} else {
    		mTextViewAnswered = (TextView)(findViewById(R.id.single_textViewAnswered));
	        mTextViewCorrect = (TextView)(findViewById(R.id.single_textViewCorrect));
	        mTextViewWrong = (TextView)(findViewById(R.id.single_textViewWrong));
	        mTextViewAvgResponse = (TextView)(findViewById(R.id.single_textViewAvgResponse));
    	}
    }

    private class ExitEventListener implements View.OnClickListener {
        public void onClick(View v) {
        	// If this is lifetime view, there is no data to update...
        	if (StatsActivity.this.mIsLifeTime) {
        		StatsActivity.this.finish();
        		return;
        	}
        	
        	// Load the lifetime data...
        	LifetimeData ld = new LifetimeData();
        	// Update with bundle information from quiz activity.
        	Bundle stats = getIntent().getExtras();
//            int totalQuestions = stats.getInt(QuizActivity.KEY_QUESTION_CORRECT) + stats.getInt(QuizActivity.KEY_QUESTION_WRONG);
            ld.totalCorrect = ld.totalCorrect + stats.getInt(QuizActivity.KEY_QUESTION_CORRECT);
        	ld.totalWrong = ld.totalWrong + stats.getInt(QuizActivity.KEY_QUESTION_WRONG);
        	ld.totalTime = ld.totalTime + stats.getLong(QuizActivity.KEY_TIME_ELAPSED);
        	ld.totalQuizes++;
        	ld.saveData();
            
            StatsActivity.this.finish();
        }
    }
    
    /**
     * Encapsulate the lifetime Data Load and update procedures.
     *
     */
    private class LifetimeData {
    	public int totalQuizes;
    	public int totalCorrect;
    	public int totalWrong;
    	public long totalTime;
    	
    	private int defaultValue = 0;
    	private SharedPreferences lifetimeData;
    	private boolean isLoaded = false;
    	
    	public LifetimeData() {
    		loadData();
    	}
    	
    	public LifetimeData loadData() {
    		// Get Current Lifetime Data.
            lifetimeData = StatsActivity.this.getPreferences(MODE_PRIVATE);
            totalQuizes = lifetimeData.getInt(QuizActivity.KEY_QUIZES_TAKEN, defaultValue);
            totalCorrect = lifetimeData.getInt(QuizActivity.KEY_QUESTION_CORRECT, defaultValue);
            totalWrong = lifetimeData.getInt(QuizActivity.KEY_QUESTION_WRONG, defaultValue);
            totalTime = lifetimeData.getLong(QuizActivity.KEY_TIME_ELAPSED, 0);
            
            isLoaded = true;
            return this;
    	}
    	
    	public void saveData() {
    		if (!isLoaded)
    			return; // no data to update...we should only modify data after loading..
    		
    		SharedPreferences.Editor updateLifetimeData = lifetimeData.edit();
    		updateLifetimeData.putInt(QuizActivity.KEY_QUIZES_TAKEN, totalQuizes);
            updateLifetimeData.putInt(QuizActivity.KEY_QUESTION_CORRECT, totalCorrect);
            updateLifetimeData.putInt(QuizActivity.KEY_QUESTION_WRONG, totalWrong);
            updateLifetimeData.putLong(QuizActivity.KEY_TIME_ELAPSED, totalTime);
            updateLifetimeData.apply();
    	}
    }
}
