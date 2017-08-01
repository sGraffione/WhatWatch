package com.example.simone.whatwatch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import database.FilmDescriptionDB;
import database.WatchListDB;

public class CustomListAdapter extends ArrayAdapter<HashMap<String, Object>> {

    ArrayList<HashMap<String, Object>> filmList;
    Context context;
    int resource;

    public CustomListAdapter(Context context, int resource, ArrayList<HashMap<String, Object>> filmList) {
        super(context, resource, filmList);
        this.filmList = filmList;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.film_element, null, true);

        }
        final View view = convertView;
        final HashMap<String, Object> data = filmList.get(position);

        imageView = (ImageView) convertView.findViewById(R.id.photo);
        Button button = (Button) convertView.findViewById(R.id.btnAddToWatch);

        Picasso.with(context).load((String) data.get("poster_path")).into(imageView);

        TextView title = (TextView) convertView.findViewById(R.id.Title);
        title.setText((String) data.get("original_title"));

        final TextView rating = (TextView) convertView.findViewById(R.id.rating);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilmDescriptionDB film = new FilmDescriptionDB((int) data.get("id"), (String) data.get("original_title"));
                WatchListDB watchListDB = new WatchListDB(getContext());
                long row = watchListDB.insertFilm(film);
                ((TextView) view.findViewById(R.id.rating)).setText(String.valueOf(row));
            }
        });


        return convertView;
    }
}