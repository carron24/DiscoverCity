package com.carrollnicholas.discocity.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.carrollnicholas.discocity.ImageReturn;
import com.carrollnicholas.discocity.R;
import com.carrollnicholas.discocity.TagActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nicholas on 12/05/2014.
 */
public class TagTests extends ActivityInstrumentationTestCase2<TagActivity> {

    private TagActivity activity;
    private Context context;
    private Button mButton;
    private TextView t1;
    private TextView t2;
    private EditText e1;
    private int sharpness;
    private int brightness;
    private int contrast;

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
        context = activity;
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

    public void openOpenCV(){
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_5, context, mOpenCVCallBack))
        {
            Log.e("image", "Cannot connect to OpenCV Manager");
        }
        else{
            Log.e("image", "Connected to OpenCV Manager");
        }

    }

    public void testBrightness() throws Exception{
        openOpenCV();

        assertNotNull(brightness);
        assertTrue(brightness >=0);
    }

    public void testSharpness() throws Exception{
        openOpenCV();

        assertNotNull(sharpness);
        assertTrue(sharpness >=0);
    }

    public void testContrast() throws Exception{
        openOpenCV();

        assertNotNull(contrast);
        assertTrue(contrast >=0);
    }


    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(context) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Mat original_image = Highgui.imread(getTestImage(1));

                    brightness = getBrightness(original_image)/1000000;
                    sharpness = getSharpness(original_image)/100000;
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    private int getBrightness(Mat image){

        Mat converted_image = image.clone();
        List<Mat> ycrcb = new ArrayList<Mat>();
        Imgproc.cvtColor(image, converted_image, Imgproc.COLOR_BGR2YCrCb);
        Core.split(converted_image, ycrcb);
        Scalar m = Core.sumElems(ycrcb.get(0));

        contrast = getContrast(ycrcb.get(0));
        double[] btns = m.val;
        return (int)btns[0];

    }
    private int getSharpness(Mat image){

        Mat dy = new Mat();
        Mat dx = new Mat();

        Imgproc.Sobel(image, dx, CvType.CV_32F, 1, 0);
        Imgproc.Sobel(image, dy,CvType.CV_32F, 0, 1);

        Core.magnitude(dx,dy,dx);
        Scalar n = Core.sumElems(dx);

        double spns[] = n.val;
        return (int)spns[0];
    }

    private int getContrast(Mat tmp){
        Mat grey = tmp.clone();

        Imgproc.cvtColor(tmp, grey, Imgproc.COLOR_YUV2GRAY_420);
        Core.MinMaxLocResult minMax = Core.minMaxLoc(grey);
        Log.v("whatthecraic: ", minMax.maxVal + "");
        Log.v("whatthecraic: ", minMax.minVal + "");
        Scalar v = Core.mean(grey);

        MatOfDouble mean = new MatOfDouble();
        MatOfDouble std = new MatOfDouble();

        Core.meanStdDev(tmp, mean, std);

        double[] sdev = std.toArray();

        return (int)sdev[0];
    }
}
