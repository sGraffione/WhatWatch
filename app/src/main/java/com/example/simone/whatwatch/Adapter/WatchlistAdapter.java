package com.example.simone.whatwatch.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simone.whatwatch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import database.Film;
import database.Tv;

public class WatchlistAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Object> films;


    public WatchlistAdapter(Context context, ArrayList<Object> films) {
        this.mContext = context;
        this.films = films;
    }

    @Override
    public int getCount() {
        return films.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.layoutfilm, null);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.preview);
        TextView season = (TextView)convertView.findViewById(R.id.season);
        TextView episode = (TextView)convertView.findViewById(R.id.episode);

            Object object = films.get(position);
            if(object instanceof Film){
                Film film = (Film) films.get(position);
                Picasso.with(mContext).load(film.getImgUrl()).into(imageView);
            }else{
                Tv tv = (Tv) films.get(position);

                season.setText("S " + tv.getSeasonCurrent());
                episode.setText("E " + tv.getEpisodeCurrent());

                if(tv.getImgUrlSeason().equals("") || tv.getImgUrlSeason() == null || tv.getImgUrlSeason().isEmpty()){
                    Picasso.with(mContext).load(R.drawable.noavailable).into(imageView);
                }else{
                    Picasso.with(mContext).load(tv.getImgUrlSeason()).into(imageView);
                }
            }


        return convertView;
    }
}
