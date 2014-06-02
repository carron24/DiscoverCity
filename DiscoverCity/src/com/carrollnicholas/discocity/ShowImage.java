package com.carrollnicholas.discocity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class ShowImage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        Bundle data = getIntent().getExtras();
        final ImageDetails id = (ImageDetails) data.getParcelable("imageDetails");
        String s = "http://nicholascarroll.info:3000" + id.getImageLink();

        ImageView imageview = (ImageView)findViewById(R.id.fullScreenImage);
        new DownloadImageTask(imageview)
                .execute(s);
    }

}
