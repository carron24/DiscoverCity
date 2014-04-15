package com.carrollnicholas.discocity;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Nicholas on 15/04/2014.
 */
class RetrieveImages extends AsyncTask<String, Void, JSONArray> {

    @Override
    protected JSONArray doInBackground(String...params) {
        StringBuilder builder = new StringBuilder();
        String json = null;
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make GET request to the given URL
        HttpGet httpGet = new HttpGet("http://nicholascarroll.info:3000/getimages");
        HttpResponse response;
        try{
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null){
                InputStream instream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line + "\n");
                }
                reader.close();
                json = builder.toString();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            jsonArray = new JSONArray(json);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return jsonArray;
    }
}