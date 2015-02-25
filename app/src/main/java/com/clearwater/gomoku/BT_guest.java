package com.clearwater.gomoku;

/**
 * Created by chenbo on 2/24/15.
 */

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.widget.ArrayAdapter;
import android.widget.ListView;


import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**This class represents the host of online mode, whose main workflow is as follows:
 * 1. initialize board based on the chosen size *
 * 2. *
 * *
 */
public class BT_guest extends PlayIF {
    public static String HOST_DEVICE_MAC = "host_device_address";

    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            HOST_DEVICE_MAC = address;

            TextView textView = (TextView)findViewById(R.id.winText);
            textView.setText("Going to connect with " + HOST_DEVICE_MAC);

            myListView = (ListView)findViewById(R.id.listView1);
            myListView.setVisibility(View.GONE);

            // Get the BluetoothDevice object
            BluetoothDevice host_device = mBluetoothAdapter.getRemoteDevice(address);

            mGuestConnectThread = new ConnectThread(host_device);
            mGuestConnectThread.start();
            
            // Create the result Intent and include the MAC address
 //           Intent intent = new Intent();
   //         intent.putExtra(HOST_DEVICE_MAC, address);

            // Set result and finish this Activity
 //           setResult(Activity.RESULT_OK, intent);
  //          finish();
        }
    };

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter<String> mArrayAdapter;

    private ListView myListView;

    private static final String GOMOKU_BT_NAME = "BluetoothGomoku";
    private static final UUID GOMOKU_BT_UUID =
            UUID.fromString("69280706-491c-448d-a96d-dbf107a45f2e");

    private ConnectThread mGuestConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView)findViewById(R.id.winText);

        // 1. Initialize BlueTooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            textView.setText("Device does not support Bluetooth!");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // 2. Let the user choose which device to connect from the paired list
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        // If there are paired devices
        if (pairedDevices.size() > 0) {
            textView.setText("Please select");
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            textView.setText("no paired devices");
        }

        myListView = (ListView)findViewById(R.id.listView1);
        myListView.setAdapter(mArrayAdapter);
        myListView.setOnItemClickListener(mDeviceClickListener);

       // Get the BluetoothDevice object
//        String address = HOST_DEVICE_MAC;
//        BluetoothDevice host_device = mBluetoothAdapter.getRemoteDevice(address);
        
//        mGuestConnectThread = new ConnectThread(host_device);
//        mGuestConnectThread.start();

        // 1. Initialize the board, size should be get from Host
//        Intent intent = getIntent();
//        size = intent.getIntExtra("SIZE", 10);
//        move = new char[size][size];

//        drawBoard = new DrawBoard(this, size);
        //   addContentView(drawBoard,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        //            ViewGroup.LayoutParams.MATCH_PARENT));

        setPlayer();

        // 3. wait player to join
        //    textView.setText(player1.name + " vs. " + player2.name);


        // 4. Main play loop



/*
        //bobo: xxx play loop
        drawBoard.setOnTouchListener(
                new DrawBoard.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent m) {
                        play(m);
                        return true;
                    }
                }
        );
        */
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            TextView textView = (TextView)findViewById(R.id.winText);

            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(GOMOKU_BT_UUID);
                
                if(tmp != null) {
                    textView.setText("ConnectThread:: host found.");
                } else {
                    textView.setText("ConnectThread:: host not found.");
                }
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            TextView textView = (TextView)findViewById(R.id.winText);

            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                textView.setText("ConnectThread:Run.");

                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(mmSocket);
 //           textView.setText("ConnectThread::run will return.");

        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public void setPlayer() {
        player1 = new Player("Peter", Color.BLACK);
        player2 = new Player("Jean", Color.WHITE);
        player1.setOpponent(player2);
        player2.setOpponent(player1);
    } // to be implemented

    public void play(MotionEvent m) {} // to be implemented

    public void addResetButton() {
        Button button_play_again = (Button)findViewById(R.id.button_play_again);
        button_play_again.setVisibility(View.VISIBLE);
        button_play_again.setOnClickListener (
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        BT_guest.this.finish();
                        Intent intent = new Intent(BT_guest.this, BT_guest.class);
                        intent.putExtra("SIZE", size);
                        startActivity(intent);
                    }
                }
        );
    }
}