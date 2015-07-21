package com.keeneye.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;


/**
 * Fragment of Search Activity(Main Activity).
 */
public class SearchActivityFragment extends Fragment {

    private String TAG = SearchActivity.class.getSimpleName();

    public static ListAdapter listAdapter;
    public static ArrayList tempValues;

    public ListView listView;
    public Spinner spinner;
    public TextView statusTextView;

    public static String spinnerItem;

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



        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView)getView().findViewById(R.id.list_view);
        spinner = (Spinner)getView().findViewById(R.id.spinner);
        statusTextView = (TextView)getView().findViewById(R.id.status_textView);

        spinnerItem = spinner.getSelectedItem().toString();

        final EditText search = (EditText)getView().findViewById(R.id.search);
        search.setHint("Search "+ spinnerItem);

        //Listener for EditText
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    //Checking internet status.
                    if (!isNetworkAvailable()) {
                        if(listAdapter!=null)
                            listAdapter.clear();

                        statusTextView.setText("Please Check your Internet Connection");
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_LONG).show();

                        switch (spinnerItem) {
                            case "Artist": {
                                listAdapter = new ListAdapter<Artist>(getActivity(), new ArrayList<Artist>(), Artist.class);
                                listView.setAdapter(listAdapter);
                                new GetResult<ArtistsPager>(listAdapter, statusTextView, ArtistsPager.class).execute(v.getText().toString());
                                break;

                            }

                            case "Track": {
                                listAdapter = new ListAdapter<Track>(getActivity(), new ArrayList<Track>(), Track.class);
                                listView.setAdapter(listAdapter);
                                new GetResult<TracksPager>(listAdapter, statusTextView, TracksPager.class).execute(v.getText().toString());
                                break;
                            }

                            case "Album":
                                listAdapter = new ListAdapter<AlbumSimple>(getActivity(), new ArrayList<AlbumSimple>(), AlbumSimple.class);
                                listView.setAdapter(listAdapter);
                                new GetResult<AlbumsPager>(listAdapter, statusTextView, AlbumsPager.class).execute(v.getText().toString());

                                break;
                        }


                    }
                }

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Filling up listView based on its type.
                if(listAdapter.getType()==Artist.class) {

                    Artist artist = (Artist) listAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), ResultActivity.class).putExtra(Intent.EXTRA_TEXT, artist.id)
                    .putExtra("Type","Artist");
                    startActivity(intent);
                }

                else if(listAdapter.getType()==Track.class){
                    Track track = (Track)listAdapter.getItem(position);
                    Bundle bundle = new Bundle();

                    bundle.putString("artist_name",track.artists.get(0).name);
                    bundle.putString("album_name",track.album.name);
                    if(track.album.images.get(0)!=null)
                        bundle.putString("img_url",track.album.images.get(0).url);
                    bundle.putString("preview_url",track.preview_url);
                    Intent intent = new Intent(getActivity(),MediaActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

                else if(listAdapter.getType()==AlbumSimple.class)
                {

                    AlbumSimple album = (AlbumSimple) listAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), ResultActivity.class).putExtra(Intent.EXTRA_TEXT, album.id)
                            .putExtra("Type","Album");
                    startActivity(intent);

                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerItem = parent.getItemAtPosition(position).toString();
                search.setHint("Search " + spinnerItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        if(tempValues!=null)
        {
            if(tempValues.get(0).getClass()==Artist.class)
                listAdapter = new ListAdapter<Artist>(getActivity(),tempValues,Artist.class);

            else if(tempValues.get(0).getClass()==Track.class)
                listAdapter = new ListAdapter<Track>(getActivity(),tempValues,Track.class);

            else if(tempValues.get(0).getClass()== AlbumSimple.class)
                listAdapter=new ListAdapter<AlbumSimple>(getActivity(),tempValues,AlbumSimple.class);

            listView.setAdapter(listAdapter);

        }





    }


    @Override
    public void onPause() {
        super.onPause();
        if(listAdapter!=null)
        tempValues = listAdapter.getValues();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
