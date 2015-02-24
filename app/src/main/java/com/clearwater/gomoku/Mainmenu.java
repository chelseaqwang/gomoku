package com.clearwater.gomoku;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Mainmenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(Mainmenu.this, Select.class);
                        Mainmenu.this.startActivity(mainIntent);
                    }
                }
        );

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(Mainmenu.this, Profile.class);
                        Mainmenu.this.startActivity(mainIntent);
                    }
                }
        );

        Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(Mainmenu.this, Statistics.class);
                        Mainmenu.this.startActivity(mainIntent);
                    }
                }
        );

        Button button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(Mainmenu.this, SettingsActivity.class);
                        Mainmenu.this.startActivity(mainIntent);
                    }
                }
        );

        Button button5 = (Button)findViewById(R.id.button5);
        button5.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Mainmenu.this.finish();
                    }
                }
        );

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(Mainmenu.this, BluetoothPair.class);
                        Mainmenu.this.startActivity(mainIntent);
                    }
                }
        );
    }

}
