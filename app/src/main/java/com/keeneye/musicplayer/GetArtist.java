
package com.keeneye.musicplayer;

import android.os.AsyncTask;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * Created by bazilm on 10-06-2015.
  Gets artist details from spotify using spotify api
  and adds it to the list adapter

  */

public class GetArtist extends AsyncTask<String,Void,ArtistsPager>

{

    private final String TAG = GetArtist.class.getSimpleName();
    /*
    Get the json string from Spotify and parse it.
     */
    ListAdapter listAdapter=null;

    public GetArtist(ListAdapter listAdapter)
    {
        this.listAdapter = listAdapter;
    }

    @Override
    protected ArtistsPager doInBackground(String... params) {

        SpotifyApi spotifyApi = new SpotifyApi();
        SpotifyService spotifyService = spotifyApi.getService();

        ArtistsPager artists = spotifyService.searchArtists(params[0]);
        return artists;
    }

    @Override
    protected void onPostExecute(ArtistsPager artistsPager) {


        for (Artist artist:artistsPager.artists.items)
        {
            listAdapter.add(artist);
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