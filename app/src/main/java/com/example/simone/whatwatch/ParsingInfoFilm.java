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

public class ParsingInfoFilm extends AsyncTask<String, Void, ArrayList<HashMap<String, Object>>>{

    private ProgressDialog pDialog;
    private ArrayList<HashMap<String, Object>> filmInfo;
    private String type;
    private int index;

    //Il construttore riceve un contesto e lo usa per istanziare la progressDialog
    public ParsingInfoFilm(Context context, String type, int index)
    {
        filmInfo = new ArrayList<>();
        this.type = type;
        this.index = index;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected ArrayList<HashMap<String, Object>> doInBackground(String... urlString){
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

                if(type.equals("movie")){

                    String TAG_TITLE = film.getString("original_title");
                    String TAG_OVERVIEW = film.getString("overview");
                    if(TAG_OVERVIEW.equals("null"))
                        TAG_OVERVIEW = "Overview not available";
                    String TAG_RATING = film.getString("vote_average");
                    String TAG_YEAR = film.getString("release_date");
                    if(TAG_YEAR.equals("null"))
                        TAG_YEAR = "Not available";
                    else
                        TAG_YEAR = TAG_YEAR.substring(0,4);
                    JSONArray TAG_GENRE = film.getJSONArray("genres");
                    if(TAG_GENRE.length()==0){
                        JSONObject object = new JSONObject();
                        object.put("name", "Genre not available");
                        TAG_GENRE.put(object);
                    }
                    String tmpStr = film.getString("poster_path");
                    String TAG_PHOTO = "https://image.tmdb.org/t/p/w780"+tmpStr;
                    if(tmpStr.equals("null"))
                        TAG_PHOTO = null;
                    String TAG_RUNTIME = film.getString("runtime");
                    if(TAG_RUNTIME.equals("null"))
                        TAG_RUNTIME = "Not available";

                    JSONObject jArrayCredits = film.getJSONObject("credits");
                    JSONArray jArrayCrew = jArrayCredits.getJSONArray("crew");
                    String TAG_DIRECTOR;
                    if(jArrayCrew.length() == 0)
                        TAG_DIRECTOR = "not available";
                    else
                        TAG_DIRECTOR = jArrayCrew.getJSONObject(0).getString("name");

                    JSONArray TAG_CAST = jArrayCredits.getJSONArray("cast");
                    if(TAG_CAST.length() == 0){
                        JSONObject object = new JSONObject();
                        object.put("cast", "Cast not available");
                        TAG_CAST.put(object);
                    }

                    JSONObject jArrayVideos = film.getJSONObject("videos");
                    JSONArray TAG_VIDEOS = jArrayVideos.getJSONArray("results");
                    if(TAG_VIDEOS.length() == 0){
                        JSONObject object = new JSONObject();
                        object.put("type", "Trailer");
                        TAG_VIDEOS.put(object);
                        object.put("key", "Video not available");
                        TAG_VIDEOS.put(object);
                    }



                    //General information of film
                    HashMap<String, Object> info = new HashMap<>();
                    info.put("original_title", TAG_TITLE);
                    info.put("overview", TAG_OVERVIEW);
                    info.put("poster_path", TAG_PHOTO);
                    info.put("vote_average", TAG_RATING);
                    info.put("genres", TAG_GENRE);
                    info.put("year", TAG_YEAR);
                    info.put("runtime", TAG_RUNTIME);
                    info.put("director", TAG_DIRECTOR);
                    info.put("cast", TAG_CAST);
                    info.put("videos", TAG_VIDEOS);

                    filmInfo.add(info);
                }else{
                        JSONArray TAG_SEASONS = film.getJSONArray("seasons");
                        String TAG_TITLE = film.getString("name");
                        int TAG_ID = film.getInt("id");
                        String TAG_OVERVIEW = film.getString("overview");
                        if (TAG_OVERVIEW.equals("null") || TAG_OVERVIEW.equals(""))
                            TAG_OVERVIEW = "Overview not available";
                        String TAG_RATING = film.getString("vote_average");
                        JSONArray TAG_GENRE = film.getJSONArray("genres");
                        if (TAG_GENRE.length() == 0) {
                            JSONObject object = new JSONObject();
                            object.put("name", "Genre not available");
                            TAG_GENRE.put(object);
                        }
                        String gliominidellamadonna = film.getString("poster_path");
                        String TAG_PHOTO = "null";
                        if (!gliominidellamadonna.equals("null")) {
                            TAG_PHOTO = "https://image.tmdb.org/t/p/w780" + gliominidellamadonna;
                        }

                        String TAG_YEAR = film.getString("first_air_date");
                        if (TAG_YEAR.equals("null"))
                            TAG_YEAR = "Release data not available";
                        else
                            TAG_YEAR = TAG_YEAR.substring(0, 4);
                        JSONArray jArrayRuntime = film.getJSONArray("episode_run_time");
                        int TAG_RUNTIME;
                        if (jArrayRuntime.length() != 0)
                            TAG_RUNTIME = (int) jArrayRuntime.get(0);
                        else
                            TAG_RUNTIME = 0;

                        JSONArray TAG_CREATOR = film.getJSONArray("created_by");
                        if (TAG_CREATOR.length() == 0) {
                            JSONObject object = new JSONObject();
                            object.put("name", "Creator not available");
                            TAG_CREATOR.put(object);
                        }


                        JSONObject jArrayCredits = film.getJSONObject("credits");
                        JSONArray TAG_CAST = jArrayCredits.getJSONArray("cast");

                        if (TAG_CAST.length() == 0) {
                            JSONObject object = new JSONObject();
                            object.put("cast", "Cast not available");
                            TAG_CAST.put(object);
                        }

                        JSONObject jArrayVideos = film.getJSONObject("videos");
                        JSONArray TAG_VIDEOS = jArrayVideos.getJSONArray("results");

                        if (TAG_VIDEOS.length() == 0) {
                            JSONObject object = new JSONObject();
                            object.put("type", "Trailer");
                            TAG_VIDEOS.put(object);
                            object.put("key", "Video not available");
                            TAG_VIDEOS.put(object);
                        }

                        //General information of film
                        HashMap<String, Object> info = new HashMap<>();
                        getExtraInfos(TAG_ID, info);
                        info.put("name", TAG_TITLE);
                        info.put("overview", TAG_OVERVIEW);
                        info.put("poster_path", TAG_PHOTO);
                        info.put("vote_average", TAG_RATING);
                        info.put("genres", TAG_GENRE);
                        info.put("seasons", TAG_SEASONS);
                        info.put("runtime", TAG_RUNTIME);
                        info.put("creator", TAG_CREATOR);
                        info.put("cast", TAG_CAST);
                        info.put("videos", TAG_VIDEOS);
                        info.put("year", TAG_YEAR);

                        filmInfo.add(info);
                }

        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return filmInfo;
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
                if((int) object.get("season_number") == index){
                    info.put("id_season", object.get("id"));
                    if((object.getString("poster_path")).equals("null")){
                        info.put("poster_path_season", "null");
                    }else{
                        info.put("poster_path_season", "https://image.tmdb.org/t/p/w500" + object.getString("poster_path"));
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
    protected void onPostExecute(ArrayList<HashMap<String, Object>> result){
        super.onPostExecute(result);

    }
}
