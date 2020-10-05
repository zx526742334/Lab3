package com.example.xuezhenglab2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity {
    private static final String TAG = "DeviceListActivity";
    public static MyBluetoothService mService = null;
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    ArrayAdapter<String> pairedDevicesArrayAdapter;
    Set<BluetoothDevice> pairedDevices;
    ListView pairedListView;
    ListView newDevicesListView;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    //public MyBluetoothService mService = null;
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_bluetooth);

        setResult(Activity.RESULT_CANCELED);



        pairedDevicesArrayAdapter =
                new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        pairedListView = (ListView) findViewById(R.id.paired_devices);



        // Find and set up the ListView for newly discovered devices
        newDevicesListView = (ListView) findViewById(R.id.new_devices);
        //mNewDevicesArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, newDevice);
        //newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        //newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver, filter);


        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(receiver, filter);





        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices


    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ArrayList newDevice = new ArrayList();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
               //if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                newDevice.add(device.getName() + "\n" + device.getAddress());
                Toast.makeText(getApplicationContext(), "Showing New Devices",Toast.LENGTH_SHORT).show();

                mNewDevicesArrayAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1, newDevice);
                newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
                newDevicesListView.setOnItemClickListener(mDeviceClickListener);
                //}
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        unregisterReceiver(receiver);
    }

    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        Toast.makeText(getApplicationContext(), "scanning" , Toast.LENGTH_SHORT).show();

        // Turn on sub-title for new devices
        //findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17);

            //BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
            //mService.connect(device);


            // Create the result Intent and include the MAC address
            //Intent intent = new Intent();
            //intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            Toast.makeText(getApplicationContext(), "Get Device Address",Toast.LENGTH_SHORT).show();
            // Set result and finish this Activity
            //setResult(Activity.RESULT_OK, intent);
            //finish();
        }
    };

    public void connectDevice(View v) {
        BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
        mService = new MyBluetoothService();
        mService.connect(device);
    }


    public void list(View v){
        pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            //findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            ArrayList paired = new ArrayList();
            for (BluetoothDevice device : pairedDevices) {
                paired.add(device.getName() + "\n" + device.getAddress());
            }
            Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();
            pairedDevicesArrayAdapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, paired);

            pairedListView.setAdapter(pairedDevicesArrayAdapter);
            pairedListView.setOnItemClickListener(mDeviceClickListener);
        } else {
            String noDevices = "No Paired Devices";
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }

    public void scanDevice(View v) {
        doDiscovery();


    }


}