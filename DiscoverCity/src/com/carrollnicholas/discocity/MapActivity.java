package com.carrollnicholas.discocity;

import android.app.Activity;
import android.os.Bundle;
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

import java.text.DecimalFormat;

public class MapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        final ImageDetails id = (ImageDetails) extras.getParcelable("imageDetails");
        String latString = id.latiData;
        String longiString = id.longiData;
        String tagText = id.tagText;
        DecimalFormat df2 = new DecimalFormat("###.##");
        String distance = Double.valueOf(df2.format(Double.parseDouble(id.distanceString))) + " metres away";

        GoogleMap mMap;
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(latString), Double.parseDouble(longiString)))
                .snippet(distance + " metres away").title(tagText));

        LatLng latLng = new LatLng(Double.parseDouble(latString), Double.parseDouble(longiString));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
    }
}
