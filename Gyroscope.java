package com.example.xuezhenglab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RadioButton;

public class Gyroscope extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private MyBluetoothService bluetoothService;
    byte[] send;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        bluetoothService = Bluetooth.mService;
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public final void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float[] values = event.values;
            float x = values[2];

            RadioButton button1 = (RadioButton) findViewById(R.id.radioButton);
            RadioButton button2 = (RadioButton) findViewById(R.id.radioButton2);
            RadioButton button3 = (RadioButton) findViewById(R.id.radioButton3);
            RadioButton button4 = (RadioButton) findViewById(R.id.radioButton4);
            RadioButton button5 = (RadioButton) findViewById(R.id.radioButton5);
            RadioButton button6 = (RadioButton) findViewById(R.id.radioButton6);
            RadioButton button7 = (RadioButton) findViewById(R.id.radioButton7);
            send = new byte[]{0, 0, 0, 1, 0, 0, 0};
            button4.setChecked(true);

            if (x*x < 2.25) {
                return;
            } else {
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(false);
                button5.setChecked(false);
                button6.setChecked(false);
                button7.setChecked(false);
                send = new byte[]{0, 0, 0, 1, 0, 0, 0};

            }
            if (x > 1.5) {
                button3.setChecked(true);
                send[2] = 0b1;
            }
            if (x > 3) {
                button2.setChecked(true);
                send[1] = 0b1;
            }
            if (x > 5) {
                button1.setChecked(true);
                send[0] = 0b1;
            }
            if (x < -1.5) {
                button5.setChecked(true);
                send[4] = 0b1;
            }
            if (x < -3) {
                button6.setChecked(true);
                send[5] = 0b1;
            }
            if (x < -5) {
                button7.setChecked(true);
                send[6] = 0b1;
            }

            bluetoothService.write(send);


        } else {
            return;
        }
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