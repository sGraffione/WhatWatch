package com.example.simone.whatwatch.FragmentClasses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

import com.example.simone.whatwatch.Adapter.CustomListAdapter;
import com.example.simone.whatwatch.Classes.App;
import com.example.simone.whatwatch.JSONParsingClasses.downloadJSONInfo;
import com.example.simone.whatwatch.MainActivity;
import com.example.simone.whatwatch.R;
import com.example.simone.whatwatch.Classes.ShowInfoAboutListElement;
import com.example.simone.whatwatch.Classes.ShowInfoAboutTvElement;

import java.util.ArrayList;
import java.util.HashMap;


public class Homepage extends Fragment {

    private static final String TAG = "Homepage";

    ArrayList<HashMap<String, Object>> filmInfo;
    ListView lv;
    String URLSelected = "https://api.themoviedb.org/3/discover/movie?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=true&page=1\n";
    CustomListAdapter adapter;
    Button forward;
    Button back;
    TextView forwardNumber;
    TextView backNumber;

    View view = null;

    int page = 1;
    int pagMax;
    int pagMaxTv;
    String typeSelected = "movie";
    String searchTypeSelected = "popularity";
    String firstPart = "https://api.themoviedb.org/3/discover/";
    String secondPart = "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=true&page=";
    int homepageSearchSelected = R.id.Popularity;
    int getHomepageSearchTypeSelected = R.id.Movies;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Log.d("HOMEPAGE", "Entrato nell'onCreate");

        filmInfo = MainActivity.getFilmInfo();
        pagMax = (int) filmInfo.get(0).get("total_pages");
        pagMaxTv = (int) filmInfo.get(0).get("total_pages_tv");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("HOMEPAGE", "Entrato in onCreateView");

        view = inflater.inflate(R.layout.fragment_homepage, container, false);

        lv = (ListView) view.findViewById(R.id.filmList);

        View footerView =  ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.navigation_button, null, false);
        lv.addFooterView(footerView);

        forward = (Button) footerView.findViewById(R.id.next);
        back = (Button) footerView.findViewById(R.id.back);
        forwardNumber = (TextView) footerView.findViewById(R.id.nextNumber);
        backNumber = (TextView) footerView.findViewById(R.id.backNumber);

        adapter = new CustomListAdapter(getActivity(), getContext(), R.layout.film_element, filmInfo, typeSelected);
        lv.setAdapter(adapter);



        if(getTypeSelected().equals("movie")){
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    HashMap<String, Object> hm_film = filmInfo.get(position);
                    int id_film = Integer.parseInt(hm_film.get("id").toString());
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", getTypeSelected());
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
                    appInfo.putExtra("type", getTypeSelected());
                    startActivity(appInfo);
                }
            });
        }

        if(page == 1){
            back.setVisibility(view.INVISIBLE);
            backNumber.setVisibility(view.INVISIBLE);
        }else{
            back.setVisibility(view.VISIBLE);
            backNumber.setVisibility(view.VISIBLE);
        }
        if(typeSelected.equals("movie")){
            if(page == pagMax){
                forward.setVisibility(view.INVISIBLE);
                forwardNumber.setVisibility(view.INVISIBLE);
            }else{
                forward.setVisibility(view.VISIBLE);
                forwardNumber.setVisibility(view.VISIBLE);
            }
        }else{
            if(page == pagMaxTv){
                forward.setVisibility(view.INVISIBLE);
                forwardNumber.setVisibility(view.INVISIBLE);
            }else{
                forward.setVisibility(view.VISIBLE);
                forwardNumber.setVisibility(view.VISIBLE);
            }
        }

        backNumber.setText("#" + String.valueOf(page-1) + " ");
        forwardNumber.setText(" #" + String.valueOf(page+1));



        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page += 1;
                String url = firstPart + typeSelected + secondPart + page + "\n";
                refresh(url);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page -= 1;
                String url = firstPart + typeSelected + secondPart + page + "\n";
                refresh(url);
            }
        });

        lv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Really_Really_Dark_Gray));

        setHasOptionsMenu(true);
        return view;
    }

    public void refresh(String url) {
        filmInfo.clear();
        new downloadJSONInfo(getActivity(), getActivity(), filmInfo, lv, getTypeSelected(), getTypeSelected()).execute(url);
        URLSelected = url;
        if (getTypeSelected().equals("movie")) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    HashMap<String, Object> hm_film = filmInfo.get(position);
                    int id_film = Integer.parseInt(hm_film.get("id").toString());
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", getTypeSelected());
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
                    appInfo.putExtra("type", getTypeSelected());
                    startActivity(appInfo);
                }
            });
        }


        if(page == 1){
            back.setVisibility(view.INVISIBLE);
            backNumber.setVisibility(view.INVISIBLE);
        }else{
            back.setVisibility(view.VISIBLE);
            backNumber.setVisibility(view.VISIBLE);
        }
        if(typeSelected.equals("movie")){
            if(page == pagMax){
                forward.setVisibility(view.INVISIBLE);
                forwardNumber.setVisibility(view.INVISIBLE);
            }else{
                forward.setVisibility(view.VISIBLE);
                forwardNumber.setVisibility(view.VISIBLE);
            }
        }else{
            if(page == pagMaxTv){
                forward.setVisibility(view.INVISIBLE);
                forwardNumber.setVisibility(view.INVISIBLE);
            }else{
                forward.setVisibility(view.VISIBLE);
                forwardNumber.setVisibility(view.VISIBLE);
            }
        }

        backNumber.setText("#" + String.valueOf(page-1) + " ");
        forwardNumber.setText(" #" + String.valueOf(page+1));

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        switch (homepageSearchSelected){
            case R.id.Popularity:
                menu.findItem(R.id.Popularity).setChecked(true);
                page = 1;
                break;
            case R.id.Most_Voted:
                menu.findItem(R.id.Most_Voted).setChecked(true);
                page = 1;
                break;
        }

        switch (getHomepageSearchTypeSelected){
            case R.id.Movies:
                menu.findItem(R.id.Movies).setChecked(true);
                page = 1;
                break;
            case R.id.Tv_shows:
                menu.findItem(R.id.Tv_shows).setChecked(true);
                page = 1;
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String URLSelected;
        int id = item.getGroupId();
        if(id == R.id.typeGroup){
            switch(item.getItemId()){
                case R.id.Movies:
                    getHomepageSearchTypeSelected = R.id.Movies;
                    typeSelected = "movie";
                    break;
                case R.id.Tv_shows:
                    getHomepageSearchTypeSelected = R.id.Tv_shows;
                    typeSelected = "tv";
                    break;
            }
            Log.d("HOMEPAGE", "Refresh ");
            URLSelected = firstPart + typeSelected + secondPart + page + "\n";
            refresh(URLSelected);
        }else{
            switch (item.getItemId()){
                case R.id.Popularity:
                    homepageSearchSelected = R.id.Popularity;
                    secondPart = "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=true&page=";
                    searchTypeSelected = "popularity";
                    break;
                case R.id.Most_Voted:
                    homepageSearchSelected = R.id.Most_Voted;
                    secondPart = "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=vote_count.desc&include_adult=false&include_video=false&page=";
                    searchTypeSelected = "vote";
                    break;
            }
            URLSelected = firstPart + typeSelected + secondPart + page + "\n";
            refresh(URLSelected);
        }

        return super.onOptionsItemSelected(item);
    }


    public String getTypeSelected(){
        return typeSelected;
    }
    public String getTypeSearch(){
        return searchTypeSelected;
    }
}
