package com.apexis.viewplayground;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScoringSlider customSlider = (ScoringSlider) findViewById(R.id.scoringSlider);
    }
}
