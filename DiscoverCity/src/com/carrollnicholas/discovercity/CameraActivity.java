package com.carrollnicholas.discovercity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends ActionBarActivity {
	static Context context;
	Camera cam;
	CameraPreview mPreview;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final String TAG = "CameraActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
		
		context = getApplicationContext();

		
		mPreview = new CameraPreview(this, cam);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		
		final Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						cam.takePicture(null, null,mPicture);
                        Intent myIntent = new Intent(CameraActivity.this, TagActivity.class);
                        CameraActivity.this.startActivity(myIntent);
					}
				});
	}

	
	private boolean checkCameraHardware(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}
	
	public static Camera getCameraInstance(){
		Camera c = null;
		try{
			c = Camera.open();
		}
		catch(Exception e){
			Log.v(TAG, "Cannot open Camera");
		}
		return c;
	}
	
	/** Create a file Uri for saving an image or video */
	@SuppressWarnings("unused")
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type){
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "DiscoverCity");
		
		if(!mediaStorageDir.exists()){
			if(!mediaStorageDir.mkdirs()){
				Log.d(TAG, "Failed to create dir");
				return null;
			}
		}
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		
		if(type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "DisCity_" + timeStamp + ".jpg");
		}
		else{
			return null;
		}
		
		return mediaFile;
		
	}
	
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			
			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			if(pictureFile == null){
				Log.d(TAG, "Error creating media file, check permissions");
				return;
			}
			
			try{
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				saveAndRotateImage(pictureFile);
				fos.close();

			} catch(FileNotFoundException e){
				Log.d(TAG, "File not found");
			} catch(IOException e){
				Log.d(TAG, "Error accessing file");
			}
			
		}
		
		
	};


    private void saveAndRotateImage(File pictureFile){
        try{
            String photopath = pictureFile.toString();
            Bitmap bmp = BitmapFactory.decodeFile(photopath);

            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            FileOutputStream fos2 = new FileOutputStream(pictureFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fos2);
            fos2.close();
        }
        catch(IOException e){
            Log.d(TAG, "Cannot save image " + e.getMessage());
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }
    //@Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            if(checkCameraHardware(context)){
                cam = getCameraInstance();
            }
            //mCamera.setPreviewCallback(null);
            mPreview = new CameraPreview(this, cam);//set preview
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }
        catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    private void releaseCamera(){
        if (cam != null){
            cam.release();        // release the camera for other applications
            mPreview.getHolder().removeCallback(mPreview);
            cam = null;
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

}
