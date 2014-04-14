package com.carrollnicholas.discocity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ImageReturn extends ActionBarActivity {

    Button b;
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    Button b5;
    static public ArrayList<ImageDetails> id = new ArrayList<ImageDetails>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_return);
        b = (Button) findViewById(R.id.button);
        b1 = (Button) findViewById(R.id.button2);
        b2 = (Button) findViewById(R.id.button3);
        b3 = (Button) findViewById(R.id.button4);
        b4 = (Button) findViewById(R.id.button5);
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
        JsonToInfo jt = new JsonToInfo(this);
        jt.infoReturn(jsonArray);
        b.setOnClickListener(onClickListener);
        b1.setOnClickListener(onClickListener);
        b2.setOnClickListener(onClickListener);
        b3.setOnClickListener(onClickListener);
        b4.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(ImageReturn.this, ResultActivity.class);
            switch (v.getId()) {
                case R.id.button:
                    myIntent.putExtra("imageDetails", id.get(0));
                    break;
                case R.id.button2:
                    myIntent.putExtra("imageDetails", id.get(1));
                    break;
                case R.id.button3:
                    myIntent.putExtra("imageDetails", id.get(2));
                    break;
                case R.id.button4:
                    myIntent.putExtra("imageDetails", id.get(3));
                    break;
                case R.id.button5:
                    myIntent.putExtra("imageDetails", id.get(4));
                    break;
            }
            ImageReturn.this.startActivity(myIntent);

        }
    };
}

class JsonToInfo{
    public Activity activity;

    public JsonToInfo(Activity ac){
        this.activity = ac;
    }

    public JSONArray infoReturn(JSONArray jsonArray){
        JSONArray jsReturn = null;
        for (int i = 0; i < 5; i++) {
            JSONObject json;
            try {
                json = jsonArray.getJSONObject(i);
                tagReturn(json, i);
                descReturn(json, i);
                locReturn(json, i);
                imageReturn(json, i);
                ImageDetails jsonDetails = new ImageDetails(json);
                ImageReturn.id.add(jsonDetails);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return jsReturn;
    }

    public void tagReturn(JSONObject json, int i){
        try{
            String type = json.getString("tagText");
            int[] ids = {R.id.textView, R.id.textView4, R.id.textView7, R.id.textView10, R.id.textView13};
            TextView text = (TextView) this.activity.findViewById(ids[i]);
            text.setText(type);
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public void descReturn(JSONObject json, int i){
        try{
            String type = json.getString("descText");
            int[] ids = {R.id.textView2, R.id.textView5, R.id.textView8, R.id.textView11, R.id.textView14};
            TextView text = (TextView) this.activity.findViewById(ids[i]);
            text.setText(type);
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void locReturn(JSONObject json, int i){
        try{
            String type = json.getString("longitude");
            String type1 = json.getString("latitude");
            String loc = type + " " + type1;
            int[] ids = {R.id.textView3, R.id.textView6, R.id.textView9, R.id.textView12, R.id.textView15};
            TextView text = (TextView) this.activity.findViewById(ids[i]);
            text.setText(loc);
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void imageReturn(JSONObject json, int i){
        try{
            String type = json.getString("image");
            String s = "http://nicholascarroll.info:3000" + type;
            int[] ids = {R.id.imageView, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5};
            new DownloadImageTask((ImageView)this.activity.findViewById(ids[i]))
                    .execute(s);
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

