package com.carrollnicholas.discovercity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class TagActivity extends ActionBarActivity {

    Button mButton;
    EditText mEdit;
    EditText mEdit2;

    String tagText;
    String descText;
    ImageView imageView;
    Bitmap picturePreview;

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
        File imgFile = new  File(extras.getString("picturePreview"));
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageView);
            myImage.setImageBitmap(myBitmap);

        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
        new MyLocationListener()
        );





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

                    }
                });


    }

    private void showCurrentLocation() {

        MyLocationListener myLoc = new MyLocationListener();
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,myLoc,null);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        else{
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,myLoc,null);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if(location != null){
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(TagActivity.this, message,
                    Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(TagActivity.this, "location is null",
                    Toast.LENGTH_LONG).show();
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

}
