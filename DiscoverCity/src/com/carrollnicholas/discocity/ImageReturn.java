package com.carrollnicholas.discocity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
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

        Bundle extras = getIntent().getExtras();
        double longitude = Double.parseDouble(extras.getString("longitude"));
        double latitude = Double.parseDouble(extras.getString("latitude"));
        Location loc = new Location("point a");
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);


        jt.infoReturn(jsonArray);
        id = jt.closestImages(id,loc);
        jt.displayReturnData(id);

        b.setOnClickListener(onClickListener);
        b1.setOnClickListener(onClickListener);
        b2.setOnClickListener(onClickListener);
        b3.setOnClickListener(onClickListener);
        b4.setOnClickListener(onClickListener);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        NavUtils.navigateUpFromSameTask(this);
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

    private static final String TAG = "JSONtoInfo";


    public JsonToInfo(Activity ac){
        this.activity = ac;
    }


    public ArrayList<Double> getDistances(ArrayList<ImageDetails> id, Location loc){
        ArrayList<Double> distances = new ArrayList<Double>();
        for (int i = 0; i < id.size(); i++) {
            JSONObject json;
            ImageDetails image = id.get(i);

            double longi = Double.parseDouble(image.longiData);
            double lati =  Double.parseDouble(image.latiData);
            Location loc1 = new Location("point b");

            loc1.setLatitude(lati);
            loc1.setLongitude(longi);

            double distance = loc.distanceTo(loc1);
            distances.add(distance);
            id.get(i).distance = distance;
            id.get(i).distanceString = distance + "";
        }
        return distances;
    }

    public ArrayList<ImageDetails> removeDupes(ArrayList<ImageDetails> id){
        ArrayList<ImageDetails> tempList = new ArrayList<ImageDetails>();
        Set<ImageDetails> s = new TreeSet<ImageDetails>(new Comparator<ImageDetails>() {

            @Override
            public int compare(ImageDetails o1, ImageDetails o2) {
                if(o1.equals(o2)) {
                    return 0;
                }
                else
                    return 1;
            }
        });
        s.addAll(id);
        tempList.addAll(s);
        Log.v(TAG, id.size() + "");
        Log.v(TAG, tempList.size() + "");

        return tempList;
    }
    public ArrayList<ImageDetails> closestImages(ArrayList<ImageDetails> id, Location loc){
        ArrayList<Double> distances = getDistances(id, loc);
        ArrayList<ImageDetails> tempList = new ArrayList<ImageDetails>();
        id = removeDupes(id);
        Collections.sort(distances);
        for(int i = 0; i < 10; i++){
            for(ImageDetails temp : id){
                if(distances.get(i) == temp.distance ){
                    tempList.add(temp);
                }
            }
        }
        for(ImageDetails temp : tempList){
            Log.v(TAG, ""+ temp.distance);
        }
        return tempList;
    }

    public JSONArray infoReturn(JSONArray jsonArray){
        JSONArray jsReturn = null;
        for (int i = 0; i < jsonArray.length()-1; i++) {
            JSONObject json;
            try {
                json = jsonArray.getJSONObject(i);
                ImageDetails jsonDetails = new ImageDetails(json);
                ImageReturn.id.add(jsonDetails);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return jsReturn;
    }

    public void displayReturnData(ArrayList<ImageDetails> id){
        Log.v("size", id.size() + "");
        for(int i = 0; i < 5; i++){
            tagReturn(id.get(i), i);
            descReturn(id.get(i), i);
            locReturn(id.get(i), i);
            imageReturn(id.get(i), i);

        }

    }


    public void tagReturn(ImageDetails id, int i){
            String type = id.tagText;
            int[] ids = {R.id.textView, R.id.textView4, R.id.textView7, R.id.textView10, R.id.textView13};
            TextView text = (TextView) this.activity.findViewById(ids[i]);
            text.setText(type);
    }

    public void descReturn(ImageDetails id, int i){

            String type = id.descText;
            int[] ids = {R.id.textView2, R.id.textView5, R.id.textView8, R.id.textView11, R.id.textView14};
            TextView text = (TextView) this.activity.findViewById(ids[i]);
            text.setText(type);
    }

    public void locReturn(ImageDetails id, int i){

            int[] ids = {R.id.textView3, R.id.textView6, R.id.textView9, R.id.textView12, R.id.textView15};
            TextView text = (TextView) this.activity.findViewById(ids[i]);
            DecimalFormat df2 = new DecimalFormat("###.##");
            String type = Double.valueOf(df2.format(id.distance)) + " metres away";
            text.setText(type);
    }

    public void imageReturn(ImageDetails id, int i){

            String type = id.imageLink;
            String s = "http://nicholascarroll.info:3000" + type;
            int[] ids = {R.id.imageView, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5};
            new DownloadImageTask((ImageView)this.activity.findViewById(ids[i]))
                    .execute(s);

    }
}


