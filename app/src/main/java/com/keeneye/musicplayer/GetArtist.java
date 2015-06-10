package com.keeneye.musicplayer;

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
    @Override
    protected ArrayList<Artist> doInBackground(String... params) {

        //Build url
        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        String artistJson=null;


        try {
            String type = "artist";
            String artist = params[0];

            String base_url = "https://api.spotify.com/v1/search";

            Uri uri = Uri.parse(base_url).buildUpon().appendQueryParameter("q", artist).appendQueryParameter("type", type).build();

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
            Log.e(TAG, "Error", e);
            return null;
        }finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error", e);
                }

            }
        }

        try{
            return getArtistDetailsFromJson(artistJson);
        }catch(JSONException e)
        {
            Log.e(TAG,"Error",e);
        }
        //empty json
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Artist> artists) {
        if (artists!=null)
        {
            for(Artist artist : artists)
            ArtistActivityFragment.getAdapter().add(artist);

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
            artistObject.add(new Artist(artistName,artistId,artistImg));
            Log.i(TAG,artistName+" "+artistId+" "+artistImg);
        }


        return artistObject;
    }


    public class Artist
    {
        /*Class to store Artist Data*/
        private String name;
        private String id;
        private String imgUrl;

        public Artist(String name,String id,String imgUrl)
        {
            this.name=name;
            this.id=id;
            this.imgUrl=imgUrl;
        }

        String getName() {
            return this.name;
        }

        String getId(){
            return this.id;
        }

        String getImgUrl(){
            return this.imgUrl;
        }
    }

}


