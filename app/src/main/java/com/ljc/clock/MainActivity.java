package com.ljc.clock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljc.clock.myview.ClockView;

public class MainActivity extends AppCompatActivity {
    ClockView clockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clockView = (ClockView)findViewById(R.id.clock_view);
        clockView.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        clockView.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        clockView.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}