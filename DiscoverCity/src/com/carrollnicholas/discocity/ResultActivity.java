package com.carrollnicholas.discocity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.resultLayout);

        Bundle data = getIntent().getExtras();
        final ImageDetails id = (ImageDetails) data.getParcelable("imageDetails");
        TextView text = (TextView) findViewById(R.id.textView);
        TextView text2 = (TextView) findViewById(R.id.textView2);
        text.setText(id.getTagText());
        text2.setText(id.getDescText());
        TextView text3 = (TextView) findViewById(R.id.textView3);
        text3.setText(id.getLongiData() + " " + id.getLatiData());
        String s = "http://nicholascarroll.info:3000" + id.getImageLink();
        ImageView fullScreenImage = (ImageView)findViewById(R.id.full_screen);
        new DownloadImageTask(fullScreenImage)
                .execute(s);



        Button mButton = (Button)findViewById(R.id.map_button);

        Button imagebutton = (Button)findViewById(R.id.image_button);


        imagebutton.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {

                    Intent myIntent = new Intent(ResultActivity.this, ShowImage.class);
                    myIntent.putExtra("imageDetails", id);
                    ResultActivity.this.startActivity(myIntent);

                }
            });


        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Intent myIntent = new Intent(ResultActivity.this, MapActivity.class);
                        myIntent.putExtra("imageDetails", id);
                        ResultActivity.this.startActivity(myIntent);

                    }
                });

    }

}
