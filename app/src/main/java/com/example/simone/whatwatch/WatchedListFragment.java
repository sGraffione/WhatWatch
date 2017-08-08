package com.example.simone.whatwatch;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import database.Film;
import database.Tv;
import database.Database;


public class WatchedListFragment extends Fragment {

    private static final String TAG = "Watched";

    private GridView gridView;

    private WatchlistAdapter watchlistAdapter = null;

    String typeSelected = "All";
    String sortingType = "Alphabetical";
    int typeSelectedId = R.id.All;

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

        Database database = new Database(getContext());
        final ArrayList<Object> films = database.getFilter(1, typeSelected, sortingType);

        WatchedAdapter watchedAdapter = new WatchedAdapter(getActivity(), films);
        gridView.setAdapter(watchedAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(films.get(i) instanceof Film){
                    int id_film = ((Film) films.get(i)).getId();
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", "movie");
                    startActivity(appInfo);
                }else{
                    int id_tv = ((Tv) films.get(i)).getIdSeries();
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutTvElement.class);
                    appInfo.putExtra("id", id_tv);
                    appInfo.putExtra("type", "tv");
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
        refresh();
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        Database database = new Database(getContext());
        final ArrayList<Object> films = database.getFilter(0, typeSelected, sortingType);
        if(films != null){
            watchlistAdapter = new WatchlistAdapter(getContext(), films);
            gridView.setAdapter(watchlistAdapter);
        }

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Database database = new Database(view.getContext());
                if(films.get(position) instanceof Film){
                    database.updateWatched(((Film) films.get(position)).getId());
                }else{
                    database.updateWatched(((Tv) films.get(position)).getIdSeries(), ((Tv) films.get(position)).getIdSeason());
                }

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

                if(films.get(i) instanceof Film){
                    int id_film = ((Film) films.get(i)).getId();
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", "movie");
                    startActivity(appInfo);
                }else{
                    int id_tv = ((Tv) films.get(i)).getIdSeries();
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutTvElement.class);
                    appInfo.putExtra("id", id_tv);
                    appInfo.putExtra("type", "tv");
                    startActivity(appInfo);
                }
            }
        });
    }

}
