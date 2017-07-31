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

public class ParsingInfoFilm extends AsyncTask<String, Void, Void>{

    private ProgressDialog pDialog;
    private ArrayList<HashMap<String, Object>> filmInfo;
    private ListView lv;
    private CustomListAdapter adapter;
    private Context context;

    //Il construttore riceve un contesto e lo usa per istanziare la progressDialog
    public ParsingInfoFilm(Context context, ArrayList<HashMap<String, Object>> filmInfo)
    {
        this.filmInfo = filmInfo;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Retrieving information...");
        pDialog.setCancelable(false);
        pDialog.show();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(adapter != null){
            adapter.clear();
        }
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
                String TAG_TITLE = film.getString("original_title");
                String TAG_OVERVIEW = film.getString("overview");
                String TAG_RATING = film.getString("vote_average");
                String TAG_RELEASE = film.getString("release_date");
                JSONArray TAG_GENRE = JSONData.getJSONArray("genres");
                String TAG_PHOTO = "https://image.tmdb.org/t/p/w300"+film.getString("poster_path");


                //General information of film
                HashMap<String, Object> info = new HashMap<>();
                info.put("original_title", TAG_TITLE);
                info.put("overview", TAG_OVERVIEW);
                info.put("poster_path", TAG_PHOTO);
                info.put("position", i);
                info.put("vote_average", TAG_RATING);
                info.put("release_date", TAG_RELEASE);
                info.put("genres", TAG_GENRE);

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
        if(pDialog.isShowing())
            pDialog.dismiss();

    }
}
