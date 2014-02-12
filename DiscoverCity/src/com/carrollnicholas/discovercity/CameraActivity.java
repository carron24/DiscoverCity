package com.carrollnicholas.discovercity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraActivity extends Activity {
	Context context;
	Camera cam;
	CameraPreview mPreview;
	
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

}
