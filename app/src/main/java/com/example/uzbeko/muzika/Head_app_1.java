package com.example.uzbeko.muzika;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;


public class Head_app_1 extends Activity {
    boolean show = true;
    ImageView imgInst = null;
    boolean off_on = true;
    Bitmap img = null;
    int displayWidth = 240;
    PlayMP3 pl;
    MediaPlayer my_pl;
    SeekBar seek;

    ScheduledExecutorService scheduledExecutorService;

//------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_app_1);

//--Finish---------------------------------------------------------------
        Button b_finish = (Button) findViewById(R.id.finish);
        b_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_pl.release();
                pl.gija.bool = false;
                Head_app_1.this.finish();
            }
        });
//--Pause----------------------------------------------------------------
        Button b_pause = (Button) findViewById(R.id.pause);

        b_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(my_pl.isPlaying() && my_pl != null){
                    my_pl.pause();
                    pl.gija.onPause();
                }else {
                    my_pl.start();
                    pl.gija.onResume();
                }
            }
        });

//--slider---------------------------------------------------------------
        final TextView textV = (TextView) findViewById(R.id.number_seekbar);

        seek = (SeekBar) findViewById(R.id.seekBar);
        seek.setProgress(50000);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textV.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(my_pl != null) {
                    pl.gija.onPause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(my_pl != null) {
                    my_pl.seekTo(seek.getProgress());
                    pl.gija.onResume();
                }

            }
        });
    //--audio player---------------------------------------------------------

        Button b1 = (Button) findViewById(R.id.audio);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               if(my_pl == null) {
                   my_pl = new MediaPlayer();
                   Object[] arr = {Head_app_1.this, my_pl};
                   pl = (PlayMP3) new PlayMP3().execute(arr);
               }
            }
        });

  }

    @Override
    protected void onPause() {
        super.onPause();
        my_pl.release();
        pl.gija.bool = false;
        Head_app_1.this.finish();
        Toast.makeText(getBaseContext(), "onPause ", Toast.LENGTH_LONG).show();
    }

//--------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_head_app_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//---methods---------------------------------------------------------------
    public void rodyk(View view){

        if(show) {
            Toast.makeText(this, "Toast message: On", Toast.LENGTH_SHORT).show();

            TextView tekstas = (TextView) findViewById(R.id.tekstas);
            tekstas.setText("View.VISIBLE");
            tekstas.setVisibility(View.VISIBLE);
    //------mazinu paveiksleli--------------------------------------------------------------------------
            BitmapFactory.Options options= new BitmapFactory.Options();
            options.inJustDecodeBounds= true;

            BitmapFactory.decodeResource(getResources(), R.drawable.pav,options);

            int width = options.outWidth;
            int height = options.outHeight;

            if (width > displayWidth) {
                int widthRatio = Math.round((float) width / (float) displayWidth);
                options.inSampleSize = widthRatio;
            }
            options.inJustDecodeBounds = false;
            img = (Bitmap) BitmapFactory.decodeResource(getResources(), R.drawable.pav,options);
    //----------------------------------------------------------------------------------------------

            imgInst = (ImageView) findViewById(R.id.img);
            imgInst.setImageBitmap(img);
            show = false;
        }else {
            if(off_on) {
                imgInst.setVisibility(View.INVISIBLE);
                off_on = false;
                Toast.makeText(this, "Toast message: Off", Toast.LENGTH_SHORT).show();

            } else{
                imgInst.setVisibility(View.VISIBLE);
                off_on = true;
                Toast.makeText(this, "Toast message: On", Toast.LENGTH_SHORT).show();
            }
        }

    }

//---seter and getter-------------------------------------------------------
    public void setMediaPlayer(MediaPlayer reference){
        my_pl = reference;
    }
}
