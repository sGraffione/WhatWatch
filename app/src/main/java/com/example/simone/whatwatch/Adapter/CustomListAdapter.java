package com.example.simone.whatwatch.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simone.whatwatch.ChatGroup.ChatGroup;
import com.example.simone.whatwatch.MainActivity;
import com.example.simone.whatwatch.R;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import database.Film;
import database.Tv;
import database.Database;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CustomListAdapter extends ArrayAdapter<HashMap<String, Object>> {

    ArrayList<HashMap<String, Object>> filmList;
    Context context;
    Activity activity;
    int resource;
    String TAG_TITLENAME;
    String type;
    private FirebaseAuth mAuth;
    private View view;

    public CustomListAdapter(Activity activity, Context context, int resource, ArrayList<HashMap<String, Object>> filmList, String TAG_TYPE) {
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
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FacebookSdk.sdkInitialize(getContext());
        ImageView imageView;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                convertView = layoutInflater.inflate(R.layout.film_element, null, true);

            }
            mAuth = FirebaseAuth.getInstance();
            view = convertView;
            final HashMap<String, Object> data = filmList.get(position);

            imageView = (ImageView) convertView.findViewById(R.id.photo);
            Button button = (Button) convertView.findViewById(R.id.btnAddToWatch);
            Button joinChat = (Button) convertView.findViewById(R.id.joinChat);

            Picasso.with(context).load((String) data.get("poster_path")).into(imageView);

            TextView title = (TextView) convertView.findViewById(R.id.Title);
            title.setText((String) data.get(TAG_TITLENAME));

            TextView rating = (TextView) convertView.findViewById(R.id.rating);
            Double ratingValue = (Double) data.get("vote_average");
            if (ratingValue < 6) {
                rating.setTextColor(Color.RED);
            } else if (ratingValue > 7.5) {
                rating.setTextColor(Color.GREEN);
            } else {
                rating.setTextColor(ContextCompat.getColor(getContext(), R.color.Orange));
            }
            rating.setText(new DecimalFormat("##.#").format(ratingValue));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Film film = null;
                    Tv tv = null;
                    Database database = new Database(getContext());
                    if (type.equals("movie")) {
                        if (database.verifyId((int) data.get("id")) == 0) {
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

                        } else {
                            Toast.makeText(view.getContext(), "It's already in your Watchlist!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (database.verifyId((int) data.get("id"), (int) data.get("id_season")) == 0) {
                            tv = new Tv((int) data.get("id"), (int) data.get("id_season"), (String) data.get(TAG_TITLENAME),
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

                        } else {
                            Toast.makeText(view.getContext(), "It's already in your Watchlist!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        /*joinChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flagWatched = false;
                final String uniqueIdDatabaseChatGroupWithoutMarcoR;      //Complimenti marco eh...non aiutarci..stronzo >:-(
                Database database = new Database(getContext());
                if(type.equals("movie")){
                    if(database.getWatched((int) data.get("id")) == 0){
                        final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.setTitle("You didn't watch this film!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok..", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }else{
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user != null){
                            flagWatched = true;
                        }else{
                            final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("You need to be logged with your facebook account");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                    uniqueIdDatabaseChatGroupWithoutMarcoR = String.valueOf((int) data.get("id")) + "_movie";
                }else{
                    if(database.getWatched((int) data.get("id"), (int) data.get("id_season")) == 0){
                        final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.setTitle("You didn't watch this serie!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }else{
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user != null){
                            flagWatched = true;
                        }else{
                            final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("You need to be logged with your facebook account");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                    uniqueIdDatabaseChatGroupWithoutMarcoR = String.valueOf((int) data.get("id")) + "_tv";
                }
                if(flagWatched){
                    Intent intent = new Intent(context, ChatGroup.class);
                    intent.putExtra("identifier", uniqueIdDatabaseChatGroupWithoutMarcoR);
                    context.startActivity(intent);
                }
            }
        });*/



            return convertView;
        }
}