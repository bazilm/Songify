
package com.keeneye.musicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.RetrofitError;


/**
    Created by bazilm on 10-06-2015.

    Does all the background tasks.

  */

public class GetResult<T> extends AsyncTask<String, Void, T>

{

    private final String TAG = GetResult.class.getSimpleName();

    ListAdapter listAdapter=null;
    TextView textView;
    Class<T> type ;
    MediaPlayer mediaPlayer;


    public GetResult(ListAdapter listAdapter,Class<T> type)
    {
        this.listAdapter = listAdapter;
        this.type = type;
        this.mediaPlayer=null;
        this.textView=null;
    }
    public GetResult(ListAdapter listAdapter,TextView textView,Class<T> type)
    {
        this.listAdapter = listAdapter;
        this.type = type;
        this.mediaPlayer=null;
        this.textView=textView;
    }

    public GetResult(Class<T> type,MediaPlayer mediaPlayer)
    {
        this.type=type;
        this.listAdapter=null;
        this.mediaPlayer=mediaPlayer;
        this.textView=null;
    }

    @Override
    protected T doInBackground(String... params) {

        SpotifyApi spotifyApi = new SpotifyApi();
        SpotifyService spotifyService = spotifyApi.getService();


        //Gets the results based on type variable.
        //Spotify Api calls go here.

        if (type == ArtistsPager.class) {
            T artists = null;
            try {
                artists = (T) spotifyService.searchArtists(params[0]);
                Log.d(TAG, "Artists found");
            }catch(RetrofitError e)
            {
                Log.d(TAG,"Internet Connection Error");
            }
            return artists;
        }

        else if(type == Tracks.class)
        {
            Map query = new HashMap();
            query.put("country","SE");
            T tracks = null;

            try {
                tracks = (T) spotifyService.getArtistTopTrack(params[0], query);
                Log.d(TAG, "Tracks found");
            }catch(RetrofitError e)
            {
                Log.d(TAG,"Internet Connection Error");
            }
            return tracks;
        }

        else if(type==TracksPager.class)
        {
            T tracks = null;
            try{
                tracks = (T)spotifyService.searchTracks(params[0]);
                Log.d(TAG,"Tracks Found");
            }catch (RetrofitError e)
            {
                Log.d(TAG,"Internet Connection Error");
            }

            return tracks;


        }

        else if (type == AlbumsPager.class)
        {
            T albums = null;

            try{
                albums = (T)spotifyService.searchAlbums(params[0]);
                Log.d(TAG,"Albums Found");
            }catch (RetrofitError e)
            {
                Log.d(TAG,"Internet Connection Error");
            }
            return albums;
        }

        else if (type == AlbumSimple.class)
        {
            Album album=null;
            try {
                album = spotifyService.getAlbum(params[0]);
            }
            catch(RetrofitError  e)
            {
                Log.d(TAG,"Internet Connection error");
            }

            return (T)album;

        }

        //Setting up mediaPlayer.

        else if (type == MediaPlayer.class)
        {

                String url = params[0];
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    Log.d(TAG, "Error preview url");
                }
                return (T) mediaPlayer;



        }


        else {
            Log.d(TAG, "Return Type NULL");
            return null;
        }
    }

    @Override
    protected void onPostExecute(T results) {

            //Clearing the status textView.
           if(textView!=null) {

               textView.setText("");
           }


        // Checking the results and adding them to ListAdapter

           if (type == ArtistsPager.class) {
                listAdapter.clear();

               List<Artist> artists = ((ArtistsPager) results).artists.items;

               if(artists.size()!=0) {
                   for (Artist artist : artists) {
                       listAdapter.add(artist);
                   }
               }
               else
                   textView.setText("No Artists Found");
            }
            else if (type == Tracks.class) {
                listAdapter.clear();

               List<Track> tracks = ((Tracks) results).tracks;

               if(tracks.size()!=0) {
                   for (Track track : tracks) {
                       listAdapter.add(track);
                   }
               }
               else
                   textView.setText("No Tracks Found");
            }
            else if (type == TracksPager.class) {
                listAdapter.clear();

               List<Track> tracks = ((TracksPager) results).tracks.items;

               if(tracks.size()!=0) {
                   for (Track track : tracks) {
                       listAdapter.add(track);
                   }
               }
               else
                   textView.setText("No Tracks Found");
            }
            else if (type == AlbumsPager.class) {
                listAdapter.clear();

               List<AlbumSimple> albums = ((AlbumsPager) results).albums.items;

               if(albums.size()!=0) {
                   for (AlbumSimple album : albums)
                       listAdapter.add(album);
               }
               else
                   textView.setText("No Albums Found");
            }
            else if (type == AlbumSimple.class) {

                listAdapter.clear();

                List<TrackSimple> tracks = (((Album) results).tracks.items);

               if(tracks.size()!=0) {
                   String artistName, albumName, imgUrl;
                   for (TrackSimple track : (((Album) results).tracks.items)) {

                       Track newTrack = new Track();

                       newTrack.preview_url = track.preview_url;
                       newTrack.name = track.name;
                       newTrack.album = ((Album) results);
                       newTrack.artists = ((Album) results).artists;
                       newTrack.album.images = ((Album) results).images;

                       listAdapter.add(newTrack);


                   }
               }
               else
                   textView.setText("No Tracks Found");

            }
        }






}







   /*

        The following code is never used. It was the initial implementation.
        Now changed to use Spotify api and Glide framework.
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
                //listAdapter.add(arist);
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


    public class Artist  {
        //Class to store Artist Data
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

*/