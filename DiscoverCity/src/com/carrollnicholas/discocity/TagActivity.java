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
import android.os.Environment;
import android.support.v4.app.NavUtils;
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
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TagActivity extends ActionBarActivity  {

    Button mButton;
    EditText mEdit;

    String tagText;
    String descText;

    Double longi;
    Double lat;

    int brightness;

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
                            StringManipulation sm = new StringManipulation();
                            tagText = sm.tagString(tagText);
                            mEdit.setText(tagText);
                            Toast.makeText(TagActivity.this, "Your photo has been tagged!",
                                    Toast.LENGTH_LONG).show();

                            descText = String.valueOf(spinner.getSelectedItem()) ;
                            showCurrentLocation();
                            UploadImage ui = new UploadImage();
                            ui.execute(tagText, descText, Double.toString(longi), Double.toString(lat), Integer.toString(brightness));
                        }



                    }
                });

        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_5, this, mOpenCVCallBack))
        {
            Log.e("image", "Cannot connect to OpenCV Manager");
        }
        else{
            Log.e("image", "Connected to OpenCV Manager");
        }
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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
                String brightness = params[4];
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
                    entity.addPart("brightness", new StringBody(brightness));

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

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("image", "OpenCV loaded successfully");
                    Mat original_image = Highgui.imread(imageLocation);
                    Mat converted_image = original_image.clone();
                    List<Mat> ycrcb = new ArrayList<Mat>();
                    Imgproc.cvtColor(original_image, converted_image, Imgproc.COLOR_BGR2YCrCb);
                    Core.split(converted_image, ycrcb);
                    Scalar m = Core.mean(ycrcb.get(0));
                    double[] btns = m.val;
                    brightness = (int)btns[0];


                    Mat dy = new Mat();
                    Mat dx = new Mat();
                    Mat tmp = new Mat();

                    Imgproc.Sobel(original_image, dx,CvType.CV_32F, 1, 0);
                    Imgproc.Sobel(original_image, dy,CvType.CV_32F, 0, 1);




                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "DiscoverCity");

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    try {
                        Core.magnitude(dx, dy, dx);
                        Highgui.imwrite(mediaStorageDir.getPath() + File.separator + "DisCity_" + timeStamp + ".jpg", dx);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(TagActivity.this,Core.sumElems(dx).toString() ,
                            Toast.LENGTH_LONG).show();
                    Log.v("magnitude", Core.sumElems(dx).toString());
                    Log.v("image1: ", brightness+"");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onResume()
    {
        Log.i("image", "Called onResume");
        super.onResume();

        Log.i("image", "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_4, this, mOpenCVCallBack))
        {
            Log.e("image", "Cannot connect to OpenCV Manager");
        }
    }
}

