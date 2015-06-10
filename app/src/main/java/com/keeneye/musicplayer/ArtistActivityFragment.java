package com.keeneye.musicplayer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistActivityFragment extends Fragment {

    private String Tag = "Artist Activity";

    public ArtistActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText search = (EditText)getView().findViewById(R.id.search);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH)
                {
                    Toast.makeText(getActivity(),v.getText().toString(),Toast.LENGTH_LONG).show();
                    GetArtist getArtist = new GetArtist();
                    getArtist.execute(v.getText().toString());

                }

            return true;
            }
        });


    }

    private class GetArtist extends AsyncTask<String,Void,String>
    {

        private final String TAG = GetArtist.class.getSimpleName();
        /*
        Get the json string from Spotify and parse it.
         */
        @Override
        protected String doInBackground(String... params) {

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
                Log.e(TAG,"Error",e);
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

        private String getArtistDetailsFromJson(String artistJson) throws JSONException
        {
            return null;
        }
    }


}
