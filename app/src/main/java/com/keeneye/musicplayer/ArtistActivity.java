package com.keeneye.musicplayer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class ArtistActivity extends ActionBarActivity {

    private LruCache<String,Bitmap> cache;
    int cacheSize;

    public LruCache<String,Bitmap> getCache(){
        return cache;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        final int max_memory=(int)(Runtime.getRuntime().maxMemory())/1024;
        cacheSize = max_memory/8;


        cache = new LruCache<String,Bitmap>(cacheSize){

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getRowBytes() * value.getHeight())/1024;
            }
        };
    }


    public void addToCache(String key,Bitmap bitmap)
    {
        if(getBitmapFromCache(key)==null)
        {
            if(bitmap!=null)
            cache.put(key,bitmap);
        }

    }

    public Bitmap getBitmapFromCache(String key)
    {
        return cache.get(key);
    }

    public void clearCache()
    {
        cache.evictAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist, menu);
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
}
