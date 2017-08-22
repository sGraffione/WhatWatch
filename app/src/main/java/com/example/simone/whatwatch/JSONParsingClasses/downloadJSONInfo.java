package com.example.simone.whatwatch.JSONParsingClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.simone.whatwatch.Adapter.CustomListAdapter;
import com.example.simone.whatwatch.R;

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
public class downloadJSONInfo extends AsyncTask<String, Void, Void> {

    private ProgressDialog pDialog;
    private ArrayList<HashMap<String, Object>> filmInfo;
    private ListView lv;
    private CustomListAdapter adapter;
    private Context context;
    private String type;
    private String searchType;
    private Activity activity;

    //Il construttore riceve un contesto e lo usa per istanziare la progressDialog
    public downloadJSONInfo(Activity activity, Context context, ArrayList<HashMap<String, Object>> filmInfo, ListView lv, String type, String searchType)
    {
        this.filmInfo = filmInfo;
        this.lv = lv;
        this.context = context;
        this.activity = activity;
        this.type = type;
        this.searchType = searchType;
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
            urlConnection.disconnect();

            String jsonString = sb.toString();
            JSONObject JSONData = new JSONObject(jsonString);
            JSONArray jArray = JSONData.getJSONArray("results");
            if(type.equals("movie")){

                for (int i = 0; i < jArray.length(); i++) {
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

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject tv_serie = jArray.getJSONObject(i);
                    String TAG_NAME = tv_serie.getString("name");
                    String TAG_OVERVIEW = tv_serie.getString("overview");
                    int TAG_ID = tv_serie.getInt("id");
                    if(!tv_serie.getString("poster_path").equals("null")) {
                        String TAG_PHOTO = "https://image.tmdb.org/t/p/w500" + tv_serie.getString("poster_path");
                        Double TAG_RATING = tv_serie.getDouble("vote_average");


                        //General information of film
                        HashMap<String, Object> info = new HashMap<>();
                        getExtraInfos(TAG_ID, info, TAG_PHOTO);
                        info.put("name", TAG_NAME);
                        info.put("overview", TAG_OVERVIEW);
                        info.put("poster_path", TAG_PHOTO);
                        info.put("vote_average", TAG_RATING);
                        info.put("id", TAG_ID);

                        filmInfo.add(info);
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void getExtraInfos(int id, HashMap<String, Object> info, String TAG){
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
            urlConnection.disconnect();

            String jsonString = sb.toString();
            JSONObject JSONData = new JSONObject(jsonString);
            JSONArray jArray = JSONData.getJSONArray("seasons");

            info.put("number_of_seasons", JSONData.get("number_of_seasons"));

            for(int i = 0; i < jArray.length(); i++){
                JSONObject object = jArray.getJSONObject(i);
                if((int) object.get("season_number") == 1){
                    info.put("id_season", object.get("id"));
                    if(object.getString("poster_path").equals("null")){
                        info.put("poster_path_season", TAG);
                    }else{
                        info.put("poster_path_season", "https://image.tmdb.org/t/p/w500" + object.get("poster_path"));
                    }
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
        if(pDialog.isShowing())
            pDialog.dismiss();

        adapter = new CustomListAdapter(activity, context, R.layout.film_element, filmInfo, type);
        lv.setAdapter(adapter);

    }
}