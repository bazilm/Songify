package com.keeneye.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
            String id = intent.getStringExtra(Intent.EXTRA_TEXT);
            Toast.makeText(getActivity(),id,Toast.LENGTH_LONG).show();
        }


    }

    public ResultActivityFragment() {
    }
}
