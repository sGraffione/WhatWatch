package com.example.simone.whatwatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import database.FilmDescriptionDB;
import database.WatchListDB;


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
        //----seasonList usata in setEpisodeList----//
        TextView genres = (TextView) findViewById(R.id.genres);
        TextView creators = (TextView) findViewById(R.id.creators);
        TextView year = (TextView) findViewById(R.id.year);
        TextView youtube = (TextView) findViewById(R.id.youtube);
        CheckBox seen = (CheckBox) findViewById(R.id.seen);
        TextView runtime = (TextView) findViewById(R.id.runtime);



        int id_film = 0;
        filmInfo = new ArrayList<>();
        String type = "tv";


        Intent intent = getIntent();
        if(intent != null){
            id_film = getIntent().getIntExtra("id", 0);
            type = getIntent().getStringExtra("type");
            String url = "https://api.themoviedb.org/3/tv/"+ id_film + "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&append_to_response=credits,videos\n";
            try{
                filmInfo = new ParsingInfoFilm(this, type).execute(url).get();
            }catch (ExecutionException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        final HashMap<String, Object> data = filmInfo.get(0);
        Title.setText((String) data.get("name"));
        Picasso.with(this).load((String) data.get("poster_path")).into(poster);
        overview.setText((String) data.get("overview"));
        Double ratingValue = Double.parseDouble((String) data.get("vote_average"));
        if( ratingValue < 6){
            rating.setTextColor(Color.RED);
        }else if( ratingValue > 7.5){
            rating.setTextColor(Color.GREEN);
        }else{
            rating.setTextColor(ContextCompat.getColor(this, R.color.Orange));
        }
        rating.setText((String) data.get("vote_average"));

        ArrayList<HashMap<String, Object>> creator = parsingJSONArray((JSONArray) data.get("creator"));
        creators.setText((String) creator.get(0).get("name"));
        for(int i = 1; i < creator.size(); i++){
            creators.append(", " + (String) creator.get(i).get("name"));
        }

        ArrayList<HashMap<String, Object>> genre = parsingJSONArray((JSONArray) data.get("genres"));
        genres.setText((String) genre.get(0).get("name"));
        for(int i = 1; i < genre.size(); i++){
            genres.append(", " + (String) genre.get(i).get("name"));
        }

        year.setText((String) data.get("year"));
        runtime.setText(String.valueOf(data.get("runtime"))+"m");

        final String ytLink = "https://www.youtube.com/watch?v=" + parsingVideos((JSONArray) data.get("videos"));
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ytLink)));
            }
        });

        final int id = id_film;
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilmDescriptionDB film = new FilmDescriptionDB(id, (String) data.get("name"), "tv", (String) data.get("poster_path"));
                WatchListDB watchListDB = new WatchListDB(view.getContext());
                long row = watchListDB.insertFilm(film);
                if(row!=-1)
                    Toast.makeText(view.getContext(), "Film added to your Watchlist", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(view.getContext(), "It's already in your Watchlist!", Toast.LENGTH_SHORT).show();
                Fragment toRefresh = MainActivity.getToRefresh();
                android.support.v4.app.FragmentTransaction ft = MainActivity.getFragmentTransaction();
                if (toRefresh != null && ft != null) {
                    ft.detach(toRefresh);
                    ft.attach(toRefresh);
                    ft.commitAllowingStateLoss();
                }
            }
        });


        setEpisodeList((JSONArray) filmInfo.get(0).get("seasons"));
    }

    private void setEpisodeList(JSONArray seasons) {
        TextView seasonView = new TextView(this);
        seasonView.setLayoutParams(new TableLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        seasonView.setText("Season #" + String.valueOf(1));
        seasonView.setTextColor(getResources().getColor(R.color.WhiteSmoke));

        /*int nSeason=0;
        int nEpisode=0;
        TextView seasonView;
        TextView episodeView;
        for (int i=0;i< seasons.length();i++) {

            RelativeLayout season_list = (RelativeLayout) findViewById(R.id.tv_series_layout);

            LinearLayout lL = new LinearLayout(this);
            lL.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lL.setLayoutParams(params1);

                try{
                    JSONObject season = seasons.getJSONObject(i);
                    nSeason = season.getInt("season_number");
                    nEpisode = season.getInt("episode_count");
                }catch (JSONException e){
                    e.printStackTrace();
                }
            seasonView = new TextView(this);
            episodeView = new TextView(this);

            seasonView.setLayoutParams(new TableLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            episodeView.setLayoutParams(new TableLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            seasonView.setText("Season #" + String.valueOf(nSeason));
            seasonView.setTextColor(getResources().getColor(R.color.WhiteSmoke));

            episodeView.setText("# of episode: " + String.valueOf(nEpisode));
            episodeView.setTextColor(getResources().getColor(R.color.WhiteSmoke));

            RelativeLayout.LayoutParams relativeParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
            relativeParams.addRule(RelativeLayout.BELOW);

            season_list.addView(lL, relativeParams);
        }*/
        /*ArrayList<HashMap<String, Object>> seasonList = new ArrayList<>();
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
        lv.setBackgroundColor(ContextCompat.getColor(this, R.color.Really_Really_Dark_Gray));*/
    }

    private ArrayList<HashMap<String, Object>> parsingJSONArray(JSONArray input) {
        ArrayList<HashMap<String, Object>> names = new ArrayList<>();
        if (input != null) {
            for (int i = 0; i < input.length(); i++) {
                HashMap<String, Object> name = new HashMap<>();
                try {
                    JSONObject season = input.getJSONObject(i);
                    String nameValue = season.getString("name");

                    name.put("name", nameValue);

                    names.add(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return names;
        }
        return null;
    }

    private String parsingVideos(JSONArray input){
        String video = null;
        if(input != null){
            for(int i = 0; i < input.length(); i++){
                try {
                    JSONObject element = input.getJSONObject(i);
                    if (element.getString("type").equals("Trailer")) {
                        video = element.getString("key");
                        return video;
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}