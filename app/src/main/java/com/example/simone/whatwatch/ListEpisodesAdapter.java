package com.example.simone.whatwatch;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import database.FilmDescriptionDB;
import database.WatchListDB;

public class ListEpisodesAdapter extends ArrayAdapter<HashMap<String, Object>>{
    ArrayList<HashMap<String, Object>> seasons;
    Context context;
    int resource;

    public ListEpisodesAdapter(Context context, int resource, ArrayList<HashMap<String, Object>> seasons){
        super(context, resource, seasons);
        this.context = context;
        this.resource = resource;
        this.seasons = seasons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.season_element, null, true);

        }

        HashMap<String, Object> data = seasons.get(position);

        TextView nSeason = (TextView)convertView.findViewById(R.id.season_number);
        TextView nEpisode = (TextView)convertView.findViewById(R.id.episode_number);

        nSeason.setText("Season number "+ data.get("season_number"));
        nEpisode.setText(data.get("episode_count")+" episodes");

        return convertView;
    }
}
