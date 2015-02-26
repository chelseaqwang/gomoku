package com.clearwater.gomoku;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class BluetoothPair extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_pair);

        Button button_host = (Button)findViewById(R.id.button_host);
        button_host.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        int size = 0;
                        RadioGroup sizeGroup = (RadioGroup)findViewById(R.id.size);
                        int sizeSelected = sizeGroup.getCheckedRadioButtonId();
                        RadioButton sizeRB = (RadioButton)findViewById(sizeSelected);
                        switch(sizeRB.getText().toString()) {
                            case "10*10":    size = 10; break;
                            case "15*15":    size = 15; break;
                            case "20*20":    size = 20; break;
                        }

                        Intent mainIntent = new Intent(BluetoothPair.this, BT_host.class);
                        mainIntent.putExtra("SIZE", size);
                        BluetoothPair.this.startActivity(mainIntent);
                    }
                }
        );

        Button button_guest = (Button)findViewById(R.id.button_join);
        button_guest.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(BluetoothPair.this, BT_guest.class);
                        BluetoothPair.this.startActivity(mainIntent);
                    }
                }
        );

    }

}

