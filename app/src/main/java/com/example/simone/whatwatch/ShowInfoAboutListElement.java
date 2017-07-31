package com.example.simone.whatwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class ShowInfoAboutListElement extends Activity {

    ArrayList<HashMap<String, Object>> filmInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_about_list_element);

        filmInfo = new ArrayList<>();
        Intent intent = getIntent();
        if(intent != null){
            int id_film = getIntent().getIntExtra("id", 0);
            final String url = "https://api.themoviedb.org/3/movie/"+ id_film + "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US";
            final TextView Title = (TextView) findViewById(R.id.Title);
            final ImageView poster = (ImageView) findViewById(R.id.poster);
            final Button add_button = (Button) findViewById(R.id.add_button);
            final TextView overview = (TextView) findViewById(R.id.Overview);
            final TextView rating = (TextView) findViewById(R.id.rating);
            new ParsingInfoFilm(this, filmInfo).execute(url);
            Title.setText((String) filmInfo.get(0).get("original_title"));

        }
    }
}
