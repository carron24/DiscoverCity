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
    static public ArrayList<ImageDetails> id;
    static public ArrayList<ImageDetails> tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_return);
        id = new ArrayList<ImageDetails>();

        int[] button_array = {R.id.button,R.id.button2,R.id.button3,R.id.button4,R.id.button5};
        for(int id : button_array){
            ((Button)findViewById(id)).setOnClickListener(onClickListener);
        }

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

        Bundle extras = getIntent().getExtras();
        double longitude = Double.parseDouble(extras.getString("longitude"));
        double latitude = Double.parseDouble(extras.getString("latitude"));

        JsonToInfo jt = new JsonToInfo(this, extras.getString("tagText"));
        Location loc = new Location("point a");
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);


        jt.infoReturn(jsonArray);
        tmp = jt.closestImages(id,loc);



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
                    myIntent.putExtra("imageDetails", tmp.get(0));
                    break;
                case R.id.button2:
                    myIntent.putExtra("imageDetails", tmp.get(1));
                    break;
                case R.id.button3:
                    myIntent.putExtra("imageDetails", tmp.get(2));
                    break;
                case R.id.button4:
                    myIntent.putExtra("imageDetails", tmp.get(3));
                    break;
                case R.id.button5:
                    myIntent.putExtra("imageDetails", tmp.get(4));
                    break;
            }
            ImageReturn.this.startActivity(myIntent);

        }
    };

}



