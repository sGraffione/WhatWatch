package com.example.simone.whatwatch.Adapter;

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

import com.example.simone.whatwatch.MainActivity;
import com.example.simone.whatwatch.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import database.Film;
import database.Tv;
import database.Database;

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
                Film film = null;
                Tv tv = null;
                Database database = new Database(getContext());
                if(type.equals("movie")) {
                    if(database.verifyId((int) data.get("id")) == 0) {
                        film = new Film((int) data.get("id"), (String) data.get(TAG_TITLENAME), 0, (String) data.get("poster_path"));
                        database.insertFilm(film);

                        Toast.makeText(view.getContext(), "Film added to your Watchlist", Toast.LENGTH_SHORT).show();
                        Fragment toRefresh = MainActivity.getToRefresh();
                        android.support.v4.app.FragmentTransaction ft = MainActivity.getFragmentTransaction();
                        if (toRefresh != null && ft != null) {
                            ft.detach(toRefresh);
                            ft.attach(toRefresh);
                            ft.commitAllowingStateLoss();
                        }

                    }else{
                        Toast.makeText(view.getContext(), "It's already in your Watchlist!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(database.verifyId((int) data.get("id"), (int) data.get("id_season")) == 0) {
                        tv = new Tv((int) data.get("id"),(int) data.get("id_season"), (String) data.get(TAG_TITLENAME),
                                (int) data.get("episode_max_season"), (int) data.get("number_of_seasons"), 0, (String) data.get("poster_path"),
                                (String) data.get("poster_path_season"));
                        database.insertSeries(tv);

                        Toast.makeText(view.getContext(), "Serie added to your Watchlist", Toast.LENGTH_SHORT).show();
                        Fragment toRefresh = MainActivity.getToRefresh();
                        android.support.v4.app.FragmentTransaction ft = MainActivity.getFragmentTransaction();
                        if (toRefresh != null && ft != null) {
                            ft.detach(toRefresh);
                            ft.attach(toRefresh);
                            ft.commitAllowingStateLoss();
                        }

                    }else{
                        Toast.makeText(view.getContext(), "It's already in your Watchlist!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return convertView;
    }
}