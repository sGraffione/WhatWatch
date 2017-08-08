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
public class JSONSearch extends AsyncTask<String, Void, ArrayList<HashMap<String, Object>>> {


    private ArrayList<HashMap<String, Object>> filmInfo;

    //Il construttore riceve un contesto e lo usa per istanziare la progressDialog
    public JSONSearch()
    {
        filmInfo = new ArrayList<>();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<HashMap<String, Object>> doInBackground(String... urlString){
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
                    if (!TAG_TYPE.equals("person")) {
                        if (TAG_TYPE.equals("movie")) {
                            TAG_TITLE = film.getString("original_title");
                        }else {
                            TAG_TITLE = film.getString("name");

                        }
                        int TAG_ID = film.getInt("id");

                        String TAG_PHOTO;
                        String str = film.getString("poster_path");
                        if(film.get("poster_path").equals("null"))
                            TAG_PHOTO = "null";
                        else
                            TAG_PHOTO = "https://image.tmdb.org/t/p/w500" + str;

                        Double TAG_RATING = film.getDouble("vote_average");


                        //General information of film
                        HashMap<String, Object> info = new HashMap<>();
                        if(TAG_TYPE.equals("tv"))
                            getExtraInfos(TAG_ID, info);

                        info.put("title", TAG_TITLE);
                        info.put("type", TAG_TYPE);
                        info.put("poster_path", TAG_PHOTO);
                        info.put("vote_average", TAG_RATING);
                        info.put("id", TAG_ID);

                        filmInfo.add(info);
                    }else{
                        JSONArray jArrayPerson = film.getJSONArray("known_for");
                        for(int j = 0; j < jArrayPerson.length(); j++) {
                            JSONObject data = jArrayPerson.getJSONObject(j);
                            TAG_TYPE = data.getString("media_type");
                            if (TAG_TYPE.equals("movie"))
                                TAG_TITLE = data.getString("original_title");
                            else
                                TAG_TITLE = data.getString("name");
                            int TAG_ID = data.getInt("id");
                            String TAG_PHOTO = "https://image.tmdb.org/t/p/w500" + data.getString("poster_path");
                            Double TAG_RATING = data.getDouble("vote_average");


                            //General information of film
                            HashMap<String, Object> info = new HashMap<>();
                            if(TAG_TYPE.equals("tv"))
                                getExtraInfos(TAG_ID, info);

                            info.put("title", TAG_TITLE);
                            info.put("type", TAG_TYPE);
                            info.put("poster_path", TAG_PHOTO);
                            info.put("vote_average", TAG_RATING);
                            info.put("id", TAG_ID);

                            filmInfo.add(info);
                        }
                    }
                }
                return filmInfo;
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
                    info.put("poster_path_season", object.get("poster_path"));
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
    protected void onPostExecute(ArrayList<HashMap<String, Object>> result){
        super.onPostExecute(result);
    }
}