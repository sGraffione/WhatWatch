package com.example.simone.whatwatch;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends MainActivity {

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
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

        String urlSearch = "https://api.themoviedb.org/3/search/multi?api_key=22dee1f565e5788c58062fdeaf490afc&language=it-IT&query="+query+"&page=1&include_adult=false\n";
        ArrayList<HashMap<String, Object>> filmInfo = new ArrayList<>();
        new JSONSearch(filmInfo).execute(urlSearch);

        ListView lv = (ListView) findViewById(R.id.searchList);
        SearchAdapter adapter = new SearchAdapter(this, R.layout.film_element, filmInfo);
        lv.setAdapter(adapter);
    }
}
