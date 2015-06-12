package com.keeneye.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by bazilm on 10-06-2015.
 */

public class GetArtist extends AsyncTask<String,Void,ArrayList<GetArtist.Artist>>
{

    private final String TAG = GetArtist.class.getSimpleName();
    /*
    Get the json string from Spotify and parse it.
     */
    SearchActivity searchActivity =null;

    public GetArtist(SearchActivity searchActivity)
    {
        this.searchActivity = searchActivity;
    }

    @Override
    protected ArrayList<Artist> doInBackground(String... params) {

        //Build url
        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        String artistJson=null;




        try {
            String type = "artist";
            String artist = params[0];
            String limit = "10";

            String base_url = "https://api.spotify.com/v1/search";

            Uri uri = Uri.parse(base_url).buildUpon().appendQueryParameter("q", artist).
                        appendQueryParameter("type", type).appendQueryParameter("limit",limit).build();

            URL url = new URL(uri.toString());
            //setup HTTP connection

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if(inputStream==null)
            {
                //inputStream null
                return null;
            }


            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line=reader.readLine())!=null)
            {
                stringBuffer.append(line+"\n");
            }

            if (stringBuffer.length()==0)
            {
                //stringBuffer empty
                return null;
            }

            artistJson= stringBuffer.toString();

        }catch(IOException e)
        {
            Log.e(TAG, "Error"+ e);
            return null;
        }finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error IO", e);
                }

            }
        }

        try{
            return getArtistDetailsFromJson(artistJson);
        }catch(JSONException e)
        {
            Log.e(TAG,"Error Json",e);
        }
        //empty json
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Artist> artists) {

        ListAdapter listAdapter = SearchActivityFragment.getAdapter();
        listAdapter.clear();
        searchActivity.clearCache();

        if (artists!=null)
        {

            for(Artist artist : artists) {
                listAdapter.add(artist);
                          }


            Log.d(TAG,"Succesfully added");
        }
    }

    private ArrayList<Artist> getArtistDetailsFromJson(String artistJson) throws JSONException
    {

        final String type_name="artists";
        final String artists="items";
        final String name="name";
        final String id = "id";
        final String img = "images";

        ArrayList<Artist> artistObject= new ArrayList<Artist>();

        JSONObject artistsJsonObject = new JSONObject(artistJson);
        JSONObject artistsObject= artistsJsonObject.getJSONObject(type_name);
        JSONArray artistArray = artistsObject.getJSONArray(artists);

        for (int i=0;i<artistArray.length();i++)
        {
            String artistName = artistArray.getJSONObject(i).getString(name);
            String artistId = artistArray.getJSONObject(i).getString(id);
            JSONArray artistImgArray = artistArray.getJSONObject(i).getJSONArray(img);
            String artistImg =null;
            if(artistImgArray.length()>0) {
                artistImg = artistImgArray.getJSONObject(artistImgArray.length() - 1).getString("url");
            }
            Bitmap image = getImage(artistImg);
            artistObject.add(new Artist(artistName,artistId,image));
            Log.i(TAG,artistName+" "+artistId+" "+artistImg);
        }


        return artistObject;
    }

    public Bitmap getImage(String imageUrl){
        HttpURLConnection urlConnection=null;
        URL imgUrl = null;
        try {
            imgUrl = new URL(imageUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG,"ERROR",e);
            return null;
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
            return null;
        }finally{
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
        }


    }


    public class Artist
    {
        /*Class to store Artist Data*/
        private String name;
        private String id;
        private Bitmap image;

        public Artist(String name,String id,Bitmap image)
        {
            this.name=name;
            this.id=id;
            this.image=image;
        }

        String getName() {
            return this.name;
        }

        String getId(){
            return this.id;
        }

        Bitmap getImage(){
            return this.image;
        }
    }

}


