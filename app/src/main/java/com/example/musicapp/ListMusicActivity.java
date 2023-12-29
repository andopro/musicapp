package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListMusicActivity extends AppCompatActivity {


    //private static final int REQUEST_PERMISSION = 99;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 99;
    ArrayList<Song> songArrayList;
    ListView lvSong;
    SongAdapter songAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);

        lvSong = findViewById(R.id.lvSong);

        songArrayList = new ArrayList<>();


//        for (int i=1; i<=10; i++)
//            songArrayList.add(new Song("Song "+i,"Artist "+i,"Path "+i));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new  String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }else {
            getSong();
        }
        lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = songArrayList.get(position);
                Intent openMusicPlayer = new Intent(ListMusicActivity.this, MusicPlayerActivity.class);
                openMusicPlayer.putExtra("song",song);
                startActivity(openMusicPlayer);
            }
        });

        songAdapter = new SongAdapter(this,  songArrayList);
        lvSong.setAdapter(songAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSong();
            }
        }
    }


    

    private void getSong() {
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA
        };

        Cursor songCursor = null;
        try {
            songCursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );

            if (songCursor != null && songCursor.moveToFirst()) {
                int indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int indexArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int indexData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                do {
                    String title = songCursor.getString(indexTitle);
                    String artist = songCursor.getString(indexArtist);
                    String path = songCursor.getString(indexData);
                    Song song = new Song(title, artist, path);
                    songArrayList.add(song);
                } while (songCursor.moveToNext());
            }
        } finally {
            if (songCursor != null) {
                songCursor.close();
            }
        }
    }


}