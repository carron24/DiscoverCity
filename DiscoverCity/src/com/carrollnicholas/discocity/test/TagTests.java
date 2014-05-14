package com.carrollnicholas.discocity.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.carrollnicholas.discocity.R;
import com.carrollnicholas.discocity.TagActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nicholas on 12/05/2014.
 */
public class TagTests extends ActivityInstrumentationTestCase2<TagActivity> {

    private TagActivity activity;
    private Button mButton;
    private TextView t1;
    private TextView t2;
    private EditText e1;


    public TagTests(){
        super(TagActivity.class);

    }

    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        String imageString = getTestImage(1);
        Intent image = new Intent();
        image.putExtra("picturePreview", imageString);
        setActivityIntent(image);
        activity = getActivity();
        mButton = (Button)activity.findViewById(R.id.button);
        t1 = (TextView)activity.findViewById(R.id.textView);
        t2 = (TextView)activity.findViewById(R.id.textView2);
        e1 = (EditText)activity.findViewById(R.id.editText);
    }


    public void testButtonExists() throws Exception{
        assertNotNull(mButton);
    }

    public void testTextViewExists() throws Exception{
        assertNotNull(t1);
        assertNotNull(t2);
    }

    public void testEditTextExists() throws Exception{
        assertNotNull(e1);
    }

    @UiThreadTest
    public void testEditCanChange() throws Exception{
        e1.setText("testing");
        assertEquals(e1.getText().toString(), "testing", e1.getText().toString());

    }

    public void testStorage() throws Exception{
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DiscoverCity");

        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                fail();
            }

        }
        assertTrue(mediaStorageDir.exists());
    }


    private static String getTestImage(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DiscoverCity");

        File mediaFile;

        if(type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "DisCity.jpg");
        }
        else{
            return null;
        }

        return mediaFile.toString();

    }




}
