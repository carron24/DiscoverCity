package com.carrollnicholas.discocity.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.carrollnicholas.discocity.CameraActivity;
import com.carrollnicholas.discocity.CameraPreview;
import com.carrollnicholas.discocity.R;
import com.carrollnicholas.discocity.TagActivity;

/**
 * Created by Nicholas on 10/05/2014.
 */
public class CameraTests extends ActivityInstrumentationTestCase2<CameraActivity> {
    private CameraActivity activity;

    public CameraTests(){
        super(CameraActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        activity = getActivity();
    }

    public void testButtonExists() throws Exception{
        Button mButton = (Button) activity.findViewById(R.id.button);
        assertTrue(mButton != null);
    }

    public void testSecondActivity() throws Exception {
        Instrumentation.ActivityMonitor monitor =
                getInstrumentation().
                        addMonitor(TagActivity.class.getName(), null, false);

        Button view = (Button) activity.findViewById(R.id.button);

        TouchUtils.clickView(this, view);

        TagActivity startedActivity = (TagActivity) monitor
                .waitForActivityWithTimeout(2000);

        TextView textView = (TextView) startedActivity.findViewById(R.id.textView);

        ViewAsserts.assertOnScreen(startedActivity.getWindow().getDecorView(),
                textView);
        assertEquals("Text incorrect", "Tag Your Picture!", textView.getText().toString());

        this.sendKeys(KeyEvent.KEYCODE_BACK);

        TouchUtils.clickView(this, view);
    }
}