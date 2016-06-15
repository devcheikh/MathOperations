package com.basic.operations.math.mathoperations.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.basic.operations.math.mathoperations.R;

public class Main_Music extends Service {

    static MediaPlayer mp;

    @Override
    public void onCreate() {
        mp = MediaPlayer.create(this, R.raw.background_main_music);
        mp.setLooping(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mp.isPlaying()) {
            mp.start();
        }

        return 1;
    }



    @Override
    public void onDestroy() {
        if (mp.isPlaying()) {
            mp.stop();
        }
        mp.release();
    }

}
