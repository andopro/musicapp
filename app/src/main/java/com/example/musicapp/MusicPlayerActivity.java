package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity implements OnClickListener {

    TextView tvTime,tvDuration,tvTitle,tvArtist;
    SeekBar SeekBarTime, SeekBarVolume;
    Button btnPlay;

    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Song song = (Song) getIntent().getSerializableExtra("song");



        tvTime = findViewById(R.id.tvTime);
        tvDuration = findViewById(R.id.tvDuration);
        SeekBarTime = findViewById(R.id.SeekBarTime);
        SeekBarVolume = findViewById(R.id.SeekBarVolume);
        btnPlay = findViewById(R.id.btnPlay);
        tvArtist = findViewById(R.id.tvArtist);
        tvTitle= findViewById(R.id.tvTitle);


        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());

        musicPlayer = new  MediaPlayer();
        try {
            musicPlayer.setDataSource(song.getPath());
            musicPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f, 1f);

        String duration = millisecondsToString(musicPlayer.getDuration());
        tvDuration.setText(duration);

         //musicPlayer.start();
        
        btnPlay.setOnClickListener(this);


        SeekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 200f;
                musicPlayer.setVolume(volume,volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBarTime.setMax(musicPlayer.getDuration());
        SeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (musicPlayer != null) {
                    if(musicPlayer.isPlaying()){
                        try{
                            final double current = musicPlayer.getCurrentPosition();
//                            double duration = musicPlayer.getDuration();
//                            final double position = (100.0/duration) * current;
                            final String elapsedTime = millisecondsToString((int) current);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTime.setText(elapsedTime);
                                    SeekBarTime.setProgress((int) current);

                                }
                            });


                            Thread.sleep(1000);

                        }catch (InterruptedException e) {

                        }
                    }
                }
            }


        }).start();


    } //end main

    public String millisecondsToString(int time){
        String elapsedTime = "";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elapsedTime = minutes+":";
        if(seconds < 10){
            elapsedTime += "0";
        }
        elapsedTime += seconds;

        return elapsedTime;

    }

    @Override
    public void onClick(View v) {

            if(musicPlayer.isPlaying()){
                musicPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.ic_play);
            }else{
                musicPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
            }
        }




//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == android.R.id.home) {
//
//            if (musicPlayer != null && musicPlayer.isPlaying()){
//                musicPlayer.stop();
//            }
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}