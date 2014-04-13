package com.carrollnicholas.discocity;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
import java.util.concurrent.ExecutionException;

public class ImageReturn extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_return);
        JSONArray jsonArray = null;
        RetrieveImages ri = new RetrieveImages();
        String s = "";
        try {
            jsonArray = ri.execute(s).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < 5; i++) {
            JSONObject json;
            try {
                json = jsonArray.getJSONObject(i);
                String type = json.getString("tagText");
                int[] ids = {R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6};
                TextView text = (TextView) findViewById(ids[i]);
                text.setText(type);


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

}
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