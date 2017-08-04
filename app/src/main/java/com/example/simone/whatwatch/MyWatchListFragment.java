package com.example.simone.whatwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private WatchlistAdapter watchlistAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_watch_list, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);

        WatchListDB watchListDB = new WatchListDB(getContext());
        final ArrayList<FilmDescriptionDB> films = watchListDB.getFilms(0);
        if(films != null){
            watchlistAdapter = new WatchlistAdapter(getContext(), films);
            gridView.setAdapter(watchlistAdapter);
        }


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You selected a film number" + position);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id_film = films.get(i).getId();
                Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                appInfo.putExtra("id", id_film);
                startActivity(appInfo);
            }
        });

        return view;
    }
}
