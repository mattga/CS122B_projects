package com.fabflix.moviequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fabflix.moviequiz.Helpers.MoviesDBHelper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newQuizButton = (Button)findViewById(R.id.take_quiz);
        newQuizButton.setOnClickListener(new NewQuizListener());

        Button viewStatsButton = (Button)findViewById(R.id.see_stats);
        viewStatsButton.setOnClickListener(new StatsButtonListener());

        // Open & Close Connection to Trigger Creation of the database if one does not exist.
        new MoviesDBHelper(this).open().close();
    }


    private class NewQuizListener implements View.OnClickListener {
        public void onClick(View btn) {
            Intent newQuiz = new Intent(MainActivity.this, QuizActivity.class);
            MainActivity.this.startActivity(newQuiz);
        }
    }

    private class StatsButtonListener implements View.OnClickListener {
        public void onClick(View btn) {
            Intent newStats = new Intent(MainActivity.this, StatsActivity.class);
            MainActivity.this.startActivity(newStats);
        }
    }
}
