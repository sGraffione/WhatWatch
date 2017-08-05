package com.example.simone.whatwatch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import database.FilmDescriptionDB;
import database.WatchListDB;


public class WatchedListFragment extends Fragment {

    private static final String TAG = "Watched";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_watched, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watched_list, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridview);

        gridView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Really_Really_Dark_Gray));

        WatchListDB watchListDB = new WatchListDB(getContext());
        final ArrayList<FilmDescriptionDB> films = watchListDB.getFilms(1);

        WatchedAdapter watchedAdapter = new WatchedAdapter(getActivity(), films);
        gridView.setAdapter(watchedAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                int id_film = films.get(i).getId();
                String type = films.get(i).getType();
                if(type.equals("movie")) {
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", type);
                    startActivity(appInfo);
                } else{
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutTvElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", type);
                    startActivity(appInfo);
                }


            }
        });
        setHasOptionsMenu(true);
        return view;
    }

}
