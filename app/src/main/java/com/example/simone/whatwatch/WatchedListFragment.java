package com.example.simone.whatwatch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import database.FilmDescriptionDB;


public class WatchedListFragment extends Fragment {

    private static final String TAG = "Watched";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watched_list, container, false);

        FilmDescriptionDB[] filmDescriptionDB = new FilmDescriptionDB[10];
        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        WatchlistAdapter watchlistAdapter = new WatchlistAdapter(getActivity(), filmDescriptionDB);
        gridView.setAdapter(watchlistAdapter);

        return view;
    }

}
