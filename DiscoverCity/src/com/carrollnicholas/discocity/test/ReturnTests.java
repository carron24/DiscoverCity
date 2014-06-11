package com.carrollnicholas.discocity.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrollnicholas.discocity.ImageReturn;
import com.carrollnicholas.discocity.R;
import com.carrollnicholas.discocity.ResultActivity;
import com.carrollnicholas.discocity.TagActivity;

import java.util.ArrayList;

/**
 * Created by Nicholas on 07/06/2014.
 */
public class ReturnTests extends ActivityInstrumentationTestCase2<ImageReturn> {

    private Activity activity;
    private Context context;
    private String longitude;
    private String latitude;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;

    private ArrayList<Button> buttons;
    private ArrayList<ImageView> images;

    public ReturnTests(){
        super(ImageReturn.class);

    }

    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        Intent image = new Intent();
        longitude = "-6.137465";
        latitude = "53.3954647";
        image.putExtra("longitude", longitude);
        image.putExtra("latitude", latitude);

        setActivityIntent(image);
        activity = getActivity();
        context = activity;

        int[] button_array = {R.id.button,R.id.button2,R.id.button3,R.id.button4,R.id.button5,
                R.id.button6, R.id.button7,R.id.button8, R.id.button9, R.id.button10};
        buttons = new ArrayList<Button>();

        for (int id : button_array) {
            Button button = (Button) activity.findViewById(id);
            buttons.add(button);
        }

        int[] image_array = {R.id.imageView, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5};

        images = new ArrayList<ImageView>();

        for (int id : image_array) {
            ImageView image1 = (ImageView) activity.findViewById(id);
            images.add(image1);
        }

    }

    public void testButtonArray() throws Exception {
        assertNotNull(buttons);
        assertTrue(buttons.size() == 10);
    }

    public void testButtonsExist() throws Exception {
        assertNotNull(buttons);
        for(Button b : buttons){
            assertNotNull(b);
        }
    }

    public void testImageArray() throws Exception {
        assertNotNull(images);
        assertTrue(images.size() == 5);
    }

    public void testImagesExist() throws Exception {
        assertNotNull(images);
        for(ImageView i : images){
            assertNotNull(i);
        }
    }


    public void testSecondActivity() throws Exception {
        Instrumentation.ActivityMonitor monitor =
                getInstrumentation().
                        addMonitor(ResultActivity.class.getName(), null, false);

        Button view = (Button) activity.findViewById(R.id.button);

        TouchUtils.clickView(this, view);

        ResultActivity startedActivity = (ResultActivity) monitor
                .waitForActivityWithTimeout(2000);

        TextView textView = (TextView) startedActivity.findViewById(R.id.textView);

        ViewAsserts.assertOnScreen(startedActivity.getWindow().getDecorView(),
                textView);
        assertEquals("Text incorrect", "#sample", textView.getText().toString());

    }

}
