package com.example.simone.whatwatch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import java.util.StringTokenizer;

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
        HashMap<String, Object> data = filmList.get(position);

        imageView = (ImageView) convertView.findViewById(R.id.photo);

        /*Target target = new Target() {
            private View convertView;
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);   //imposta l'immagine di sfondo di ImageView
                Drawable image = imageView.getDrawable();
                convertView.setBackground(image);   //ritorna un drawable e lo usa per impostare lo sfondo dell'item
                imageView.setImageBitmap(null);     //rimuove lo sfondo da ImageView
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}

            private Target init(View convertView){
                this.convertView = convertView;
                return this;
            }
        }.init(convertView);

        Picasso.with(context).load((String) data.get("backdrop_path")).into(target);*/

        Picasso.with(context).load((String) data.get("poster_path")).into(imageView);

        TextView title = (TextView) convertView.findViewById(R.id.Title);
        title.setText((String) data.get("original_title"));
        Button btnAddElement = (Button) convertView.findViewById(R.id.btnAddToWatch);
        btnAddElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }
}