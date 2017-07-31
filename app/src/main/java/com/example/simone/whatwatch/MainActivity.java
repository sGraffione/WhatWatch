package com.example.simone.whatwatch;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionPageAdapter;

    private ViewPager mViewPager;

    private Toolbar toolbar;

    int homepageSearchSelected = R.id.Popularity;

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
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyWatchListFragment(), "My Watchlist");
        adapter.addFragment(new Homepage(), "Homepage");
        adapter.addFragment(new WatchedListFragment(), "Watched");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (homepageSearchSelected){
            case R.id.Popularity:
                menu.findItem(R.id.Popularity).setChecked(true);
                break;
            case R.id.Most_Voted:
                menu.findItem(R.id.Most_Voted).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String URLSelected = "https://api.themoviedb.org/3/discover/movie?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=true&page=1\n";
        int id = item.getItemId();

        switch (id){
            case R.id.Popularity:
                homepageSearchSelected = R.id.Popularity;
                URLSelected = "https://api.themoviedb.org/3/discover/movie?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=true&page=1\n";
                break;
            case R.id.Most_Voted:
                homepageSearchSelected = R.id.Most_Voted;
                URLSelected = "https://api.themoviedb.org/3/discover/movie?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=vote_count.desc&include_adult=false&include_video=false&page=1\n";
                break;
        }

        List<Fragment> allFragment = getSupportFragmentManager().getFragments();
        ((Homepage) allFragment.get(1)).refresh(URLSelected);
        return super.onOptionsItemSelected(item);
    }

}
