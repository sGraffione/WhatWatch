package com.example.simone.whatwatch;

import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import database.Film;
import database.Database;


public class ShowInfoAboutListElement extends Activity {

    ArrayList<HashMap<String, Object>> filmInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_about_list_element);

        TextView Title = (TextView) findViewById(R.id.Title);
        ImageView poster = (ImageView) findViewById(R.id.poster);
        Button add_button = (Button) findViewById(R.id.add_button);
        CheckBox seen = (CheckBox) findViewById(R.id.seen);
        Button ytBtn = (Button) findViewById(R.id.youtube);
        TextView overview = (TextView) findViewById(R.id.Overview);
        TextView rating = (TextView) findViewById(R.id.rating);
        TextView year = (TextView) findViewById(R.id.year);
        TextView runtime =(TextView) findViewById(R.id.runtime);
        TextView director = (TextView) findViewById(R.id.director);
        TextView cast = (TextView) findViewById(R.id.cast);
        TextView genres = (TextView) findViewById(R.id.genres);


        String type = "movie";
        int id_film = 0;
        filmInfo = new ArrayList<>();

        Intent intent = getIntent();
        if(intent != null){
            id_film = getIntent().getIntExtra("id", 0);
            type = getIntent().getStringExtra("type");
            String url = "https://api.themoviedb.org/3/" + type + "/"+ id_film + "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&append_to_response=credits,videos\n";
            try{
                filmInfo = new ParsingInfoFilm(this, type, 1).execute(url).get();
            }catch (ExecutionException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        final HashMap<String, Object> data = filmInfo.get(0);

        Title.setText((String) data.get("original_title"));
        if(data.get("poster_path") == null){
            Picasso.with(this).load(R.drawable.noavailable).into(poster);
        }else{
            Picasso.with(this).load((String) data.get("poster_path")).into(poster);
        }

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
        year.setText((String) data.get("year"));
        if (data.get("runtime").equals("Not available")) {
            runtime.setText("Not Found");
        }else{
            runtime.setText(data.get("runtime") + ("m"));
        }

        String element = null;
        director.setText((String) data.get("director"));

            ArrayList<HashMap<String, Object>> genre = parsingJSONArray((JSONArray) data.get("genres"));
            genres.setText((String) genre.get(0).get("name"));
            for(int i = 1; i < genre.size(); i++){
                genres.append(", " + genre.get(i).get("name"));
            }

        ArrayList<HashMap<String, Object>> peopleOfIbiza = parsingJSONArray((JSONArray) data.get("cast"));
            cast.setText((String) peopleOfIbiza.get(0).get("name"));
        if(peopleOfIbiza.size() != 1) {
            if(peopleOfIbiza.size() < 5){
                for (int i = 1; i < peopleOfIbiza.size(); i++) {
                    cast.append(", " + peopleOfIbiza.get(i).get("name"));
                }
            }else{
                for (int i = 1; i < 5; i++) {
                    cast.append(", " + peopleOfIbiza.get(i).get("name"));
                }
            }
        }

        Film film = new Database(this).getFilmById(id_film);
        int isSeen;
        if(film != null){
            isSeen = film.getWatched();
        }else{
            isSeen = 0;
        }
        if(isSeen == 1){
            seen.setChecked(true);
        }

        String url = parsingVideos((JSONArray) data.get("videos"));
        if(url.equals("Video not available")){
            ytBtn.setVisibility(View.INVISIBLE);
        }else{
            final String ytLink = "https://www.youtube.com/watch?v=" + parsingVideos((JSONArray) data.get("videos"));
            ytBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ytLink)));
                }
            });
        }

        final int id = id_film;
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Film film = new Film(id, (String) data.get("original_title"), 0, (String) data.get("poster_path"));
                Database watchListDB = new Database(view.getContext());
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
