package com.clearwater.gomoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class selection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Button button_start = (Button)findViewById(R.id.button_start);
        button_start.setOnClickListener (
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        int size = 0, mode = 0;
                        RadioGroup sizeGroup = (RadioGroup)findViewById(R.id.size);
                        int sizeSelected = sizeGroup.getCheckedRadioButtonId();
                        RadioButton sizeRB = (RadioButton)findViewById(sizeSelected);
                        switch(sizeRB.getText().toString()) {
                            case "10*10":    size = 10; break;
                            case "15*15":    size = 15; break;
                            case "20*20":    size = 20; break;
                        }

                        RadioGroup modeGroup = (RadioGroup)findViewById(R.id.mode);
                        int modeSelected = modeGroup.getCheckedRadioButtonId();
                        RadioButton modeRB = (RadioButton)findViewById(modeSelected);
                        switch(modeRB.getText().toString()) {
                            case "Offline":    mode = 1; break;
                            case "Computer":   mode = 2; break;
                            case "Online":     mode = 3; break;
                        }

                        Intent intent = new Intent(selection.this, board.class);
                        intent.putExtra("SIZE", size);
                        intent.putExtra("MODE", mode);
                        startActivity(intent);
                    }
                }
        );
    }

}
