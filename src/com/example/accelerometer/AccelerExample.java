package com.example.accelerometer;

import android.support.v7.app.ActionBarActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

public class AccelerExample extends ActionBarActivity implements SensorEventListener{
	private SensorManager sensorManager;
	private String DEVICE_ADDRESS = "00:14:03:11:35:45";

		TextView xCoor; // declare X axis object
		TextView yCoor; // declare Y axis object
		TextView zCoor; // declare Z axis object
		
		@Override
	public void onCreate(Bundle savedInstanceState)
	{
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
			
			xCoor=(TextView)findViewById(R.id.xcoor); // create X axis object
			yCoor=(TextView)findViewById(R.id.ycoor); // create Y axis object
			zCoor=(TextView)findViewById(R.id.zcoor); // create Z axis object
			
			sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
			// add listener. The listener will be HelloAndroid (this) class
			sensorManager.registerListener(this, 
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
			
			/*	More sensor speeds (taken from api docs)
			    SENSOR_DELAY_FASTEST get sensor data as fast as possible
			    SENSOR_DELAY_GAME	rate suitable for games
			 	SENSOR_DELAY_NORMAL	rate (default) suitable for screen orientation changes
			*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.acceler_example, menu);
		return true;
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
	
    @Override
    protected void onStart(){
    	super.onStart();
    	
    	Amarino.connect(this, DEVICE_ADDRESS);

    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Amarino.disconnect(this, DEVICE_ADDRESS);
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// check sensor type
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
		{			
		// assign directions
		float x=event.values[0];
		float y=event.values[1];
		float z=event.values[2];
			
		xCoor.setText("X: "+x);
		yCoor.setText("Y: "+y);
		zCoor.setText("Z: "+z);
		
		SendArgtoArduino_z((int)z*10);
		}
	}
	
    private void SendArgtoArduino_z(int z)
    {

    	Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'A', z);
    }
}
