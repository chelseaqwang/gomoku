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

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter<String> mArrayAdapter;

    private ListView myListView;


    private static final String GOMOKU_BT_NAME = "BluetoothGomoku";
    private static final UUID GOMOKU_BT_UUID =
            UUID.fromString("69280706-491c-448d-a96d-dbf107a45f2e");

    private AcceptThread mHostAcceptThread;
    private ConnectedThread mConnectedThread;
    private int size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent intent = getIntent();
        size = intent.getIntExtra("SIZE", 10);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView)findViewById(R.id.winText);

        // 1. initialize BlueTooth
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

        // 2. waiting for player to join
        mHostAcceptThread = new AcceptThread();
        mHostAcceptThread.start();



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
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // Start the thread to manage the connection and perform transmissions
      //  mConnectedThread = new ConnectedThread(socket);
      //  mConnectedThread.start();
        
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
    
    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(GOMOKU_BT_NAME, GOMOKU_BT_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            TextView textView = (TextView)findViewById(R.id.winText);
            textView.setText("AcceptThread::run is invoked.");

            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                textView.setText("AcceptThread::run is waiting.");

                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    System.out.println("acceptThread::run accept() succeed.");
                    //                  textView.setText("AcceptThread::guest just joined.");

                    // Do work to manage the connection (in a separate thread)
//                    manageConnectedSocket(socket);

                    setContentView(R.layout.activity_board);
                    System.out.println("xxxxxxxxx.")
                    Context context = getApplicationContext();
                    drawBoard = new DrawBoard(context, size);
                    addContentView(drawBoard,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    System.out.println("acceptThread::run hostDrawBoard() finished.");

                    //connected(socket,socket.getRemoteDevice());
                    try {
                        mmServerSocket.close();
                        System.out.println("acceptThread::mmServerSocket.close();");
                    } catch (IOException e) {}
                    
                    break;
                }
            }
 //           textView.setText("AcceptThread::run is done.");
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            hostDrawBoard();
            
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity

                } catch (IOException e) {
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) { }
        }

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
                        BT_host.this.finish();
                        Intent intent = new Intent(BT_host.this, BT_host.class);
                        intent.putExtra("SIZE", size);
                        startActivity(intent);
                    }
                }
        );
    }
}