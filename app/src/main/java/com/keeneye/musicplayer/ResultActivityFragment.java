package com.keeneye.musicplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by bazilm on 11-06-2015.
 */
public class ResultActivityFragment extends Fragment {

    private final String TAG = ResultActivityFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();

        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT))
        {
            Log.d(TAG,"Result Activity created");
            String id = intent.getStringExtra(Intent.EXTRA_TEXT);
            Toast.makeText(getActivity(),id,Toast.LENGTH_LONG).show();
            final ListAdapter listAdapter = new ListAdapter<Track>(getActivity(),new ArrayList<Track>(),Track.class);
            ListView listView = (ListView)getView().findViewById(R.id.search_container);
            listView.setAdapter(listAdapter);

            new GetResult<Tracks>(listAdapter,Tracks.class).execute(id);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Track track = (Track)listAdapter.getItem(position);
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    String url = track.preview_url;
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try{
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepare();
                    }catch(IOException e)
                    {
                        Log.e(TAG,"Error"+e);
                    }

                    mediaPlayer.start();
                }
            });

        }


    }

    public ResultActivityFragment() {
    }
}
