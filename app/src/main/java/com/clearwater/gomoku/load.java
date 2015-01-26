package com.clearwater.gomoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class load extends Activity {

    private final int LOAD_DISPLAY_LENGHT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(load.this, mainmenu.class);
                load.this.startActivity(mainIntent);
                load.this.finish();
            }
        }, LOAD_DISPLAY_LENGHT);

    }
}