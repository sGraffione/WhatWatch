package com.example.simone.whatwatch;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class Homepage extends Fragment {

    private static final String TAG = "Homepage";

    ArrayList<HashMap<String, Object>> filmInfo;
    ListView lv;
    String URLSelected = "https://api.themoviedb.org/3/discover/movie?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=true&page=1\n";
    Button btnAddElement;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        filmInfo = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.filmList);

        new downloadJSONInfo(getActivity(), filmInfo, lv, ((MainActivity)getActivity()).getTypeSelected(), ((MainActivity)getActivity()).getTypeSearch()).execute(URLSelected);


        if(((MainActivity)getActivity()).getTypeSelected().equals("movie")){
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    HashMap<String, Object> hm_film = filmInfo.get(position);
                    int id_film = Integer.parseInt(hm_film.get("id").toString());
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", ((MainActivity)getActivity()).getTypeSelected());
                    startActivity(appInfo);
                }
            });
        }else{
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    HashMap<String, Object> hm_film = filmInfo.get(position);
                    int id_tv = Integer.parseInt(hm_film.get("id").toString());
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutTvElement.class);
                    appInfo.putExtra("id", id_tv);
                    appInfo.putExtra("type", ((MainActivity)getActivity()).getTypeSelected());
                    startActivity(appInfo);
                }
            });
        }

        lv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Really_Really_Dark_Gray));

        return view;
    }

    public void refresh(String url) {
        filmInfo.clear();
        new downloadJSONInfo(getActivity(), filmInfo, lv, ((MainActivity) getActivity()).getTypeSelected(), ((MainActivity) getActivity()).getTypeSelected()).execute(url);
        URLSelected = url;
        if (((MainActivity) getActivity()).getTypeSelected().equals("movie")) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    HashMap<String, Object> hm_film = filmInfo.get(position);
                    int id_film = Integer.parseInt(hm_film.get("id").toString());
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", ((MainActivity) getActivity()).getTypeSelected());
                    startActivity(appInfo);
                }
            });
        } else {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    HashMap<String, Object> hm_film = filmInfo.get(position);
                    int id_tv = Integer.parseInt(hm_film.get("id").toString());
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutTvElement.class);
                    appInfo.putExtra("id", id_tv);
                    appInfo.putExtra("type", ((MainActivity) getActivity()).getTypeSelected());
                    startActivity(appInfo);
                }
            });
        }
    }
}
