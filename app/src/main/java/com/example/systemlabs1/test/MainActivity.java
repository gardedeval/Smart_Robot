package com.example.systemlabs1.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import android.widget.*;
import android.view.*;
import android.view.View.OnClickListener;

import static com.example.systemlabs1.test.R.id.main;

public  class MainActivity extends Activity implements SensorEventListener {

    private TextView text;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private static long counter = 0L;
    private static final Integer SERVERPORT = 6789;
    public final String SERVERIP = "192.168.42.1";
    private static String angle = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //text = (TextView) findViewById(R.id.text);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do nothing
    }
    public void onSensorChanged(SensorEvent event) {
        angle = "";
        counter++;
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            int azimuthInDegress = (int)(Math.toDegrees(azimuthInRadians) + 360) % 360;
            Integer d = ((Integer) azimuthInDegress);
          //  text.setText(d.toString());
            angle = d.toString();
           // text.invalidate();
            if(counter > 10) {
                new DevalWorker().execute(angle);
                counter = 0;
            }

        }
    }
    public void clickFunc(View view){
        Toast.makeText(MainActivity.this, angle,Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(main, menu);
        Button button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Button Clicked",	Toast.LENGTH_SHORT).show();
                new DevalWorker().execute(angle);
            }

        });
        return true;
    }

    protected class DevalWorker extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... params) {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket packet = new DatagramPacket(params[0].getBytes(),params[0].length(),serverAddr, SERVERPORT);
                socket.send(packet);
                System.out.println("Helllo I am here");

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
           
        }
    }
}
