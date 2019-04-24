package com.example.linhly.focusnow;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer= MediaPlayer.create(getApplicationContext(), R.raw.backgroundmusic);
        mPlayer.start();
    }

    public void next(View view) {
        mPlayer.stop();
        Intent x = new Intent(this, HomeScreen.class);
        startActivity(x);
    }

    @Override
    public void onStop(){
        super.onStop();
        finish();
    }
}
