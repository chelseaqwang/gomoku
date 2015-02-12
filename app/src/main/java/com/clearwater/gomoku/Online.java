package com.clearwater.gomoku;


import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Online extends PlayIF {
    public void setPlayer() {} // to be implemented

    public void play(MotionEvent m) {} // to be implemented

    public void addResetButton() {
        Button button_play_again = (Button)findViewById(R.id.button_play_again);
        button_play_again.setVisibility(View.VISIBLE);
        button_play_again.setOnClickListener (
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Online.this, Online.class);
                        intent.putExtra("SIZE", size);
                        startActivity(intent);
                    }
                }
        );
    }
}
