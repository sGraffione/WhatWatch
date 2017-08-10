package com.example.simone.whatwatch;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends Activity {

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(String query){
        try{
            query = URLEncoder.encode(query, "utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        String urlSearch = "https://api.themoviedb.org/3/search/multi?api_key=22dee1f565e5788c58062fdeaf490afc&language=en_US&query="+query+"&page=1&include_adult=false\n";
        ArrayList<HashMap<String, Object>> filmInfo = new ArrayList<>();
        try {
            filmInfo = new JSONSearch().execute(urlSearch).get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        TextView noResult = (TextView) findViewById(R.id.noResults);
        ListView lv = (ListView) findViewById(R.id.searchList);
        SearchAdapter adapter = new SearchAdapter(this, R.layout.film_element, filmInfo);
        if(filmInfo == null || filmInfo.size() == 0){
            noResult.setVisibility(View.VISIBLE);
        }
        lv.setAdapter(adapter);
        lv.setBackgroundColor(getResources().getColor(R.color.Really_Really_Dark_Gray));
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.Really_Really_Dark_Gray));
        final ArrayList<HashMap<String, Object>> data = filmInfo;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                HashMap<String, Object> hm_film = data.get(position);
                String type = (String) hm_film.get("type");
                int id_film = Integer.parseInt(hm_film.get("id").toString());
                if(type.equals("movie")){
                    Intent appInfo = new Intent(view.getContext(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", type);
                    startActivity(appInfo);
                }else{
                    Intent appInfo = new Intent(view.getContext(), ShowInfoAboutTvElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", type);
                    startActivity(appInfo);
                }
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("ONPAUSE", "ON PAUSSSSSSSE");


    }
}
