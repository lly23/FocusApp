package com.example.linhly.focusnow;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Loser extends AppCompatActivity {
    private TextView mTextView;
    private int mTimeLeftInMillis;
    private Button mButtonStart;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lose_screen);

        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                finish();
                Toast.makeText(getApplicationContext(),"Oops! You exited the app. Your pet has died.", Toast.LENGTH_LONG).show();
                goToLoseScreen();
            }

            @Override
            public void onHomeLongPressed() {

            }
        });
        mHomeWatcher.startWatch();

        ImageView cat = (ImageView) findViewById(R.id.pet);
        cat.setBackgroundResource(R.drawable.sad_cat_anim);
        ((AnimationDrawable)cat.getBackground()).start();

        mTextView = findViewById(R.id.countdown_view);
        mTextView.setVisibility(View.INVISIBLE);
        mButtonStart = findViewById(R.id.button_start);

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minutes;

                mButtonStart.setVisibility(View.INVISIBLE);

                minutes = 1 * 60000;

                mTimeLeftInMillis = minutes;
                Log.v("Time left in millis: ", "" + mTimeLeftInMillis);

                startTimer();
            }
        });

        updateCountDownText();
    }

    private void startTimer() {
        mTextView.setVisibility(View.VISIBLE);
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = (int) millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                back();
            }
        }.start();

        mTimerRunning = true;
    }

    private void updateCountDownText() {
        int minutes = (mTimeLeftInMillis / 1000) / 60;
        int seconds = (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextView.setText(timeLeftFormatted);
    }

    @Override
    public void onStop(){
        super.onStop();
        finish();
    }

    public void back() {
        Intent x = new Intent(this, MainActivity.class);
        startActivity(x);
    }

    public void goToLoseScreen() {
        Intent x = new Intent(this, Loser.class);
        startActivity(x);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Revive your pet!", Toast.LENGTH_LONG).show();
    }

}


