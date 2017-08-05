package com.example.simone.whatwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.WeakHashMap;

import database.FilmDescriptionDB;
import database.WatchListDB;


public class MyWatchListFragment extends Fragment {
    private static final String TAG = "My Watchlist";

    private GridView gridView;

    private WatchlistAdapter watchlistAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_watch_list, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Really_Really_Dark_Gray));

        WatchListDB watchListDB = new WatchListDB(getContext());
        final ArrayList<FilmDescriptionDB> films = watchListDB.getFilms(0);
        if(films != null){
            watchlistAdapter = new WatchlistAdapter(getContext(), films);
            gridView.setAdapter(watchlistAdapter);
        }


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                WatchListDB watchedListDB = new WatchListDB(view.getContext());
                long row = watchedListDB.updateWatched(films.get(position).getId());

               Fragment toRefreshWatched = MainActivity.getToRefreshWatched();
                android.support.v4.app.FragmentTransaction ft = MainActivity.getFragmentTransaction();
                if (toRefreshWatched != null && ft != null) {
                    ft.detach(toRefreshWatched);
                    ft.attach(toRefreshWatched);
                    ft.commitAllowingStateLoss();
                }
                Fragment toRefresh = MainActivity.getToRefresh();
                android.support.v4.app.FragmentTransaction ft2 = MainActivity.getFragmentTransaction();
                if (toRefresh != null && ft2 != null) {
                    ft2.detach(toRefresh);
                    ft2.attach(toRefresh);
                    ft2.commitAllowingStateLoss();
                }
                return true;
            }
        });

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

        return view;
    }
}
