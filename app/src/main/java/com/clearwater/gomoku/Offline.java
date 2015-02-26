package com.clearwater.gomoku;

import android.content.Intent;
import android.view.MotionEvent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Offline extends PlayIF {

    public void setPlayer() {
//        try {
//            File file = getFileStreamPath("player.txt");
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String line = br.readLine();
//            System.out.println("*****************************");
//            System.out.println(line);
//            player1 = new Player(line.split("\\s+")[0], Color.BLACK);
//            player1.win = Integer.parseInt(line.split("\\s+")[1]);
//            line = br.readLine();
//            player2 = new Player(line.split("\\s+")[0], Color.WHITE);
//            player2.win = Integer.parseInt(line.split("\\s+")[1]);
//
//        } catch (IOException ignored) {
//            System.out.println("error");
//        }
        player1 = new Player("Peter", Color.BLACK);
        player2 = new Player("Jean", Color.WHITE);
        player1.setOpponent(player2);
        player2.setOpponent(player1);
    }

    public void play(MotionEvent m) {
        Player player;
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

        round++;
        player = whoseTurn(round);
        move[i][j] = player.getFlag();
        radius = (float) (grid*0.9/2);
        addPiece(i * grid + grid / 2, j * grid + grid / 2, radius, player.color);

        display(i, j, player);
    }

    public void addResetButton() {
        Button button_play_again = (Button)findViewById(R.id.button_play_again);
        button_play_again.setVisibility(View.VISIBLE);
        button_play_again.setOnClickListener (
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Offline.this.finish();
                        Intent intent = new Intent(Offline.this, Offline.class);
                        intent.putExtra("SIZE", size);
                        startActivity(intent);
                    }
                }
        );
    }

}
