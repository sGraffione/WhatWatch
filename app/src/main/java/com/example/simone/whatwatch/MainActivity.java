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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionPageAdapter;

    private ViewPager mViewPager;

    private Toolbar toolbar;

    String typeSelected = "movie";
    String searchTypeSelected = "popularity";
    String firstPart = "https://api.themoviedb.org/3/discover/";
    String secondPart = "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=true&page=1\n";
    int homepageSearchSelected = R.id.Popularity;
    int getHomepageSearchTypeSelected = R.id.Movies;

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
        adapter.addFragment(new Homepage(), "Homepage");
        adapter.addFragment(new MyWatchListFragment(), "My Watchlist");
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

        switch (getHomepageSearchTypeSelected){
            case R.id.Movies:
                menu.findItem(R.id.Movies).setChecked(true);
                break;
            case R.id.Tv_shows:
                menu.findItem(R.id.Tv_shows).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String URLSelected;
        int id = item.getGroupId();
        if(id == R.id.typeGroup){
            switch(item.getItemId()){
                case R.id.Movies:
                    getHomepageSearchTypeSelected = R.id.Movies;
                    typeSelected = "movie";
                    break;
                case R.id.Tv_shows:
                    getHomepageSearchTypeSelected = R.id.Tv_shows;
                    typeSelected = "tv";
                    break;
            }
        }else{
            switch (item.getItemId()){
                case R.id.Popularity:
                    homepageSearchSelected = R.id.Popularity;
                    secondPart = "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=true&page=1\n";
                    searchTypeSelected = "popularity";
                    break;
                case R.id.Most_Voted:
                    homepageSearchSelected = R.id.Most_Voted;
                    secondPart = "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=vote_count.desc&include_adult=false&include_video=false&page=1\n";
                    searchTypeSelected = "vote";
                    break;
            }
        }

        URLSelected = firstPart + typeSelected + secondPart;
        List<Fragment> allFragment = getSupportFragmentManager().getFragments();
        ((Homepage) allFragment.get(0)).refresh(URLSelected);
        return super.onOptionsItemSelected(item);
    }

    public void refreshFragmentWhatchList(){
        //List<Fragment> allFragment = getSupportFragmentManager().getFragments();
        MyWatchListFragment myWatchListFragment = new MyWatchListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(myWatchListFragment);
        fragmentTransaction.attach(myWatchListFragment);
        fragmentTransaction.commit();
    }

    public String getTypeSelected(){
        return typeSelected;
    }
    public String getTypeSearch(){
        return searchTypeSelected;
    }
}
