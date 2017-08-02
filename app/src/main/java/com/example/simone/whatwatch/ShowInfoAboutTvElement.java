package com.example.simone.whatwatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class ShowInfoAboutTvElement extends Activity {

    ArrayList<HashMap<String, Object>> filmInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_about_tv_element);

        final TextView Title = (TextView) findViewById(R.id.Title);
        final ImageView poster = (ImageView) findViewById(R.id.poster);
        final Button add_button = (Button) findViewById(R.id.add_button);
        final TextView overview = (TextView) findViewById(R.id.Overview);
        final TextView rating = (TextView) findViewById(R.id.rating);

        String type = "movie";
        int id_film;
        filmInfo = new ArrayList<>();

        Intent intent = getIntent();
        if(intent != null){
            id_film = getIntent().getIntExtra("id", 0);
            type = getIntent().getStringExtra("type");
            String url = "https://api.themoviedb.org/3/" + type + "/"+ id_film + "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US\n";
            try{
                filmInfo = new ParsingInfoFilm(this, type).execute(url).get();
            }catch (ExecutionException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        Title.setText((String) filmInfo.get(0).get("name"));
        Picasso.with(this).load((String) filmInfo.get(0).get("poster_path")).into(poster);
        overview.setText((String) filmInfo.get(0).get("overview"));
        Double ratingValue = Double.parseDouble((String) filmInfo.get(0).get("vote_average"));
        if( ratingValue < 6){
            rating.setTextColor(Color.RED);
        }else if( ratingValue > 7.5){
            rating.setTextColor(Color.GREEN);
        }else{
            rating.setTextColor(ContextCompat.getColor(this, R.color.Orange));
        }
        rating.setText((String) filmInfo.get(0).get("vote_average"));

        setEpisodeList((JSONArray) filmInfo.get(0).get("seasons"));
    }

    private void setEpisodeList(JSONArray seasons) {
        ArrayList<HashMap<String, Object>> seasonList = new ArrayList<>();
        if (seasons != null) {
            for (int i=0;i<seasons.length();i++){
                HashMap<String, Object> seasonElement = new HashMap<>();
                try{
                    JSONObject season = seasons.getJSONObject(i);
                    int nSeason = season.getInt("season_number");
                    int nEpisode = season.getInt("episode_count");

                    seasonElement.put("season_number", nSeason);
                    seasonElement.put("episode_count", nEpisode);

                    seasonList.add(seasonElement);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        ListView lv = (ListView) findViewById(R.id.season_list);
        ListEpisodesAdapter listEpisodeAdapter = new ListEpisodesAdapter(this, R.layout.film_element, seasonList);
        lv.setAdapter(listEpisodeAdapter);
    }
}