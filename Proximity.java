package com.example.xuezhenglab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RadioButton;


public class Proximity extends AppCompatActivity  implements SensorEventListener{
    private SensorManager sensorManager;
    private MyBluetoothService bluetoothService;
    private StringBuffer mOutStringBuffer;
    byte[] send;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        bluetoothService = Bluetooth.mService;
        mOutStringBuffer = new StringBuffer("");


    }
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float[] values = event.values;
            float x = values[0];


            RadioButton button1 = (RadioButton) findViewById(R.id.radioButton);
            RadioButton button2 = (RadioButton) findViewById(R.id.radioButton2);
            RadioButton button3 = (RadioButton) findViewById(R.id.radioButton3);
            RadioButton button4 = (RadioButton) findViewById(R.id.radioButton4);
            RadioButton button5 = (RadioButton) findViewById(R.id.radioButton5);
            RadioButton button6 = (RadioButton) findViewById(R.id.radioButton6);
            RadioButton button7 = (RadioButton) findViewById(R.id.radioButton7);

            if (x < 0.5) {

                send = new byte[]{1, 1, 1, 1, 1, 1, 1};

                button1.setChecked(true);
                button2.setChecked(true);
                button3.setChecked(true);
                button4.setChecked(true);
                button5.setChecked(true);
                button6.setChecked(true);
                button7.setChecked(true);


            } else {
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(false);
                button4.setChecked(false);
                button5.setChecked(false);
                button6.setChecked(false);
                button7.setChecked(false);

                send = new byte[]{0, 0, 0, 0, 0, 0, 0};

            }

            bluetoothService.write(send);

        } else {
            return;
        }
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }









}