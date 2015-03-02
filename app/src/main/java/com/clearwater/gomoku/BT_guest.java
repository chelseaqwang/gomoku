package com.clearwater.gomoku;

/**
 * Created by chenbo on 2/24/15.
 */

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
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
    public static final int MESSAGE_GAME_START = 6;

    protected Context context;
    private String connectedDeviceName;
    private BT_service mBTService;
    private BluetoothAdapter mBluetoothAdapter;
  
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Game status
    private static final int GAME_ST_NORMAL = 0;
    private static final int GAME_ST_WIN = 1;
    private static final int GAME_ST_TIE = 2;
    
    //Indicate whether game start
    private boolean is_game_start = false;
    private boolean is_in_turn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView)findViewById(R.id.winText);
        textView.setVisibility(View.INVISIBLE);
        
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
        
        // 3. Set up the button for guest to scan and connect to host, which will be handled
        //    by a new activity "BT_DeviceListActivity". This activity will return the host
        //    device MAC address
        // Make the button of "connect to host" be visible and if it is clicked
        Button button_guest_scan = (Button)findViewById(R.id.guest_scan);
        button_guest_scan.setVisibility(View.VISIBLE);
        button_guest_scan.setOnClickListener (
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent serverIntent = new Intent(BT_guest.this, BT_DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                    }
                }
        );
    }

    public void onDestroy() {

        super.onDestroy();
        if (mBTService != null) {
            mBTService.stop();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
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

    private void setupBTService() {

        context = getApplicationContext();
        mBTService = new BT_service(context, mHandler);
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
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    handle_message_read(readMessage);
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

    //
    protected void handle_message_read(String str) {
        System.out.println("BT_guest::handle_message_read()");

        String msg_type = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
        System.out.println("BT_guest::handle_message_read(): msg_type = " + msg_type);

        // 1. message to start game
        if(msg_type.equals("game_start") ) {
            String str_size = str.substring(str.indexOf(" ") + 1);
            size = Integer.parseInt (str_size);
            startGameGuest();
        }
        /*
        else if(msg_type.equals("switch_turn") ) {
            // a. message of switch_turn,  "msg fmt: [switch_turn]"
            is_in_turn = true;

            TextView textView = (TextView)findViewById(R.id.winText);
            textView.setText("Your turn!");
            updateChronometer();
        }
        */
        else if(msg_type.equals("remote_piece") ) {
            // b. message of remote_piece,  msg fmt: "[remote_piece] (x,y)"
            // 1. get the value of x y
            String str_i = str.substring(str.indexOf("(") + 1, str.indexOf(","));
            String str_j = str.substring(str.indexOf(",") + 1, str.indexOf(")"));
            int i =  Integer.parseInt( str_i );
            int j =  Integer.parseInt( str_j );

            // 1. add piece
            int width = getResources().getDisplayMetrics().widthPixels;
            float grid = width/size;
            move[i][j] = player2.getFlag();
            float radius = (float) (grid*0.9/2);
            addPiece(i * grid + grid / 2, j * grid + grid / 2, radius, player2.color);

            // 2. checkwin&&checktie and display
            if (checkWin(i, j, player2.color)) {
                updateTextView(player2, GAME_ST_WIN);
                player2.recordWin();
                gameRoundEnd();
            } else if (checkTie()) {
                updateTextView(player2, GAME_ST_TIE);
                gameRoundEnd();
            } else {
                updateTextView(player1, GAME_ST_NORMAL);
            }

            // 3. switch turn
            is_in_turn = true;

            TextView textView = (TextView)findViewById(R.id.winText);
            textView.setText("Your turn!");
            updateChronometer();
        }
    }

    private void startGameGuest () {
        //1. set scan button as invisible
        TextView textView = (TextView)findViewById(R.id.guest_scan);
        textView.setVisibility(View.INVISIBLE);

        //2. set move and draw board
        move = new char[size][size];
        drawBoard = new DrawBoard(this, size);
        addContentView(drawBoard,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // 3. set up the setOnTouchListener
        drawBoard.setOnTouchListener(
                new DrawBoard.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent m) {
                        if(is_game_start && is_in_turn) {
                            play(m);
                        }
                        return true;
                    }
                }
        );
        //3. set is_game_start = true and set is_in_turn = false, as host always hold first piece
        is_game_start = true;
        is_in_turn = false;
        setPlayer();
        
        //4. display game status
        updateChronometer();
        updateTextView(player2, GAME_ST_NORMAL);
    }


    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link BT_DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(BT_DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        TextView textView = (TextView)findViewById(R.id.winText);
        textView.setText("Host MAC: " + address);
        textView.setVisibility(View.VISIBLE);
        
        mBTService.connect(device, secure);
    }
    
    public void setPlayer() {
        player1 = new Player("Guest", Color.WHITE);
        player2 = new Player("Host:" + connectedDeviceName, Color.BLACK);
        player1.setOpponent(player2);
        player2.setOpponent(player1);
    } // to be implemented

    public void play(MotionEvent m) {
        System.out.println("BT_guest::play() is invoked");

        //1. get the (x,y)
        float x = m.getX();
        float y = m.getY();
        int width = getResources().getDisplayMetrics().widthPixels;
        float grid = width/size;
        float radius;
        int i, j;

        i = Math.round((x-grid/2)/grid);
        j = Math.round((y-grid/2)/grid);

        if(!checkIndex(i, j)) return;
        if (move[i][j] != '\0') return;

        //2. put the piece on host
        move[i][j] = player1.getFlag();
        radius = (float) (grid*0.9/2);
        addPiece(i * grid + grid / 2, j * grid + grid / 2, radius, player1.color);

        //3. send message of (i,j) to guest to sync the board
        //      msg fmt: [remote_piece] (x,y)
        String location_msg = "[remote_piece] " +
                "(" +  Integer.toString(i) + "," + Integer.toString(j) + ")";
        sendMessage(location_msg);
        System.out.println(location_msg);

        //4. give out turn and sending turn change message
        is_in_turn = false;
        //      msg fmt: [switch_turn]
        String switch_turn_msg = "[switch_turn]";
        sendMessage(switch_turn_msg);
        System.out.println(switch_turn_msg);

        //5. display information and checkwin&&checktie
        if (checkWin(i, j, player1.color)) {
            updateTextView(player1, GAME_ST_WIN);
            player1.recordWin();
            gameRoundEnd();
        } else if (checkTie()) {
            updateTextView(player1, GAME_ST_TIE);
            gameRoundEnd();
        } else {
            updateTextView(player2, GAME_ST_NORMAL);
        }
        
        
    }

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

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBTService.getState() != mBTService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBTService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            //     mOutStringBuffer.setLength(0);
            //       mOutEditText.setText(mOutStringBuffer);
        }
    }

    protected void updateChronometer() {
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    protected void updateTextView(Player player, int gm_st) {
        TextView textView = (TextView)findViewById(R.id.winText);

        switch (gm_st) {
            case GAME_ST_NORMAL:
                textView.setText("Waiting for " + player.name + " ...");
                break;
            case GAME_ST_WIN:
                textView.setText("Winner is " + player.name + "!");
                break;
            case GAME_ST_TIE:
                textView.setText("Game is tied !");
                break;
        }
    }

    /*
    * Cleanup the layout and also reset the related variables to reset the next round of game
    * * * */
    protected void gameRoundEnd () {
        addResetButton();
    }

}


