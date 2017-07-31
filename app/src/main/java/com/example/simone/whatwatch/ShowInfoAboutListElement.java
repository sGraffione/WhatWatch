package com.example.simone.whatwatch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class ShowInfoAboutListElement extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_about_list_element);

        TextView Title = (TextView) findViewById(R.id.Title);
        ImageView poster = (ImageView) findViewById(R.id.poster);
        Button add_button = (Button) findViewById(R.id.add_button);
        TextView overview = (TextView) findViewById(R.id.Overview);
        TextView rating = (TextView) findViewById(R.id.rating);



    }
}
