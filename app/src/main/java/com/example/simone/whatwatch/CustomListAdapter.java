package com.example.simone.whatwatch;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.MainThread;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;

import java.text.DecimalFormat;
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
    String TAG_TITLENAME;
    String type;

    public CustomListAdapter(Context context, int resource, ArrayList<HashMap<String, Object>> filmList, String TAG_TYPE) {
        super(context, resource, filmList);
        this.filmList = filmList;
        this.context = context;
        this.resource = resource;
        if(TAG_TYPE.equals("movie")){
            TAG_TITLENAME = "original_title";
        }else{
            TAG_TITLENAME = "name";
        }
        this.type = TAG_TYPE;
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
        title.setText((String) data.get(TAG_TITLENAME));

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
                FilmDescriptionDB film = new FilmDescriptionDB((int) data.get("id"), (String) data.get(TAG_TITLENAME), type, (String) data.get("poster_path"));
                WatchListDB watchListDB = new WatchListDB(getContext());
                int res = watchListDB.verifyId((int) data.get("id"));
                if(res==0) {
                    long row = watchListDB.insertFilm(film);
                    if (row != -1) {
                        Toast.makeText(view.getContext(), "Film added to your Watchlist", Toast.LENGTH_SHORT).show();
                        Fragment toRefresh = MainActivity.getToRefresh();
                        android.support.v4.app.FragmentTransaction ft = MainActivity.getFragmentTransaction();
                        if (toRefresh != null && ft != null) {
                            ft.detach(toRefresh);
                            ft.attach(toRefresh);
                            ft.commitAllowingStateLoss();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "It's already in your Watchlist!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(view.getContext(), "It's already in your Watchlist! (DATABASE CHECK)", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return convertView;
    }
}