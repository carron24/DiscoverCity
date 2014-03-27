package com.carrollnicholas.discovercity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;


public class TagActivity extends ActionBarActivity {

    Button mButton;
    EditText mEdit;
    EditText mEdit2;

    String tagText;
    String descText;

    Double longi;
    Double lat;

    File imgFile;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    protected LocationManager locationManager;

    Location location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        mButton = (Button)findViewById(R.id.button);
        mEdit   = (EditText)findViewById(R.id.editText);
        mEdit2 = (EditText)findViewById(R.id.editText2);

        Bundle extras = getIntent().getExtras();
        imgFile = new  File(extras.getString("picturePreview"));
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageView);
            myImage.setImageBitmap(myBitmap);

        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {

                        tagText =  mEdit.getText().toString();
                        descText = mEdit2.getText().toString();
                        Log.v("Tag Text",tagText);
                        Log.v("Description Text", descText);
                        showCurrentLocation();
                        UploadImage ui = new UploadImage();
                        Log.v("Longitude", Double.toString(longi));
                        Log.v("Latitude",Double.toString(lat));
                        ui.execute(tagText, descText, Double.toString(longi), Double.toString(lat));

                    }
                });


    }

    private void showCurrentLocation() {

        MyLocationListener myLoc = new MyLocationListener();
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,myLoc,null);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null){
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(TagActivity.this, message,
                    Toast.LENGTH_LONG).show();
            longi = location.getLongitude();
            lat = location.getLatitude();

        }
        else{
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,myLoc,null);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(TagActivity.this, message,
                    Toast.LENGTH_LONG).show();
            longi = location.getLongitude();
            lat = location.getLatitude();

        }

    }
private class MyLocationListener implements LocationListener {
    public void onLocationChanged(Location location) {
        String message = String.format(
                "New Location \n Longitude: %1$s \n Latitude: %2$s",
                location.getLongitude(), location.getLatitude()
        );
        Toast.makeText(TagActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onStatusChanged(String s, int i, Bundle b){

    }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private class UploadImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
                String tText = params[0];
                String dText = params[1];
                String longiString = params[2];
                String latString = params[3];
                try{

                    // 1. create HttpClient
                    HttpClient httpclient = new DefaultHttpClient();

                    // 2. make POST request to the given URL
                    HttpPost httpPost = new HttpPost("http://172.245.128.203:3000/addimage");

                    String json = "";

                    // 3. build jsonObject
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("tagText", tText);
                    jsonObject.accumulate("descText", dText);
                    jsonObject.accumulate("longitude", longiString);
                    jsonObject.accumulate("latitude", latString);


                    // 4. convert JSONObject to JSON to String
                    json = jsonObject.toString();

                    // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                   //  ObjectMapper mapper = new ObjectMapper();
                    // json = mapper.writeValueAsString(person);

                    // 5. set json to StringEntity


                    //ContentBody cbFile = new FileBody(imgFile, "image/jpeg" );
                    //entity.addPart("source", cbFile);
                   StringEntity se = new StringEntity(json);

                    // 6. set httpPost Entity
                    httpPost.setEntity(se);


                    httpPost.setHeader("Accept", "application/json");
                    httpPost.setHeader("Content-type", "application/json");
                    // 8. Execute POST request to the given URL
                    HttpResponse httpResponse = httpclient.execute(httpPost);

                   MultipartEntity entity = new MultipartEntity(
                            HttpMultipartMode.BROWSER_COMPATIBLE);
                   // File file = new File(imgFile);
                    FileBody cbFile = new FileBody( imgFile, "image/jpeg");
                    entity.addPart("image", cbFile);
                    entity.addPart("name", new StringBody("test"));
                    entity.addPart("tagTest", new StringBody("hello"));
                    HttpPost httpPost2 = new HttpPost("http://172.245.128.203:3000/newimage");

                    // httpPost2.setHeader("Content-Type", "multipart/form-data ");
                    httpPost2.setEntity(entity);
                    //httpPost2.setHeader("Accept", "multipart/form-data ");
                    HttpResponse httpResponse2 = httpclient.execute(httpPost2);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            return "";


        }
    }
}
