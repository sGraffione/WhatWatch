package com.example.simone.whatwatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

//caricamento del file json in background
public class JSONSearch extends AsyncTask<String, Void, Void> {


    private ArrayList<HashMap<String, Object>> filmInfo;

    //Il construttore riceve un contesto e lo usa per istanziare la progressDialog
    public JSONSearch(ArrayList<HashMap<String, Object>> filmInfo)
    {
        this.filmInfo = filmInfo;
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
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject film = jArray.getJSONObject(i);
                    String TAG_TYPE = film.getString("media_type");
                    String TAG_TITLE;
                    if(TAG_TYPE.equals("movie"))
                        TAG_TITLE = film.getString("original_title");
                    else
                        TAG_TITLE = film.getString("name");
                    int TAG_ID = film.getInt("id");
                    String TAG_PHOTO = "https://image.tmdb.org/t/p/w185"+film.getString("poster_path");
                    Double TAG_RATING = film.getDouble("vote_average");


                    //General information of film
                    HashMap<String, Object> info = new HashMap<>();
                    info.put("title", TAG_TITLE);
                    info.put("type", TAG_TYPE);
                    info.put("poster_path", TAG_PHOTO);
                    info.put("vote_average", TAG_RATING);
                    info.put("id", TAG_ID);

                    filmInfo.add(info);
                }
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
    }
}