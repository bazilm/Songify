package com.keeneye.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A placeholder fragment containing a simple view.
 */
public class MediaActivityFragment extends Fragment {

    private final String TAG = MediaActivityFragment.class.getSimpleName();

    public MediaActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();

        if(intent!=null)
        {
            Log.d(TAG,"Media Activity Created");
            Bundle bundle=intent.getExtras();
            String artist_name = bundle.getString("artist_name");
            String album_name=bundle.getString("album_name");
            String img_url = bundle.getString("img_url");


            TextView artist = (TextView)getView().findViewById(R.id.artist_name_text_view);
            TextView album = (TextView)getView().findViewById(R.id.album_name_text_view);
            ImageView image = (ImageView)getView().findViewById(R.id.track_artwork_view);

            artist.setText(artist_name);
            album.setText(album_name);
            Glide.with(getActivity()).load(img_url).into(image);


        }
    }


}
