package com.example.accelerometer;

import java.util.ArrayList;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

public class AccelerExample extends ActionBarActivity implements SensorEventListener{
	private SensorManager sensorManager;
	private String DEVICE_ADDRESS = "";
	private String DEVICE_ADDRESS_TEMP = "";
	private BluetoothDiscovery BTD;

	TextView xCoor; // declare X axis object
	TextView yCoor; // declare Y axis object
	TextView zCoor; // declare Z axis object
	private Spinner  FinishSpinner;
		
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
			Setting_Dialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    @Override
    protected void onStart(){
    	super.onStart();

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
    
    public void ArdConnect_setting(String DEVICE_ADDRESS_TEMP )
    {
    	//int flag = DEVICE_ADDRESS.compareTo(DEVICE_ADDRESS_TEMP);
    	//if (flag != 0)   //when two address different, reconect
    	//{
    		Amarino.disconnect(this, DEVICE_ADDRESS);
    		DEVICE_ADDRESS = DEVICE_ADDRESS_TEMP;
    		Amarino.connect(this, DEVICE_ADDRESS);
    	//}	
    }
    
    private void Setting_Dialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Setting");
		
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(this);
		FinishSpinner = new Spinner(this);
				
	    ArrayList<String> spinnerArray = new ArrayList<String>();
	    spinnerArray.add(DEVICE_ADDRESS);
		BTD = new BluetoothDiscovery(this, spinnerArray);
	    spinnerArray = BTD.getBTAddress();
	    
	    FinishSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
	    {
	    	public void onItemSelected(AdapterView adapterView, View view, int position, long id)
	    	{
	    		DEVICE_ADDRESS_TEMP = adapterView.getSelectedItem().toString();
	    	}
	    	
	    	public void onNothingSelected(AdapterView arg0){}; 
	    });
	    	    		
	    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
	    FinishSpinner.setAdapter(spinnerArrayAdapter);
	    
	    linear.addView(FinishSpinner);
	    builder.setView(linear); 
				
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
	        public void onClick(DialogInterface dialog, int id) 
	        {
	    		ArdConnect_setting(DEVICE_ADDRESS_TEMP);	
	        }

	    });
		
	    builder.setNegativeButton("Clean", new DialogInterface.OnClickListener() 
	    {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

	    });
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}
}
