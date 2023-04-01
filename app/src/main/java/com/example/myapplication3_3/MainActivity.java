package com.example.myapplication3_3;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private String nameAudio = "";
    private final String DATA_STREAM = "https://mp3melodii.ru/files_site_02/001/veselaya_melodiya_iz_peredachi_kalambur_derevnya_durakov.mp3";
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private TextView name;
    private Switch loop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        name = findViewById(R.id.name);
        loop = findViewById(R.id.switchLoop);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        loop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(mediaPlayer != null) {
                    mediaPlayer.setLooping(isChecked);
                    name.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
                            + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
                }
            }
        });
    }
    public void onClickSource(View view) throws IOException {
        releaseMediaPlayer();
        try {
            switch (view.getId()) {
                case R.id.btnStream:
                    Toast.makeText(this, "Музыка играет", Toast.LENGTH_SHORT).show();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(DATA_STREAM);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.prepareAsync();
                    nameAudio = "Песня, которую Слава скинул";
                    name.setText(nameAudio+"\n(проигрывание "+mediaPlayer.isPlaying()+", время "+mediaPlayer.getCurrentPosition()+",\nповтор "+mediaPlayer.isLooping()+", громкость "+audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)+")");
                    break;
                case R.id.btnRAW :
                    Toast.makeText(this, "Запущен аудио-файл с памяти телефона", Toast.LENGTH_SHORT).show();
                    mediaPlayer = MediaPlayer.create(this, R.raw.music3);
                    mediaPlayer.start();
                    nameAudio = "Просто какая-то музыка";
                    name.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
                            + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Источник информации не найден", Toast.LENGTH_SHORT).show();
        }
        if (mediaPlayer == null) return;
        mediaPlayer.setLooping(loop.isChecked());
        mediaPlayer.setOnCompletionListener(this);
    }
    public void onClick(View view) {
        if (mediaPlayer == null) return;
        switch (view.getId()) {
            case R.id.btnResume:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.btnPause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.btnStop:
                mediaPlayer.stop();
                break;
            case R.id.btnForward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                break;
            case R.id.btnBack:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                break;
        }
        name.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
                + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
    }
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        Toast.makeText(this, "Старт медиа-плейера", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Toast.makeText(this, "Отключение медиа-плейера", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}