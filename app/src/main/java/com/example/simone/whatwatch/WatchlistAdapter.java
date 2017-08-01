package com.example.simone.whatwatch;

import database.FilmDescriptionDB;
import database.WatchListDB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WatchlistAdapter extends BaseAdapter {

    private final Context mContext;
    private final FilmDescriptionDB[] films;

    // 1
    public WatchlistAdapter(Context context, FilmDescriptionDB[] films) {
        this.mContext = context;
        this.films = films;
    }

    // 2
    @Override
    public int getCount() {
        return films.length;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        ArrayList<FilmDescriptionDB> film = new ArrayList<>();

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.layoutfilm, null);
        }

        // 3


        WatchListDB watchListDB = new WatchListDB(mContext);




        final ImageView imageView = (ImageView)convertView.findViewById(R.id.preview);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.title);

        // 4
        //imageView.setImageResource(film.getImageResource());
       // nameTextView.setText(mContext.getString(book.getName()));
        //authorTextView.setText(mContext.getString(book.getAuthor()));

        return convertView;
    }
}
