package com.example.simone.whatwatch;

import database.Film;
import database.Tv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WatchedAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Object> films;

    // 1
    public WatchedAdapter(Context context, ArrayList<Object> films) {
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
        TextView nameTextView = (TextView)convertView.findViewById(R.id.title);

        Object object = films.get(position);
        if(object instanceof Film){
            Film film = (Film) films.get(position);
            if(film != null){
                //nameTextView.setText(film.getName());
            }
            Picasso.with(mContext).load(film.getImgUrl()).into(imageView);
        }else{
            Tv tv = (Tv) films.get(position);
            if(tv != null){
               // nameTextView.setText(tv.getName());
            }
            Picasso.with(mContext).load(tv.getImgUrlSeries()).into(imageView);
        }

        return convertView;
    }
}
