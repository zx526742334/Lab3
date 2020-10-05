package com.example.xuezhenglab2;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RadioButton;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private MyBluetoothService bluetoothService;
    float accelerationSquareRoot;
    byte[] send;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        bluetoothService = Bluetooth.mService;

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        float[] values =  event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        accelerationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        RadioButton button1 = (RadioButton) findViewById(R.id.radioButton);
        RadioButton button2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton button3 = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton button4 = (RadioButton) findViewById(R.id.radioButton4);
        RadioButton button5 = (RadioButton) findViewById(R.id.radioButton5);
        RadioButton button6 = (RadioButton) findViewById(R.id.radioButton6);
        RadioButton button7 = (RadioButton) findViewById(R.id.radioButton7);

        if (accelerationSquareRoot < 1.4) {
            return;
        }
        if (accelerationSquareRoot >= 1.4 ) {
            button1.setChecked(true);
            button2.setChecked(false);
            button3.setChecked(false);
            button4.setChecked(false);
            button5.setChecked(false);
            button6.setChecked(false);
            button7.setChecked(false);
            send = new byte[]{1, 0, 0, 0, 0, 0, 0};
        }
        if (accelerationSquareRoot >= 1.75) {
            button2.setChecked(true);
            send[1] = 0b1;
        }
        if (accelerationSquareRoot >= 2.25) {
            button3.setChecked(true);
            send[2] = 0b1;
        }
        if (accelerationSquareRoot >= 2.5) {
            button4.setChecked(true);
            send[3] = 0b1;
        }
        if (accelerationSquareRoot >= 3.5) {
            button5.setChecked(true);
            send[4] = 0b1;
        }
        if (accelerationSquareRoot >= 3.75) {
            button6.setChecked(true);
            send[5] = 0b1;
        }
        if (accelerationSquareRoot >= 4) {
            button7.setChecked(true);
            send[6] = 0b1;
        }

        bluetoothService.write(send);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }




}