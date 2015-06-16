package com.keeneye.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends Fragment {

    private String TAG = "Artist Activity";
    public static ListAdapter listAdapter;
    public static ArrayList<Artist> tempValues;
    public ListView listView;
    public static int scrollPos;

    public SearchActivityFragment() {
    }

    public static ListAdapter getAdapter()
    {
        return listAdapter;
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

        listView = (ListView)getView().findViewById(R.id.list_view);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(getActivity(), v.getText().toString(), Toast.LENGTH_LONG).show();

                    new GetResult<ArtistsPager>(listAdapter,ArtistsPager.class).execute(v.getText().toString());

                }

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Artist artist = (Artist) listAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ResultActivity.class).putExtra(Intent.EXTRA_TEXT, artist.id);
                startActivity(intent);
            }
        });


    }



    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (tempValues!=null) {
            listAdapter = new ListAdapter<Artist>(getActivity(), tempValues, Artist.class);

        }
        else {
            listAdapter = new ListAdapter<Artist>(getActivity(), new ArrayList<Artist>(), Artist.class);

        }


        listView.setAdapter(listAdapter);
        listView.scrollTo(0,scrollPos);


    }


    @Override
    public void onPause() {
        super.onPause();
        tempValues = (ArrayList<Artist>)listAdapter.getValues();
        scrollPos = listView.getScrollY();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
