package com.keeneye.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bazilm on 11-06-2015.
 */
public class GetImage extends AsyncTask<String,Void,Bitmap> {

    private final String TAG = GetImage.class.getSimpleName();
    private ImageView imageView=null;

    public GetImage(ImageView imgView)
    {
        this.imageView=imgView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        HttpURLConnection urlConnection=null;
        URL imgUrl = null;
        try {
             imgUrl = new URL(params[0]);
        } catch (MalformedURLException e) {
            Log.e(TAG,"ERROR",e);
        }

        try
        {
            urlConnection = (HttpURLConnection)imgUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode==200)
            {
                return BitmapFactory.decodeStream(urlConnection.getInputStream());
            }
            else
            {
                return null;
            }
        }catch (IOException e)
        {
         Log.e(TAG,"ERROR",e);
        }finally{
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);


    }
}
