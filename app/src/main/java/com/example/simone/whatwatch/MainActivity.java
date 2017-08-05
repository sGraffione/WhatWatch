package com.example.simone.whatwatch;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.content.Context;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionPageAdapter;

    private ViewPager mViewPager;

    private Toolbar toolbar;

    private static Fragment toRefresh = null;
    private static Fragment toRefreshWatched = null;
    private static Fragment toRefreshHomepage = null;
    private static android.support.v4.app.FragmentManager fragmentManager = null;




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


}
