package com.keeneye.musicplayer;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;


public class MediaActivity extends ActionBarActivity {

    private final static String TAG = MediaActivity.class.getSimpleName();

    public String preview_url=null;
    public MediaPlayer mediaPlayer;
    public  GetResult<MediaPlayer> getMusic;
    public SeekBar seekbar = null;
    public android.os.Handler handler = null;
    public Runnable seekbarUpdation = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer.isPlaying())
            {
                seekbar.setProgress(mediaPlayer.getCurrentPosition()/1000);
                handler.postDelayed(this,1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        preview_url=getIntent().getStringExtra("preview_url");
        mediaPlayer = new MediaPlayer();
        getMusic = new GetResult<MediaPlayer>(MediaPlayer.class,mediaPlayer);
        getMusic.execute(preview_url);
        seekbar = (SeekBar)this.findViewById(R.id.seekbar);
        handler = new Handler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        getMusic.cancel(true);
        handler.removeCallbacks(seekbarUpdation);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();

    }

    @Override
    protected  void onResume()
    {
        super.onResume();
        if(!mediaPlayer.isPlaying()&&getMusic.getStatus()== AsyncTask.Status.FINISHED)
                mediaPlayer.start();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"Restarting");
        mediaPlayer = new MediaPlayer();
        getMusic = new GetResult<MediaPlayer>(MediaPlayer.class,mediaPlayer);
        getMusic.execute(preview_url);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void play(View v)  {

        if (getMusic.getStatus() == AsyncTask.Status.FINISHED)
        {
            if (!mediaPlayer.isPlaying()) {
                seekbar.setMax(mediaPlayer.getDuration() / 1000);
                mediaPlayer.start();
                handler.post(seekbarUpdation);


            }
        }

        else
            Toast.makeText(this,"Streaming Audio",Toast.LENGTH_LONG).show();



    }

    public void pause(View v)
    {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public void stop(View v)
    {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            seekbar.setProgress(0);
        }
    }
}
