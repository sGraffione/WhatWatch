package com.example.simone.whatwatch.JSONParsingClasses;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import database.Database;


public class TitleParsing extends AsyncTask<String, Void, String> {

    private String type;
    HashMap<String, String> info;

    //Il construttore riceve un contesto e lo usa per istanziare la progressDialog
    public TitleParsing(String type)
    {
        info = new HashMap<>();
        this.type = type;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... urlString){
        //android.os.Debug.waitForDebugger();
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
            JSONObject film = new JSONObject(jsonString);
            String TAG_TITLE;
            if(type.equals("movie")){
                TAG_TITLE = film.getString("original_title");
                return TAG_TITLE;
            }else{
                TAG_TITLE = film.getString("name");
                return TAG_TITLE;
            }

        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}