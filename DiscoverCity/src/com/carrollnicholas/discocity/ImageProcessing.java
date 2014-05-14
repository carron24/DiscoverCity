/**
 * Created by Nicholas on 14/05/2014.
 */
package com.carrollnicholas.discocity;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import android.os.Environment;
import android.util.Log;

import org.opencv.core.Mat;
import static org.opencv.highgui.Highgui.imread;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class ImageProcessing {
    public static void main(String[] args) {
        System.out.println("Hello, OpenCV");
    }
}