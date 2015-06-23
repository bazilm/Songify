package com.keeneye.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by bazilm on 11-06-2015.
 */
public class ResultActivityFragment extends Fragment {

    private final String TAG = ResultActivityFragment.class.getSimpleName();
    public static ListAdapter listAdapter;
    public static ArrayList<Track> tempValues;
    public ListView listView;
    public static int scrollPos;

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
            String type = intent.getStringExtra("Type");

            switch(type) {
                case "Artist": {
                    String id = intent.getStringExtra(Intent.EXTRA_TEXT);
                    Toast.makeText(getActivity(), id, Toast.LENGTH_LONG).show();

                    listView = (ListView) getView().findViewById(R.id.search_container);

                    listAdapter = new ListAdapter<Track>(getActivity(), new ArrayList<Track>(), Track.class);
                    new GetResult<Tracks>(listAdapter, Tracks.class).execute(id);
                    listView.setAdapter(listAdapter);
                    break;
                }

                case "Album": {
                    String id = intent.getStringExtra(Intent.EXTRA_TEXT);
                    Toast.makeText(getActivity(), id, Toast.LENGTH_LONG).show();

                    listView = (ListView) getView().findViewById(R.id.search_container);
                    listAdapter = new ListAdapter<Track>(getActivity(), new ArrayList<Track>(), Track.class);
                    new GetResult<AlbumSimple>(listAdapter, AlbumSimple.class).execute(id);
                    listView.setAdapter(listAdapter);
                    break;


                }
            }


        }

        else
        {
            listView = (ListView)getView().findViewById(R.id.search_container);
            listAdapter = new ListAdapter<Track>(getActivity(), tempValues, Track.class);
            listView.setAdapter(listAdapter);
            listView.scrollTo(0, scrollPos);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listAdapter.getType() == Track.class) {
                    Track track = (Track) listAdapter.getItem(position);
                    Bundle bundle = new Bundle();

                    bundle.putString("artist_name", track.artists.get(0).name);
                    bundle.putString("album_name", track.album.name);
                    if (track.album.images.get(0) != null)
                        bundle.putString("img_url", track.album.images.get(0).url);
                    bundle.putString("preview_url", track.preview_url);
                    Intent intent = new Intent(getActivity(), MediaActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }



            }
        });


    }

    @Override
    public void onPause()
    {
        super.onPause();
        tempValues= listAdapter.getValues();
        scrollPos=listView.getScrollY();
    }

    public ResultActivityFragment() {
    }
}

