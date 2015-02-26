package com.clearwater.gomoku;


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
import android.os.Handler;

import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class BT_host extends PlayIF {
    private static final String TAG = "BT_Gomoku";
    private static final boolean D = true;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    protected Context context;
    private String connectedDeviceName;
    private BT_service mBTService;
    private BluetoothAdapter mBluetoothAdapter;


    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        setPlayer();

        //1. Right after enter the main activity of BT_guest, check whether BT is available
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported, in which case we will quit
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }



    @Override
    public void onStart() {
        super.onStart();

        //2. Check whether BT is enabled

        // If BT is not on, request that it be enabled.
        // setupBTService() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mBTService == null) {
            setupBTService();
        }
        Intent intent = getIntent();
        size = intent.getIntExtra("SIZE", 10);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView)findViewById(R.id.winText);
        textView.setText("Waiting for guest player.");

    }

    @Override
    public void onResume() {
        System.out.println("BluetoothChatFragment::onResume is invoked.\n");

        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBTService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBTService.getState() == mBTService.STATE_NONE) {
                // Start the Bluetooth chat services
                mBTService.start();
            }
        }

        System.out.println("BluetoothChatFragment::onResume is invoked.\n");

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*
            case REQUEST_CONNECT_DEVICE_SECURE:
                System.out.println("BT_guest::onActivityResult() REQUEST_CONNECT_DEVICE_SECURE: ");
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("RESULT_OK\n");
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            */
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupBTService();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    this.finish();
                }
        }
    }
    
    protected final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BT_service.STATE_CONNECTED:
                            //TODO: Tell the game that the connection was established
                            break;
                        case BT_service.STATE_CONNECTING:
                            //TODO: Tell the game that it's connecting
                            break;
                        case BT_service.STATE_LISTEN:
                        case BT_service.STATE_NONE:
                            //TODO: Tell the game that it's not connected
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //TODO: Handle the message (i.e. wait for the ACK)
                    break;
                case MESSAGE_READ:
                    byte readChar = (byte) msg.obj;

                    //Toast.makeText(context, new String(readBuf), Toast.LENGTH_LONG).show(); //this is test code. Remove when not needed

                    acceptCharacter(readChar);

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    connectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(context, "Connected to "
                            + connectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    protected void acceptCharacter(byte character) {
    }

        private void hostDrawBoard() {
        // 3. Initialize the board
        Intent intent = getIntent();
        size = intent.getIntExtra("SIZE", 10);
        move = new char[size][size];

        drawBoard = new DrawBoard(this, size);
       
        addContentView(drawBoard,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                   ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void setupBTService() {

        System.out.println("BT_host::setupBTService() is invoked.");
        
        context = getApplicationContext();
        mBTService = new BT_service(context, mHandler);
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
                        BT_host.this.finish();
                        Intent intent = new Intent(BT_host.this, BT_host.class);
                        intent.putExtra("SIZE", size);
                        startActivity(intent);
                    }
                }
        );
    }
}