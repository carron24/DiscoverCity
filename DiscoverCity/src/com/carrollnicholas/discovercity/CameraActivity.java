package com.carrollnicholas.discovercity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameraActivity extends Activity {
	static Context context;
	Camera cam;
	CameraPreview mPreview;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final String TAG = "CameraActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		
		context = getApplicationContext();
		
		if(checkCameraHardware(context)){
			cam = getCameraInstance();
		}
		else{
			Toast.makeText(context, "NOOOOOOOOOO", Toast.LENGTH_SHORT).show();
		}
		
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
					}
				});
	}

	
	private boolean checkCameraHardware(Context context){
		if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			return true;
		}
		else {
			return false;
		}
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
				String photopath = pictureFile.toString();
			    Bitmap bmp = BitmapFactory.decodeFile(photopath);

			    Matrix matrix = new Matrix();
			    matrix.postRotate(90);
			    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			    pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

				FileOutputStream fos2 = new FileOutputStream(pictureFile);
		        bmp.compress(Bitmap.CompressFormat.JPEG, 85, fos2);
			    
				fos.close();
				fos2.close();
			} catch(FileNotFoundException e){
				Log.d(TAG, "File not found");
			} catch(IOException e){
				Log.d(TAG, "Error accessing file");
			}
			
		}
		
		
	};

	
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (cam != null){
            cam.release();        // release the camera for other applications
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
