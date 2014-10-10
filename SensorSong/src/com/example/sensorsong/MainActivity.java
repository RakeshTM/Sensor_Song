package com.example.sensorsong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener{

	private long now;
	private float x;
	private float y;
	private float z;
	private long lastUpdate;
	private long lastShake;
	private float lastX;
	private float lastY;
	private float lastZ;
	private long timeDiff;
	private float force;
	SensorManager sensorManager;
	 Sensor accelerometer;
	private SongsManager songsManager;
	TextView tvResult;
	MediaPlayer mediaPlayer;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String,String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvResult = (TextView) findViewById(R.id.tvResult);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		songsManager = new SongsManager();
		songsList  = songsManager.getPlayList();
		 mediaPlayer = new MediaPlayer();
	}
	@Override
	protected void onResume() {
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		now = event.timestamp;

		x = event.values[0];
		y = event.values[1];
		z = event.values[2];

		// if not interesting in shake events
		// just remove the whole if then else block
		if (lastUpdate == 0) {
			lastUpdate = now;
			lastShake = now;
			lastX = x;
			lastY = y;
			lastZ = z;
			Toast.makeText(this, "No Motion detected", Toast.LENGTH_SHORT)
					.show();

		} else {
			timeDiff = now - lastUpdate;

			if (timeDiff > 0) {

				/*
				 * force = Math.abs(x + y + z - lastX - lastY - lastZ) /
				 * timeDiff;
				 */
				force = Math.abs(x + y + z - lastX - lastY - lastZ);

				float threshold = 2.5f;
				if (Float.compare(force, threshold) > 0) {
					

					long interval = 10000;
					if (now - lastShake >= interval) {

						// trigger shake event
						//Toast.makeText(this, "shake detected", 1000).show();

						changeTheSong();
					} else {
						//Toast.makeText(this, "No shake detected", 1000).show();

					}
					lastShake = now;
				}
				lastX = x;
				lastY = y;
				lastZ = z;
				lastUpdate = now;
			} else {
				Toast.makeText(this, "No Motion detected", Toast.LENGTH_SHORT)
						.show();

			}
		}
		
	}

	public void changeTheSong() {
		Random rand = new Random();
		int play = rand.nextInt(songsList.size());
		tvResult.setText(songsList.get(play).get("songName"));
		playSong(play);
		
	}
	public void playSong(int play) {
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(songsList.get(play).get("seongPath"));
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onPause() {
		mediaPlayer.release();
		sensorManager.unregisterListener(this, accelerometer);
		super.onPause();
	}
	@Override
	protected void onStop() {
		mediaPlayer.release();
		sensorManager.unregisterListener(this, accelerometer);
		super.onStop();
	}
	protected void onRestart() 
	{
		// TODO Auto-generated method stub
		super.onRestart();
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	
}
