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

public class WatchedAdapter extends BaseAdapter {

    private final Context mContext;
    private final FilmDescriptionDB[] films;

    // 1
    public WatchedAdapter(Context context, FilmDescriptionDB[] films) {
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
        ArrayList<FilmDescriptionDB> films = new ArrayList<>();

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.layoutfilm, null);
        }

        // 3
        ImageView imageView = (ImageView)convertView.findViewById(R.id.preview);
        TextView nameTextView = (TextView)convertView.findViewById(R.id.title);


        // 4
        //imageView.setImageResource(film.getImageResource());
        // nameTextView.setText(mContext.getString(book.getName()));
        //authorTextView.setText(mContext.getString(book.getAuthor()));

        return convertView;
    }
}
