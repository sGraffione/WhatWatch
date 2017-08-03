package com.example.simone.whatwatch;


import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class SearchActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            /*
             * codice che utilizza il contenuto dell'oggetto
             * String query per effettuare la ricerca
             */

        }
    }
}
