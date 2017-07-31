package com.example.simone.whatwatch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowInfoAboutListElement extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_about_list_element);

        TextView Title = (TextView) findViewById(R.id.Title);
        Title.setText(getIntent().getStringExtra("Title"));

    }
}
