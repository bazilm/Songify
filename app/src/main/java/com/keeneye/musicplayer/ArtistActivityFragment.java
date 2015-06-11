package com.keeneye.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistActivityFragment extends Fragment {

    private String Tag = "Artist Activity";
    public static ListAdapter listAdapter;
    public ArtistActivityFragment() {
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

        ListView listView = (ListView)getView().findViewById(R.id.list_view);
        listAdapter = new ListAdapter(getActivity(), new ArrayList<GetArtist.Artist>());

        listView.setAdapter(listAdapter);

                search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            Toast.makeText(getActivity(), v.getText().toString(), Toast.LENGTH_LONG).show();
                            GetArtist getArtist = new GetArtist((ArtistActivity)getActivity());
                            getArtist.execute(v.getText().toString());

                        }

                        return true;
                    }
                });


    }



}
