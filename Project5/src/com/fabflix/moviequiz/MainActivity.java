package com.fabflix.moviequiz;

import android.app.Activity;
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
        newQuizButton.setOnClickListener(new StatsButtonListener());

        new MoviesDBHelper(this).open().close(); // open and closed connection...

    }


    private class NewQuizListener implements View.OnClickListener {
        public void onClick(View btn) {
            // Start New Quiz Activity Here...
        }
    }

    private class StatsButtonListener implements View.OnClickListener {
        public void onClick(View btn) {
            // Start New Quiz Activity Here...
        }
    }
}
