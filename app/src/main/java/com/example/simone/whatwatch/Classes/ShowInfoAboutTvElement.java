package com.example.simone.whatwatch.Classes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simone.whatwatch.ChatGroup.ChatGroup;
import com.example.simone.whatwatch.JSONParsingClasses.ParsingInfoFilm;
import com.example.simone.whatwatch.MainActivity;
import com.example.simone.whatwatch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import database.Film;
import database.Tv;
import database.Database;


public class ShowInfoAboutTvElement extends Activity {

    ArrayList<HashMap<String, Object>> filmInfo;
    int id_film;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_about_tv_element);

        final TextView Title = (TextView) findViewById(R.id.Title);
        final ImageView poster = (ImageView) findViewById(R.id.poster);
        final Button add_button = (Button) findViewById(R.id.add_button);
        final TextView overview = (TextView) findViewById(R.id.Overview);
        final TextView rating = (TextView) findViewById(R.id.rating);
        TextView genres = (TextView) findViewById(R.id.genres);
        TextView creators = (TextView) findViewById(R.id.creators);
        TextView year = (TextView) findViewById(R.id.year);
        TextView youtube = (TextView) findViewById(R.id.youtube);
        TextView runtime = (TextView) findViewById(R.id.runtime);
        Button check = (Button) findViewById(R.id.check);
        Button joinChat = (Button) findViewById(R.id.joinChat);


        id_film = 0;
        filmInfo = new ArrayList<>();
        String type = "tv";


        Intent intent = getIntent();
        if (intent != null) {
            id_film = getIntent().getIntExtra("id", 0);
            type = getIntent().getStringExtra("type");
            url = "https://api.themoviedb.org/3/tv/" + id_film + "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&append_to_response=credits,videos\n";
            try {
                filmInfo = new ParsingInfoFilm(this, type, 1).execute(url).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final HashMap<String, Object> data = filmInfo.get(0);
        Title.setText((String) data.get("name"));
        if (data.get("poster_path").equals("null")) {
            Picasso.with(this).load(R.drawable.noavailable).into(poster);
        } else {
            Picasso.with(this).load((String) data.get("poster_path")).into(poster);
        }

        overview.setText((String) data.get("overview"));
        Double ratingValue = Double.parseDouble((String) data.get("vote_average"));
        if (ratingValue < 6) {
            rating.setTextColor(Color.RED);
        } else if (ratingValue > 7.5) {
            rating.setTextColor(Color.GREEN);
        } else {
            rating.setTextColor(ContextCompat.getColor(this, R.color.Orange));
        }
        rating.setText((String) data.get("vote_average"));

        String element = null;

        ArrayList<HashMap<String, Object>> creator = parsingJSONArray((JSONArray) data.get("creator"));
        creators.setText((String) creator.get(0).get("name"));
        for (int i = 1; i < creator.size(); i++) {
            creators.append(", " + creator.get(i).get("name"));
        }


        ArrayList<HashMap<String, Object>> genre = parsingJSONArray((JSONArray) data.get("genres"));
        genres.setText((String) genre.get(0).get("name"));
        for (int i = 1; i < genre.size(); i++) {
            genres.append(", " + genre.get(i).get("name"));
        }

            year.setText((String) data.get("year"));

            if ((int) data.get("runtime") == 0) {
                runtime.setText("Not found");
            } else {
                runtime.setText(String.valueOf(data.get("runtime")) + "m");
            }

            String result = parsingVideos((JSONArray) data.get("videos"));
        if(result != null) {
            if(result.equals("Video not available")) {
                youtube.setVisibility(View.INVISIBLE);
            }else {
                final String ytLink = "https://www.youtube.com/watch?v=" + result;
                youtube.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ytLink)));
                    }
                });
            }
        }else{
            youtube.setVisibility(View.INVISIBLE);
        }

            final int id = id_film;
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String poster_path_season = null;
                    if(data.get("poster_path_season") == null || data.get("poster_path_season").equals("null") || data.get("poster_path_season").equals("")) {
                        poster_path_season = data.get("poster_path").toString();
                    }else{
                        poster_path_season = (String) data.get("poster_path_season");
                    }
                    Tv tv = null;
                    try{
                        tv = new Tv(id, (int) data.get("id_season"), (String) data.get("name"), (int) data.get("episode_max_season"), (int) data.get("number_of_seasons"),
                                0, (String) data.get("poster_path"), poster_path_season);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    Database database = new Database(view.getContext());

                    if (!database.verifySeasonWatched(id, (int) data.get("id_season"))) {
                        if(database.verifyId(id, (int) data.get("id_season")) == 0){
                            database.insertSeries(tv);
                            Toast.makeText(view.getContext(), "Season 1 added to your Watchlist", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(view.getContext(), "You have already a season in your watchlist", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(view.getContext(), "You have already watched this season", Toast.LENGTH_SHORT).show();
                    }
                    Fragment toRefresh = MainActivity.getToRefresh();
                    android.support.v4.app.FragmentTransaction ft = MainActivity.getFragmentTransaction();
                    if (toRefresh != null && ft != null) {
                        ft.detach(toRefresh);
                        ft.attach(toRefresh);
                        ft.commitAllowingStateLoss();
                    }
                }
            });

        Database database = new Database(this);

        Boolean isSeen;

        isSeen = database.verifySeriesWatched(id);

        if(isSeen)
            check.setVisibility(View.VISIBLE);

            setEpisodeList((JSONArray) filmInfo.get(0).get("seasons"));

        joinChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flagWatched = false;
                boolean watched = new Database(v.getContext()).getTvById(id);
                if (!watched) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                    alertDialog.setTitle("You need to watch at least one season of this series!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    final String uniqueIdDatabaseChatGroupWithoutMarcoR;      //Complimenti marco eh...non aiutarci..stronzo >:-(
                    //Database database = new Database(v.getContext());
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            flagWatched = true;
                        } else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                            alertDialog.setTitle("You need to be logged with your facebook account");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                    }

                    uniqueIdDatabaseChatGroupWithoutMarcoR = String.valueOf((id + "_tv"));

                    if (flagWatched) {
                        Intent intent = new Intent(v.getContext(), ChatGroup.class);
                        intent.putExtra("identifier", uniqueIdDatabaseChatGroupWithoutMarcoR);
                        intent.putExtra("title", (String) data.get("name"));
                        v.getContext().startActivity(intent);
                    }
                }
            }
        });



    }

    private void setEpisodeList(JSONArray seasons) {
        Database database = new Database(this);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.tv_series_layout);
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);

        int id_prec_tv1 = 0;
        int id_prec_tv2 = 0;
        int id_prec_btn = 0;

        int height = 60;
        int start = 0;
        try {
            JSONObject object = seasons.getJSONObject(0);
            if(object.getInt("season_number") == 0){
                start = 1;
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        int j = 0;
        for(int i = start; i < seasons.length(); i++){
            try {

                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                TextView tv1 = new TextView(this);
                params1.addRule(RelativeLayout.ALIGN_START, R.id.Overview);
                params1.setMargins(0,10 ,30,0);
                if(id_prec_tv1 != 0) {
                    params1.addRule(RelativeLayout.BELOW, id_prec_tv1);
                }else{
                    params1.addRule(RelativeLayout.BELOW, R.id.Overview);
                }
                tv1.setId(View.generateViewId());
                tv1.setText("Season #" + seasons.getJSONObject(i).getInt("season_number"));
                tv1.setTextColor(getResources().getColor(R.color.WhiteSmoke));
                tv1.setHeight(height);
                id_prec_tv1 = tv1.getId();

                TextView tv2 = new TextView(this);
                params2.addRule(RelativeLayout.RIGHT_OF, tv1.getId());
                params2.setMargins(0,10,20,0);
                if(id_prec_tv2 != 0) {
                    params2.addRule(RelativeLayout.BELOW, id_prec_tv2);
                    params2.addRule(RelativeLayout.ALIGN_START, id_prec_tv2);
                }else {
                    params2.addRule(RelativeLayout.BELOW, R.id.Overview);
                }
                tv2.setId(View.generateViewId());
                tv2.setText(seasons.getJSONObject(i).getString("episode_count") + " episodes");
                tv2.setTextColor(getResources().getColor(R.color.WhiteSmoke));
                tv2.setHeight(height);
                id_prec_tv2 = tv2.getId();

                final TextView btn_add = new TextView(this);
                params3.addRule(RelativeLayout.RIGHT_OF, tv2.getId());
                params3.addRule(RelativeLayout.ALIGN_BOTTOM, tv2.getId());
                params3.addRule(RelativeLayout.ALIGN_TOP, tv2.getId());
                params3.addRule(RelativeLayout.ALIGN_BASELINE, tv2.getId());
                if(id_prec_btn != 0){
                    params3.addRule(RelativeLayout.BELOW, id_prec_btn);
                    params3.addRule(RelativeLayout.ALIGN_START, id_prec_btn);
                }else{
                    params3.addRule(RelativeLayout.BELOW, R.id.Overview);
                }

                btn_add.setId(View.generateViewId());
                if(!database.verifySeasonWatched(id_film, seasons.getJSONObject(i).getInt("id"))){
                    if(database.verifyId(id_film, seasons.getJSONObject(i).getInt("id")) == 0) {
                        btn_add.setBackgroundResource(R.drawable.add);
                        btn_add.setHeight(height);
                        btn_add.setWidth(height);
                        btn_add.setGravity(Gravity.CENTER);
                    }else{
                        btn_add.setText("In watchlist");
                        btn_add.setTextColor(getResources().getColor(R.color.Orange));
                    }
                }else{
                    btn_add.setText("Watched");
                    btn_add.setTextColor(Color.GREEN);
                }
                id_prec_btn = btn_add.getId();


                if(start == 0){
                    j = i+1;
                }else{
                    j = i;
                }
                final int index = j;
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_season(index, v, btn_add.getId());
                    }
                });

                relativeLayout.addView(tv1, params1);
                relativeLayout.addView(tv2, params2);
                relativeLayout.addView(btn_add, params3);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void add_season(int i, View view, int id_btn){
        try{
            filmInfo = new ParsingInfoFilm(this, "tv", i).execute(url).get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        String poster_path_season;
        HashMap<String, Object> data = filmInfo.get(0);
        /*if(data.get("poster_path_season").toString().equals("null") || data.get("poster_path_season").toString().equals("") || data.get("poster_path_season") == null){
            poster_path_season = data.get("poster_path").toString();
        }else{
            poster_path_season = data.get("poster_path_season").toString();
        }*/
        Tv tv = new Tv(id_film, (int) data.get("id_season"), (String) data.get("name"), 1, (int) data.get("episode_max_season"), i, (int) data.get("number_of_seasons"),
                0, (String) data.get("poster_path"), (String) data.get("poster_path_season"));
        Database database = new Database(view.getContext());
        if (!database.verifySeasonWatched(id_film, (int) data.get("id_season"))) {
            if(!database.verifyIdSeries(id_film)){
                Toast.makeText(view.getContext(), "You added the season number " + i + " to your Watchlist", Toast.LENGTH_SHORT).show();
                database.insertSeries(tv);
                TextView btn_add = (TextView) findViewById(id_btn);
                btn_add.setBackground(null);
                btn_add.setWidth(80);
                btn_add.setText("In watchlist");
                btn_add.setTextColor(getResources().getColor(R.color.Orange));
            }else{
                Toast.makeText(view.getContext(), "You have already a season of this serie", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(view.getContext(), "You have already watched this season", Toast.LENGTH_SHORT).show();
        }
        Fragment toRefresh = MainActivity.getToRefresh();
        android.support.v4.app.FragmentTransaction ft = MainActivity.getFragmentTransaction();
        if (toRefresh != null && ft != null) {
            ft.detach(toRefresh);
            ft.attach(toRefresh);
            ft.commitAllowingStateLoss();
        }
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