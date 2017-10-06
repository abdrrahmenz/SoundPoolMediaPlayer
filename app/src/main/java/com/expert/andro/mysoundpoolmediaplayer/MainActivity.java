package com.expert.andro.mysoundpoolmediaplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSoundPool,btnMedia,btnMediaStop;
    SoundPool soundPool;
    Intent intent;
    int wav;
    boolean soundpoolloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSoundPool = (Button) findViewById(R.id.btn_soundpool);
        btnMedia = (Button) findViewById(R.id.btn_mediaplayer);
        btnMediaStop = (Button) findViewById(R.id.btn_mediaplayer_stop);
        btnMedia.setOnClickListener(this);
        btnMediaStop.setOnClickListener(this);
        btnSoundPool.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                soundpoolloaded = true;
            }
        });

        intent = new Intent(this, MediaService.class);
        intent.setAction(MediaService.ACTION_CREATE);
        intent.setPackage(MediaService.ACTION_PACKAGE);
        startService(intent);

        wav = soundPool.load(this, R.raw.clinking_glasses, 1);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_soundpool:
                if (soundpoolloaded == true){
                    soundPool.play(wav,1,1,0,2,1);
                }
                break;
            case R.id.btn_mediaplayer:
                intent.setAction(MediaService.ACTION_PLAY);
                intent.setPackage(MediaService.ACTION_PACKAGE);
                startService(intent);
                break;
            case R.id.btn_mediaplayer_stop:
                intent.setAction(MediaService.ACTION_STOP);
                intent.setPackage(MediaService.ACTION_PACKAGE);
                startService(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
}
