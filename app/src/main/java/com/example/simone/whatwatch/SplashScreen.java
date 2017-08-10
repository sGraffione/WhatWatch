package com.example.simone.whatwatch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Handler;


public class SplashScreen extends Activity{

    private static int SPLASH_TIME_OUT = 3000;
    String URLSelected = "https://api.themoviedb.org/3/discover/movie?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=true&page=1\n";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ArrayList<HashMap<String, Object>> filmInfo = new ArrayList<>();
                new downloadJSONInfo(filmInfo, "movie").execute(URLSelected);
            }
        }, SPLASH_TIME_OUT);

    }



    public class downloadJSONInfo extends AsyncTask<String, Void, Void> {

        private ArrayList<HashMap<String, Object>> filmInfo;
        private String type;

        //Il construttore riceve un contesto e lo usa per istanziare la progressDialog
        public downloadJSONInfo(ArrayList<HashMap<String, Object>> filmInfo, String type)
        {
            this.filmInfo = filmInfo;
            this.type = type;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... urlString){
            try {
                HttpURLConnection urlConnection;
                URL url = new URL(urlString[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    String lineToAppend = line + "\n";
                    sb.append(lineToAppend);
                }
                br.close();

                String jsonString = sb.toString();
                JSONObject JSONData = new JSONObject(jsonString);
                JSONArray jArray = JSONData.getJSONArray("results");
                if(type.equals("movie")){

                    for (int i = 0; i < jArray.length() - 10; i++) {
                        JSONObject film = jArray.getJSONObject(i);
                        String TAG_TITLE = film.getString("original_title");
                        String TAG_OVERVIEW = film.getString("overview");
                        int TAG_ID = film.getInt("id");
                        String TAG_PHOTO = "https://image.tmdb.org/t/p/w500"+film.getString("poster_path");
                        Double TAG_RATING = film.getDouble("vote_average");


                        //General information of film
                        HashMap<String, Object> info = new HashMap<>();
                        info.put("original_title", TAG_TITLE);
                        info.put("overview", TAG_OVERVIEW);
                        info.put("poster_path", TAG_PHOTO);
                        info.put("vote_average", TAG_RATING);
                        info.put("id", TAG_ID);

                        filmInfo.add(info);
                    }
                }else if(type.equals("tv")) {

                    for (int i = 0; i < jArray.length() - 10; i++) {
                        JSONObject tv_serie = jArray.getJSONObject(i);
                        String TAG_NAME = tv_serie.getString("name");
                        String TAG_OVERVIEW = tv_serie.getString("overview");
                        int TAG_ID = tv_serie.getInt("id");
                        String TAG_PHOTO = "https://image.tmdb.org/t/p/w500" + tv_serie.getString("poster_path");
                        Double TAG_RATING = tv_serie.getDouble("vote_average");
                        urlConnection.disconnect();


                        //General information of film
                        HashMap<String, Object> info = new HashMap<>();
                        getExtraInfos(TAG_ID, info);
                        info.put("name", TAG_NAME);
                        info.put("overview", TAG_OVERVIEW);
                        info.put("poster_path", TAG_PHOTO);
                        info.put("vote_average", TAG_RATING);
                        info.put("id", TAG_ID);

                        filmInfo.add(info);
                    }
                }

            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void getExtraInfos(int id, HashMap<String, Object> info){
            String urlString = "https://api.themoviedb.org/3/tv/"+id+"?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US\n";
            try {
                HttpURLConnection urlConnection;
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    String lineToAppend = line + "\n";
                    sb.append(lineToAppend);
                }
                br.close();

                String jsonString = sb.toString();
                JSONObject JSONData = new JSONObject(jsonString);
                JSONArray jArray = JSONData.getJSONArray("seasons");

                info.put("number_of_seasons", JSONData.get("number_of_seasons"));

                for(int i = 0; i < jArray.length(); i++){
                    JSONObject object = jArray.getJSONObject(i);
                    if((int) object.get("season_number") == 1){
                        info.put("id_season", object.get("id"));
                        info.put("poster_path_season", "https://image.tmdb.org/t/p/w500"+ object.getString("poster_path"));
                        info.put("episode_max_season", object.get("episode_count"));
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            i.putExtra("INFO", filmInfo);
            startActivity(i);

            finish();
        }
    }
}
