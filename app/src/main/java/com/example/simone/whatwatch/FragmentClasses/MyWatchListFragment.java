package com.example.simone.whatwatch.FragmentClasses;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.simone.whatwatch.Adapter.WatchlistAdapter;
import com.example.simone.whatwatch.JSONParsingClasses.ParsingInfoFilm;
import com.example.simone.whatwatch.JSONParsingClasses.RuntimeParsing;
import com.example.simone.whatwatch.MainActivity;
import com.example.simone.whatwatch.R;
import com.example.simone.whatwatch.Classes.ShowInfoAboutListElement;
import com.example.simone.whatwatch.Classes.ShowInfoAboutTvElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import database.Film;
import database.Tv;
import database. Database;


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
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_watch_list, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Really_Really_Dark_Gray));

        final Database database = new Database(getContext());
        final ArrayList<Object> films = database.getFilter(0, typeSelected, sortingType);
        if(films != null){
            watchlistAdapter = new WatchlistAdapter(getContext(), films);
            gridView.setAdapter(watchlistAdapter);
        }


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                final Database database = new Database(view.getContext());
                final int j = position;
                if(films.get(position) instanceof Film){

                    final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("Choose...");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "I've watched it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            database.updateWatched(((Film) films.get(j)).getId());
                            String url = "https://api.themoviedb.org/3/movie/"+ ((Film) films.get(j)).getId() +"?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US";
                            new RuntimeParsing(view.getContext(), "movie").execute(url);
                            refreshWatchedWatchlist();
                        }
                    });
                    alertDialog.show();

                }else{
                    final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("Choose...");

                    if(database.verifyEpisodeMax(((Tv) films.get(j)).getIdSeries(), ((Tv) films.get(j)).getIdSeason())){
                        if(database.verifySeasonMax(((Tv) films.get(j)).getIdSeries(), ((Tv) films.get(j)).getIdSeason())){
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "I finished this series", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int id_series = ((Tv) films.get(j)).getIdSeries();
                                    int id_season = ((Tv) films.get(j)).getIdSeason();
                                    database.updateWatched(id_series, id_season);
                                    refreshWatchedWatchlist();
                                }
                            });
                        }else{
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Go to the next season", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int id_series = ((Tv) films.get(j)).getIdSeries();
                                    int id_season = ((Tv) films.get(j)).getIdSeason();
                                    int next_season = ((Tv) films.get(j)).getSeasonCurrent() + 1;
                                    int max_season = ((Tv) films.get(j)).getEpisodeMax();
                                    int trueNextSeason = checkNextSeason(id_series, next_season - 1, max_season, database);
                                    if(trueNextSeason != -1) {
                                        database.updateWatched(id_series, id_season);
                                        String url = "https://api.themoviedb.org/3/tv/"+ id_series + "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&append_to_response=credits,videos\n";
                                        try{
                                            ArrayList<HashMap<String, Object>> newSeason = new ParsingInfoFilm(view.getContext(), "tv", trueNextSeason).execute(url).get();
                                            String poster_path_season;
                                            HashMap<String, Object> data = newSeason.get(0);
                                            if(data.get("poster_path_season").toString().equals("null") || data.get("poster_path_season").toString().equals("") || data.get("poster_path_season") == null){
                                                poster_path_season = data.get("poster_path").toString();
                                            }else{
                                                poster_path_season = data.get("poster_path_season").toString();
                                            }
                                            Tv tv = new Tv(id_series, (int) data.get("id_season"), (String) data.get("name"), 1, (int) data.get("episode_max_season"), next_season, (int) data.get("number_of_seasons"),
                                                    0, (String) data.get("poster_path"), poster_path_season);
                                            database.insertSeries(tv);
                                        }catch (InterruptedException e){
                                            e.printStackTrace();
                                        }catch (ExecutionException e){
                                            e.printStackTrace();
                                        }
                                    }else{
                                        diplayAlertSeasonMax(id_series, id_season);
                                    }

                                    refreshWatchedWatchlist();
                                }
                            });

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "I watched this season", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int var = database.updateWatched(((Tv) films.get(j)).getIdSeries(), ((Tv) films.get(j)).getIdSeason());
                                    Log.d("WATCHLIST", String.valueOf(var));
                                    refreshWatchedWatchlist();
                                }
                            });
                        }
                    }else{
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Go to the next episode", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int var = database.updateEpisode(((Tv) films.get(j)).getIdSeries(), ((Tv) films.get(j)).getIdSeason());
                                Log.d("WATCHLIST", String.valueOf(var));
                                String url = "https://api.themoviedb.org/3/tv/"+ ((Tv) films.get(j)).getIdSeries() +"?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US";
                                new RuntimeParsing(view.getContext(), "tv").execute(url);
                                refreshWatchedWatchlist();
                            }
                        });
                    }
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "I don't like it (delete now)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            database.deleteSeason(((Tv) films.get(j)).getIdSeries(), ((Tv) films.get(j)).getIdSeason());
                            refreshWatchedWatchlist();
                        }
                    });
                    alertDialog.show();
                }
                refreshWatchedWatchlist();
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
        setHasOptionsMenu(true);

        return view;
    }

    private void diplayAlertSeasonMax(final int id_series, final int id_season) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Warning! You've already watched all the following seasons of this series.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Database database = new Database(getContext());
                database.updateWatched(id_series, id_season);
                alertDialog.dismiss();
                refreshWatchedWatchlist();
            }
        });
        alertDialog.show();
    }

    public int checkNextSeason(int id_series, int current_season, int season_max, Database database){

        ArrayList<Integer> seasonsWatched = database.getSeasonsWatched(id_series);
        int nextSeason = current_season + 1;
        while(seasonsWatched.contains(nextSeason)){
            nextSeason++;
            if(nextSeason > season_max){
                return -1;
            }
        }
        return nextSeason;
    }



    public void refreshWatchedWatchlist(){
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
