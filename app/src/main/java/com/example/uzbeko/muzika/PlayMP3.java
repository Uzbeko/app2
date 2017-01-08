package com.example.uzbeko.muzika;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Ed on 7/30/2015.
 */
public class PlayMP3 extends AsyncTask {

    private MediaPlayer player;
    private Head_app_1 act_refer;
    MyRunnable gija;
    //-------------------------------------------------------------------------------------------------
    class MyRunnable extends Thread {

        boolean bool = true;
        private Object mPauseLock;
        private boolean mPaused;
        private boolean mFinished;

        public MyRunnable(String name){
            super(name);
        }

        public void  onPause(){
            synchronized (mPauseLock){
                mPaused = true;
            }
        }
        public void onResume(){
            synchronized (mPauseLock){
                mPaused = false;
                mPauseLock.notifyAll();
            }
        }

        @Override
        public void run() {
            mPauseLock = new Object();
            mPaused = false;
            mFinished = false;

            while (bool) {

                synchronized (mPauseLock){
                    while (mPaused){
                        try{
                            mPauseLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                try {
                    act_refer.seek.setProgress(player.getCurrentPosition());

                    gija.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    bool = false;
                }
            }
        }
    }


    //--------------------------------------------------------------------------------------------------
    @Override
    protected Object doInBackground(Object[] params) {

        act_refer = (Head_app_1) params[0];
        player = (MediaPlayer) params [1];

        try {
            player.setDataSource(act_refer, Uri.parse("android.resource://com.example.uzbeko.muzika/" + R.raw.audio));
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepareAsync();

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();


// with hendlear----------------------------------------------------------------

                    gija = new MyRunnable("progres_Bar_Slide");
                    gija.start();
                }

            });

            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                }
            });

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    gija.interrupt();
                    mp.release();
                    player = null;
                    act_refer.setMediaPlayer(player);

                }
            });
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;

                }
            });
            player.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
                @Override
                public void onTimedText(MediaPlayer mp, TimedText text) {

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        //-----------------------------------------------------------------
        return player;
    }
    //--------------------------------------------------------------------------------------------------
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        act_refer.setMediaPlayer(player);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        act_refer.seek.incrementProgressBy((Integer)values[0]);
    }

}
