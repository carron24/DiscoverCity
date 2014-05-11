package com.carrollnicholas.discocity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;


public class TagActivity extends ActionBarActivity {

    Button mButton;
    EditText mEdit;
    EditText mEdit2;

    String tagText;
    String descText;

    Double longi;
    Double lat;

    String imageLocation;

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
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        imageLocation = extras.getString("picturePreview");
        File imgFile = new  File(imageLocation);



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
                        if(tagText.equals("")){
                            Toast.makeText(TagActivity.this, "tag cannot be empty",
                                    Toast.LENGTH_LONG).show();

                        }
                        else{
                            tagText =  mEdit.getText().toString();
                            descText = String.valueOf(spinner.getSelectedItem()) ;
                            Log.v("Tag Text",tagText);
                            Log.v("Description Text", descText);
                            showCurrentLocation();
                            UploadImage ui = new UploadImage();
                            Log.v("Longitude", Double.toString(longi));
                            Log.v("Latitude",Double.toString(lat));
                            ui.execute(tagText, descText, Double.toString(longi), Double.toString(lat));
                        }



                    }
                });


    }

    private void showCurrentLocation() {

        MyLocationListener myLoc = new MyLocationListener();
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,myLoc,null);
        LocationManager locationManager2 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager2.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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

                    HttpClient client = new DefaultHttpClient();
                    HttpPost poster = new HttpPost("http://nicholascarroll.info:3000/newimage");

                    File imageFile = new File(imageLocation);
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("image", new FileBody(imageFile));
                    entity.addPart("tagText", new StringBody(tText));
                    entity.addPart("descText", new StringBody(dText));
                    entity.addPart("longitude", new StringBody(longiString));
                    entity.addPart("latitude", new StringBody(latString));

                    poster.setEntity(entity);

                    client.execute(poster);

                    Intent myIntent = new Intent(TagActivity.this, ImageReturn.class);
                    myIntent.putExtra("longitude", longiString);
                    myIntent.putExtra("latitude", latString);
                    TagActivity.this.startActivity(myIntent);



                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            return "";


        }
    }
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}
