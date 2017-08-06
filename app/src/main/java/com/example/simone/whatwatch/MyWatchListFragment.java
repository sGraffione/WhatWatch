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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import database.FilmDescriptionDB;
import database.WatchListDB;


public class MyWatchListFragment extends Fragment {
    private static final String TAG = "My Watchlist";

    private GridView gridView;

    private WatchlistAdapter watchlistAdapter = null;

    String typeSelected = "All";
    String sortingType = "Alphabetical";
    int sortingTypeId = R.id.A_Z;
    int typeSelectedId = R.id.All;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_watchlist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_watch_list, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Really_Really_Dark_Gray));

        WatchListDB watchListDB = new WatchListDB(getContext());
        final ArrayList<FilmDescriptionDB> films = watchListDB.getFilms(0, typeSelected, sortingType);
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
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        switch (typeSelectedId){
            case R.id.All:
                menu.findItem(R.id.All).setChecked(true);
                break;
            case R.id.Movies:
                menu.findItem(R.id.Movies).setChecked(true);
                break;
            case R.id.Tv_shows:
                menu.findItem(R.id.Tv_shows).setChecked(true);
        }

        switch (sortingTypeId){
            case R.id.A_Z:
                menu.findItem(R.id.A_Z).setChecked(true);
                break;
            case R.id.Recent:
                menu.findItem(R.id.Recent).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String URLSelected;
        int id = item.getGroupId();
        if(id == R.id.typeGroup){
            switch(item.getItemId()){
                case R.id.All:
                    typeSelectedId = R.id.All;
                    typeSelected = "All";
                    break;
                case R.id.Movies:
                    typeSelectedId = R.id.Movies;
                    typeSelected = "Movie";
                    break;
                case R.id.Tv_shows:
                    typeSelectedId = R.id.Tv_shows;
                    typeSelected = "Tv_shows";
                    break;
            }
        }else{
            switch (item.getItemId()){
                case R.id.A_Z:
                    sortingTypeId = R.id.A_Z;
                    sortingType = "Alphabetical";
                    break;
                case R.id.Recent:
                    sortingTypeId = R.id.Recent;
                    sortingType = "Recent";
                    break;
            }
        }

        refresh();

        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        WatchListDB watchListDB = new WatchListDB(getContext());
        final ArrayList<FilmDescriptionDB> films = watchListDB.getFilms(0, typeSelected, sortingType);
        if(films != null){
            watchlistAdapter = new WatchlistAdapter(getContext(), films);
            gridView.setAdapter(watchlistAdapter);
        }
    }
}
