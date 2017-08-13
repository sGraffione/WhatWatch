package com.example.simone.whatwatch.FragmentClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simone.whatwatch.Adapter.WatchedAdapter;
import com.example.simone.whatwatch.ChatGroup.ChatGroup;
import com.example.simone.whatwatch.R;
import com.example.simone.whatwatch.Classes.ShowInfoAboutListElement;
import com.example.simone.whatwatch.Classes.ShowInfoAboutTvElement;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import database.AndroidDatabaseManager;
import database.Film;
import database.Tv;
import database.Database;


public class WatchedListFragment extends Fragment {

    private static final String TAG = "Watched";

    private GridView gridView;

    private WatchedAdapter watchedAdapter = null;

    private LoginButton loginButton;

    CallbackManager callbackManager;

    String typeSelected = "All";
    String sortingType = "Alphabetical";
    int typeSelectedId = R.id.All;

    TextView film_count;
    TextView minutes_count;
    TextView series_count;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_watched, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext());
        View view = inflater.inflate(R.layout.fragment_watched_list, container, false);
        initializeControls(view);
        loginWithFb();

        mAuth = FirebaseAuth.getInstance();

        view.findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = view.getId();
                if(i == R.id.logout_button){
                    signOut();
                }
            }
        });

        view.findViewById(R.id.open_activity_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatGroup.class);
                startActivity(intent);
            }
        });







        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Really_Really_Dark_Gray));
        film_count = (TextView) view.findViewById(R.id.film_count);
        series_count = (TextView) view.findViewById(R.id.series_count);
        minutes_count = (TextView) view.findViewById(R.id.minutes_count);

        Database database = new Database(getContext());
        final ArrayList<Object> films = database.getFilter(1, typeSelected, sortingType);

        film_count.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent dbmanager = new Intent(getActivity(), AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });

        /*film_count.setText(Integer.toString(database.getFilmsSeen()));
        series_count.setText(Integer.toString(database.getSeriesSeen()));
        minutes_count.setText(Integer.toString(database.getFilmsTime() + database.getSeriesTime()));*/

        WatchedAdapter watchedAdapter = new WatchedAdapter(getActivity(), films);
        gridView.setAdapter(watchedAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (films.get(i) instanceof Film) {
                    int id_film = ((Film) films.get(i)).getId();
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", "movie");
                    startActivity(appInfo);
                } else {
                    int id_tv = ((Tv) films.get(i)).getIdSeries();
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutTvElement.class);
                    appInfo.putExtra("id", id_tv);
                    appInfo.putExtra("type", "tv");
                    startActivity(appInfo);
                }
            }
        });
        setHasOptionsMenu(true);
        return view;
    }


    private void handleFacebookAccessToken(AccessToken token) {
    Log.d(TAG, "handleFacebookAccessToken:" + token.getToken());

    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
    mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                        Log.d("BANANA", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                    // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                    }

                    // ...
                }
            });
    }


    private void initializeControls(View v){
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.setFragment(this);
    }

    private void loginWithFb(){
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook Login","Success: " + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                film_count.setText("Canceled");
            }

            @Override
            public void onError(FacebookException e) {
                minutes_count.setText("Login error: " + e.getMessage());
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            minutes_count.setText(user.getDisplayName());
            series_count.setText(user.getUid());

            getActivity().findViewById(R.id.login_button).setVisibility(View.GONE);
            getActivity().findViewById(R.id.logout_button).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.open_activity_test).setVisibility(View.VISIBLE);
        } else {
            minutes_count.setText("Disconnected");
            series_count.setText(null);

            getActivity().findViewById(R.id.login_button).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.logout_button).setVisibility(View.GONE);
            getActivity().findViewById(R.id.open_activity_test).setVisibility(View.GONE);
        }
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Log.d("Facebook logout", "Uscito");
        updateUI(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        switch (typeSelectedId) {
            case R.id.All:
                menu.findItem(R.id.All).setChecked(true);
                break;
            case R.id.Movies:
                menu.findItem(R.id.Movies).setChecked(true);
                break;
            case R.id.Tv_shows:
                menu.findItem(R.id.Tv_shows).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.All:
                typeSelectedId = R.id.All;
                typeSelected = "All";
                break;
            case R.id.Movies:
                typeSelectedId = R.id.Movies;
                typeSelected = "Movie";
                break;
            case R.id.Tv_shows:
                typeSelectedId = R.id.Tv_shows;
                typeSelected = "Tv_shows";
                break;
        }
        refresh();
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        Database database = new Database(getContext());
        final ArrayList<Object> films = database.getFilter(1, typeSelected, sortingType);
        if (films != null) {
            watchedAdapter = new WatchedAdapter(getContext(), films);
            gridView.setAdapter(watchedAdapter);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (films.get(i) instanceof Film) {
                    int id_film = ((Film) films.get(i)).getId();
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutListElement.class);
                    appInfo.putExtra("id", id_film);
                    appInfo.putExtra("type", "movie");
                    startActivity(appInfo);
                } else {
                    int id_tv = ((Tv) films.get(i)).getIdSeries();
                    Intent appInfo = new Intent(getActivity(), ShowInfoAboutTvElement.class);
                    appInfo.putExtra("id", id_tv);
                    appInfo.putExtra("type", "tv");
                    startActivity(appInfo);
                }
            }
        });
    }
}