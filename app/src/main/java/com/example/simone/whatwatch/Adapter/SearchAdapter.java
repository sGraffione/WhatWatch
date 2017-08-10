package com.example.simone.whatwatch.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simone.whatwatch.MainActivity;
import com.example.simone.whatwatch.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import database.Film;
import database.Tv;
import database.Database;

public class SearchAdapter extends ArrayAdapter<HashMap<String, Object>> {

    ArrayList<HashMap<String, Object>> filmList;
    Context context;
    int resource;
    String type;

    public SearchAdapter(Context context, int resource, ArrayList<HashMap<String, Object>> filmList) {
        super(context, resource, filmList);
        this.filmList = filmList;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.film_element, null, true);

        }
        final View view = convertView;
        final HashMap<String, Object> data = filmList.get(position);

        type = (String) data.get("type");

        imageView = (ImageView) convertView.findViewById(R.id.photo);
        Button button = (Button) convertView.findViewById(R.id.btnAddToWatch);

        if(data.get("poster_path").equals("null") || data.get("poster_path").equals("")) {
            Picasso.with(context).load(R.drawable.noavailable).into(imageView);
        }else{
            Picasso.with(context).load((String) data.get("poster_path")).into(imageView);
        }

        TextView title = (TextView) convertView.findViewById(R.id.Title);
        String titletmp = (String) data.get("title");
        title.setText(titletmp);

        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        Double ratingValue = (Double) data.get("vote_average");
        if( ratingValue < 6){
            rating.setTextColor(Color.RED);
        }else if( ratingValue > 7.5){
            rating.setTextColor(Color.GREEN);
        }else{
            rating.setTextColor(ContextCompat.getColor(getContext(), R.color.Orange));
        }
        rating.setText(new DecimalFormat("##.#").format(ratingValue));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Film film = null;
                Tv tv = null;
                Database database = new Database(getContext());
                if(data.get("type").equals("movie")) {
                    if(database.verifyId((int) data.get("id")) == 0) {
                        film = new Film((int) data.get("id"), (String) data.get("title"), 0, (String) data.get("poster_path"));
                        database.insertFilm(film);
                        Toast.makeText(view.getContext(), "Film added to your Watchlist", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(view.getContext(), "It's already in your Watchlist!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(database.verifyId((int) data.get("id"), (int) data.get("id_season")) == 0) {
                        String poster_path_season = data.get("poster_path_season").toString();
                        if(data.get("poster_path_season").toString().equals("null") || data.get("poster_path_season").toString().equals("")) {
                            poster_path_season = (String) data.get("poster_path");
                        }
                        tv = new Tv((int) data.get("id"),(int) data.get("id_season"), (String) data.get("title"), (int) data.get("episode_max_season"), (int) data.get("number_of_seasons"), 0, (String) data.get("poster_path"), poster_path_season);
                        database.insertSeries(tv);
                        Toast.makeText(view.getContext(), "Serie added to your Watchlist", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(view.getContext(), "It's already in your Watchlist!", Toast.LENGTH_SHORT).show();
                    }
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


        return convertView;
    }
}