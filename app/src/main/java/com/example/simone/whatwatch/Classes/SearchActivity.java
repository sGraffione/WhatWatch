package com.example.simone.whatwatch.Classes;


import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.simone.whatwatch.Adapter.SearchAdapter;
import com.example.simone.whatwatch.JSONParsingClasses.JSONSearch;
import com.example.simone.whatwatch.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends Activity {

    private ProgressDialog pDialog;
    String query;
    int page = 1;
    int pagMax = 1;

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
            query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(String query){
        try{
            query = URLEncoder.encode(query, "utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        final String urlSearch = "https://api.themoviedb.org/3/search/multi?api_key=22dee1f565e5788c58062fdeaf490afc&language=en_US&query="+query+"&page="+page+"&include_adult=false\n";
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

        View footerView =  ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.navigation_button, null, false);
        lv.addFooterView(footerView);

        Button forward = (Button) footerView.findViewById(R.id.next);
        Button back = (Button) footerView.findViewById(R.id.back);

        SearchAdapter adapter = new SearchAdapter(this, R.layout.film_element, filmInfo);
        if(filmInfo == null || filmInfo.size() == 0){
            noResult.setVisibility(View.VISIBLE);
        }
        if(adapter !=  null)
            lv.setAdapter(adapter);
        lv.setBackgroundColor(getResources().getColor(R.color.Really_Really_Dark_Gray));
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.Really_Really_Dark_Gray));



        if(page == 1){
            back.setVisibility(view.INVISIBLE);
        }else{
            back.setVisibility(view.VISIBLE);
        }
        if(page == pagMax){
            forward.setVisibility(view.INVISIBLE);
        }else{
            forward.setVisibility(view.VISIBLE);
        }

        back.setText("<< Page " + String.valueOf(page-1));
        forward.setText("Page " + String.valueOf(page+1) + " >>");

        final String tmpQuery = query;
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page += 1;
                String url = "https://api.themoviedb.org/3/search/multi?api_key=22dee1f565e5788c58062fdeaf490afc&language=en_US&query="+tmpQuery+"&page="+page+"&include_adult=false\n";
                doSearch(url);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page -= 1;
                String url = "https://api.themoviedb.org/3/search/multi?api_key=22dee1f565e5788c58062fdeaf490afc&language=en_US&query="+tmpQuery+"&page="+page+"&include_adult=false\n";
                doSearch(url);
            }
        });


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
