package com.carrollnicholas.discocity;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MapActivity extends Activity {
    private ImageDetails id;
    private String latString;
    private String longiString;
    private String tagText;
    private DecimalFormat df2;
    private String distance;
    private String addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String TAG = "MapActivity";
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        id = (ImageDetails) extras.getParcelable("imageDetails");
        latString = id.getLatiData();
        longiString = id.getLongiData();
        tagText = id.getTagText();
        df2 = new DecimalFormat("###.##");
        distance = (df2.format(Double.parseDouble(id.getDistanceString())) + " metres away");
        addressText = getAddress(latString, longiString);

        final GoogleMap mMap;
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        initMap(mMap);
        mapAdapter(mMap);
    }




    private void initMap(GoogleMap mMap){
        mMap.setMyLocationEnabled(true);
        // Setting a custom info window adapter for the google map

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(latString), Double.parseDouble(longiString))));


        LatLng latLng = new LatLng(Double.parseDouble(latString), Double.parseDouble(longiString));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
    }
    private void mapAdapter(GoogleMap mMap){
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude
                TextView tvTag = (TextView) v.findViewById(R.id.tag);

                // Getting reference to the TextView to set longitude
                TextView tvDistance = (TextView) v.findViewById(R.id.distance);

                // Getting reference to the TextView to set longitude
                TextView tvStreet = (TextView) v.findViewById(R.id.street);

                // Setting the latitude
                tvTag.setText(tagText);

                // Setting the longitude
                tvDistance.setText(distance);

                tvStreet.setText(addressText + " ");

                // Returning the view containing InfoWindow contents
                return v;

            }
        });
    }
    public String getAddress( String latString, String longiString) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String addressText = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latString), Double.parseDouble(longiString), 1);
            for (Address a : addresses) {
                Address address = addresses.get(0);
                Log.v("help ", address.toString());
                addressText = String.format(
                        "%s, %s %s",
                        // If there's a street address, add it
                        address.getThoroughfare(),
                        // Locality is usually a city
                        address.getLocality(),
                        address.getSubAdminArea());
                // Return the text
                Log.v("help2 ", addressText);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressText;
    }
}
