package com.example.microscope;

import java.io.File;

import com.example.mycameratest.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * This is the main activity to record a video, process it and then play it.
 * 
 * Updates:
 * 03/17: Currently it is a framework implementing recording and plying a video, and Microscope class is empty.
 * 		Bug1: It works correctly on Lenovo, 4.0.3, but it cannot quit the camera view after taking a video on 
 * 		Sumsung Galaxy Mini, 4.1.2.
 * 
 * @author David
 *
 */
public class MainActivity extends Activity {
	
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private static final int MEDIA_TYPE_VIDEO = 2;
	private static final String dir = "Microscope";
	private static final String defaultName = "last.avi";
	
	private Uri fileUri;
	private Button bVideo;
	private Button bProcess;
	private Button bPlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bVideo = (Button) findViewById(R.id.video);
		bVideo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the video file name
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			    startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);	
			}
			
		});
		
		bProcess = (Button) findViewById(R.id.process);
		bProcess.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String pathName = "file://"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
						+ "/"+dir+"/"+ defaultName;
				Uri myUri = Uri.parse(pathName);// initialize Uri here
				Microscope ms = new Microscope(myUri);
				ms.process();
			}
			
		});
		
		bPlay = (Button) findViewById(R.id.play);
		bPlay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String pathName = "file://"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
						+ "/"+dir+"/"+ defaultName;
				Uri myUri = Uri.parse(pathName);// initialize Uri here
				VideoView videoView =(VideoView)findViewById(R.id.videoView);
			    MediaController mediaController= new MediaController(MainActivity.this);
			    mediaController.setAnchorView(videoView);               
			    videoView.setMediaController(mediaController);
			    videoView.setVideoURI(myUri);        
			    videoView.requestFocus();
			    videoView.start();				
			}
			
		});
				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Video captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Video has been saved!", Toast.LENGTH_SHORT).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }
	}
	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}
	
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), dir);
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("Microscope", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    File mediaFile;
	    if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + defaultName);
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
