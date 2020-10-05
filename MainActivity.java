package com.example.xuezhenglab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testAccelerometer(View view) {
        Intent intent = new Intent(this, Accelerometer.class);
        startActivity(intent);
    }

    public void testGyroscope(View view) {
        Intent intent = new Intent(this, Gyroscope.class);
        startActivity(intent);
    }

    public void testProximity(View view) {
        Intent intent = new Intent(this, Proximity.class);
        startActivity(intent);
    }

    public void bluetooth(View view) {
        Intent intent = new Intent(this, Bluetooth.class);
        startActivity(intent);
    }
}
