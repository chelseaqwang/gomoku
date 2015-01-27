package com.clearwater.gomoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class level extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        DrawBoard drawBoard;

        Button button_small = (Button)findViewById(R.id.button_small);
        button_small.setOnClickListener (
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(level.this, board.class);
                        intent.putExtra("SIZE", 10);
                        startActivity(intent);

                    }
                }
        );
        Button button_medium = (Button)findViewById(R.id.button_medium);
        button_medium.setOnClickListener (
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(level.this, board.class);
                        intent.putExtra("SIZE", 15);
                        startActivity(intent);
                    }
                }
        );
        Button button_large = (Button)findViewById(R.id.button_large);
        button_large.setOnClickListener (
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(level.this, board.class);
                        intent.putExtra("SIZE", 20);
                        startActivity(intent);
                    }
                }
        );


    }

}
