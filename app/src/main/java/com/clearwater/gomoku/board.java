package com.clearwater.gomoku;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.content.Intent;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Button;


public class board extends ActionBarActivity {
    DrawBoard drawBoard;
    int size;
    public char[][] piece_array;
    int round = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        size = intent.getIntExtra("SIZE", 10);
        piece_array = new char[size][size];

        drawBoard = new DrawBoard(this, size);
        addContentView(drawBoard,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));

        drawBoard.setOnTouchListener(
                new DrawBoard.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent m) {
                        play(m);
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void play(MotionEvent m) {

        String color;
        int intColor;
        Context context = getApplicationContext();
        float x = m.getX();
        float y = m.getY();
        float grid = 800/size;
        float radius;
        int i, j;
        TextView textView = (TextView)findViewById(R.id.winText);

        i = Math.round((x-grid/2)/grid);
        j = Math.round((y-grid/2)/grid);

        if(j >= size) return;
        if (piece_array[i][j] != '\0') return;

        round++;
        color = whichPlayer(round);
        if (color == "Black") {
            piece_array[i][j] = 'b';
            intColor = Color.BLACK;
        } else {
            piece_array[i][j] = 'w';
            intColor = Color.WHITE;
        }

        x = Math.round((x-grid/2)/grid)*grid + grid/2;
        y = Math.round((y-grid/2)/grid)*grid + grid/2;
        radius = (float) (grid*0.9/2);

        DrawPiece drawPiece = new DrawPiece(context, x, y, radius, intColor);
        addContentView(drawPiece,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


        if(round > 8 && checkWin(i, j, intColor)) {
            textView.setText(color + " wins!");
            drawBoard.setOnTouchListener(null);

            Button button_play_again = (Button)findViewById(R.id.button_play_again);
            button_play_again.setVisibility(View.VISIBLE);
            button_play_again.setOnClickListener (
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(board.this, board.class);
                            intent.putExtra("SIZE", size);
                            startActivity(intent);
                        }
                    }
            );
        } else {
            textView.setText(whichPlayer(round + 1) + "'s turn");
        }
    }

    public boolean checkWin(int i, int j, int color) {

        StringBuffer s = new StringBuffer();
        for (int p = Math.max(i-5, 0); p < Math.min(i+6, size); p++) {
            s.append(piece_array[p][j]);
        }
        if (onlyFive(s.toString(), color)) return true;

        s = new StringBuffer();
        for (int q = Math.max(j-5, 0); q < Math.min(j+6, size); q++) {
            s.append(piece_array[i][q]);
        }
        if (onlyFive(s.toString(), color)) return true;

        s = new StringBuffer();
        for (int p = i-5, q = j-5; p < i+6; p++, q++) {
            if (!checkIndex(p, q)) continue;
            s.append(piece_array[p][q]);
        }
        if (onlyFive(s.toString(), color)) return true;

        s = new StringBuffer();
        for (int p = i-5, q = j+5; p < i+6; p++, q--) {
            if (!checkIndex(p, q)) continue;
            s.append(piece_array[p][q]);
        }
        if (onlyFive(s.toString(), color)) return true;

        return false;
    }

    private boolean checkIndex(int p, int q) {
        if (p < 0 || p >= size || q < 0 || q >= size) {
            return false;
        } else {
            return true;
        }
    }

    private boolean onlyFive(String s, int color) {
        String five, six, seven;

        if(color == Color.BLACK) {
            five = "bbbbb";
            six = "bbbbbb";
            seven = "wbbbbbw";
        } else {
            five = "wwwww";
            six = "wwwwww";
            seven = "bwwwwwb";
        }

        if (s.contains(five) && !s.contains(six) && !s.contains(seven)) {
            return true;
        } else {
            return false;
        }
    }

    private String whichPlayer(int i) {
        if (i % 2 == 0) {
            return "Black";
        } else {
            return "White";
        }
    }

}
