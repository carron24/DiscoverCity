package com.carrollnicholas.discocity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle data = getIntent().getExtras();
        ImageDetails id = (ImageDetails) data.getParcelable("imageDetails");
        TextView text = (TextView) findViewById(R.id.textView);
        text.setText(id.tagText);
        TextView text2 = (TextView) findViewById(R.id.textView2);
        text2.setText(id.descText);
        TextView text3 = (TextView) findViewById(R.id.textView3);
        text3.setText(id.longiData + " " + id.latiData);
        String s = "http://nicholascarroll.info:3000" + id.imageLink;
        new DownloadImageTask((ImageView)findViewById(R.id.imageView))
                .execute(s);
    }
}
