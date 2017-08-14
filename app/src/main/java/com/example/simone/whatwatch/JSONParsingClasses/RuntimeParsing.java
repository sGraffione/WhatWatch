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


    public class RuntimeParsing extends AsyncTask<String, Void, Void> {

        private String type;
        private Context mContext;
        HashMap<String, Integer> info;

        //Il construttore riceve un contesto e lo usa per istanziare la progressDialog
        public RuntimeParsing(Context context, String type)
        {
            info = new HashMap<>();
            this.type = type;
            mContext = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

                String jsonString = sb.toString();
                JSONObject film = new JSONObject(jsonString);

                if(type.equals("movie")){

                    String TAG_RUNTIME = film.getString("runtime");
                    int runtime = 0;
                    if(TAG_RUNTIME.equals("null"))
                        runtime = 0;
                    else
                        runtime = Integer.parseInt(TAG_RUNTIME);

                    //General information of film
                    info.put("runtime", runtime);

                }else{
                    JSONArray jArrayRuntime = film.getJSONArray("episode_run_time");
                    int TAG_RUNTIME;
                    if (jArrayRuntime.length() != 0)
                        TAG_RUNTIME = (int) jArrayRuntime.get(0);
                    else
                        TAG_RUNTIME = 0;

                    //General information of film
                    info.put("runtime", TAG_RUNTIME);

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

            Database database = new Database(mContext);
            if(type.equals("movie")){
                database.updateFilmTime(info.get("runtime"));
            }else{
                database.updateSeriesTime(info.get("runtime"));
            }
        }
    }