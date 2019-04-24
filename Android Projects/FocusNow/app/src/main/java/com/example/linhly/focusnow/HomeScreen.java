package com.example.linhly.focusnow;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class HomeScreen extends AppCompatActivity {
    private TextView mTextView;
    ImageView cat;
    Spinner dropdown;
    ImageView dataIcon;
    private Button mButtonStart;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private int mTimeLeftInMillis;
    private String strInput;
    ArrayList<Integer> timeCollectedArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mTimerRunning == true) {
                    finish();
                    Toast.makeText(getApplicationContext(),"Oops! You exited the app. Your pet has died.", Toast.LENGTH_LONG).show();
                    goToLoseScreen();
                }
            }

            @Override
            public void onHomeLongPressed() {

            }
        });
        mHomeWatcher.startWatch();

        cat = (ImageView) findViewById(R.id.pet);
        cat.setBackgroundResource(R.drawable.happy_cat_anim);
        ((AnimationDrawable)cat.getBackground()).start();
        dataIcon = findViewById(R.id.screen_time);

        mTextView = findViewById(R.id.countdown_view);
        dropdown  = findViewById(R.id.spinner1);
        String[] items = new String[]{"1", "5", "10", "20", "30", "40", "50", "60"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        mButtonStart = findViewById(R.id.button_start);

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minutes;
                cat.setBackgroundResource(R.drawable.happy_cat_anim);
                ((AnimationDrawable)cat.getBackground()).start();
                mButtonStart.setVisibility(View.INVISIBLE);
                dataIcon.setVisibility(View.INVISIBLE);
                strInput = dropdown.getSelectedItem().toString();

                minutes = Integer.parseInt(strInput) * 60000;

                mTimeLeftInMillis = minutes;
                Log.v("Time left in millis: ", "" + mTimeLeftInMillis);

                startTimer();
            }
        });

        updateCountDownText();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = (int) millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                int time = Integer.parseInt(strInput);
                timeCollectedArray.add(time);
                mTimerRunning = false;
                resetTimer();
                cat.setBackgroundResource(R.drawable.loving_cat_anim);
                ((AnimationDrawable)cat.getBackground()).start();
            }
        }.start();

        mTimerRunning = true;
    }

    private void resetTimer() {
        mTextView.setText("");
        updateCountDownText();
        mButtonStart.setVisibility(View.VISIBLE);
        dataIcon.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (mTimeLeftInMillis / 1000) / 60;
        int seconds = (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextView.setText(timeLeftFormatted);
    }

    public void goToData(View view) {
        Intent x = new Intent(this, ScreenTime.class);
        x.putIntegerArrayListExtra("data_array", timeCollectedArray);
        startActivity(x);
    }

    public void goToLoseScreen() {
        Intent x = new Intent(this, Loser.class);
        startActivity(x);
    }

    @Override
    public void onBackPressed() {
        if (mTimerRunning == true) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to go back? Your pet will die.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            HomeScreen.this.finish();
                            goToLoseScreen();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        finish();
    }

}

