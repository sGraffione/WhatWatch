package com.example.simone.whatwatch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class ShowInfoAboutListElement extends Activity {

    HashMap<String, Object> filmInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_about_list_element);

        filmInfo = new HashMap<>();
        int id = getIntent().getIntExtra("id", 0);
        String url = "https://api.themoviedb.org/3/movie/"+ id + "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US";
        TextView Title = (TextView) findViewById(R.id.Title);
        ImageView poster = (ImageView) findViewById(R.id.poster);
        Button add_button = (Button) findViewById(R.id.add_button);
        TextView overview = (TextView) findViewById(R.id.Overview);
        TextView rating = (TextView) findViewById(R.id.rating);

        new ParsingInfoFilm(filmInfo).execute();



    }
}
