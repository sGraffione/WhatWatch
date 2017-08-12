package com.example.simone.whatwatch;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.simone.whatwatch.Adapter.SectionsPageAdapter;
import com.example.simone.whatwatch.Classes.SearchActivity;
import com.example.simone.whatwatch.FragmentClasses.Homepage;
import com.example.simone.whatwatch.FragmentClasses.MyWatchListFragment;
import com.example.simone.whatwatch.FragmentClasses.WatchedListFragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionPageAdapter;

    private ViewPager mViewPager;

    private Toolbar toolbar;

    CallbackManager callbackManager;

    public static ArrayList<HashMap<String, Object>> filmInfo = new ArrayList<>();

    public static Fragment toRefresh = null;
    public static Fragment toRefreshWatched = null;
    public static Fragment toRefreshHomepage = null;
    public static android.support.v4.app.FragmentManager fragmentManager = null;


    public static ArrayList<HashMap<String, Object>> getFilmInfo(){return filmInfo;}

    public static Fragment getToRefresh() {
        return toRefresh;
    }

    public static Fragment getToRefreshWatched() {return toRefreshWatched;}


    public static  android.support.v4.app.FragmentTransaction getFragmentTransaction() {
        return fragmentManager.beginTransaction();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fragmentManager = getSupportFragmentManager();

        Log.d("MAIN ACTIVITY", "Preso filmInfo");
        filmInfo = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("INFO");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("ONRESUME", "MAIN ACTIVITY");
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        toRefreshHomepage = new Homepage();
        adapter.addFragment(toRefreshHomepage, "Homepage");
        toRefresh = new MyWatchListFragment();
        toRefreshWatched = new WatchedListFragment();
        adapter.addFragment(toRefresh, "My Watchlist");
        adapter.addFragment(toRefreshWatched, "Watched");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchView search = (SearchView) MenuItemCompat.getActionView((menu.findItem(R.id.action_search)));
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
        search.setQueryHint(getResources().getString(R.string.search_hint));
        return true;
    }
}
