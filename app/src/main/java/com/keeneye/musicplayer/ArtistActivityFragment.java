package com.keeneye.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
                    Toast.makeText(getActivity(),"Searching",Toast.LENGTH_LONG).show();

                }

            return true;
            }
        });
    }


}
