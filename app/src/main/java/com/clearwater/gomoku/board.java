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
import java.util.Random;


public class board extends ActionBarActivity {
    DrawBoard drawBoard;
    int size, mode;
    public char[][] piece_array;
    int round = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        size = intent.getIntExtra("SIZE", 10);
        mode = intent.getIntExtra("MODE", 1);
        piece_array = new char[size][size];

        drawBoard = new DrawBoard(this, size);
        addContentView(drawBoard,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));

        switch(mode) {
            case 1:
                drawBoard.setOnTouchListener(
                        new DrawBoard.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent m) {
                                offlinePlay(m);
                                return true;
                            }
                        }
                );
                break;
            case 2:
                drawBoard.setOnTouchListener(
                        new DrawBoard.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent m) {
                                aiPlay(m);
                                return true;
                            }
                        }
                );
                break;
            case 3:
                drawBoard.setOnTouchListener(
                        new DrawBoard.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent m) {
                                // not implemented yet
                                return true;
                            }
                        }
                );
                break;
        }
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

    public void offlinePlay(MotionEvent m) {

        String color;
        int intColor;
        Context context = getApplicationContext();
        float x = m.getX();
        float y = m.getY();
        float grid = 800/size;
        float radius;
        int i, j;

        i = Math.round((x-grid/2)/grid);
        j = Math.round((y-grid/2)/grid);

        if(!checkIndex(i, j)) return;
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

        radius = (float) (grid*0.9/2);

        DrawPiece drawPiece =
                new DrawPiece(context, i*grid+grid/2, j*grid+grid/2, radius, intColor);
        addContentView(drawPiece,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        display(i, j, intColor);
    }

    public void aiPlay(MotionEvent m) {
        Context context = getApplicationContext();
        float x = m.getX();
        float y = m.getY();
        float grid = 800/size;
        float radius;
        int i, j;

        i = Math.round((x-grid/2)/grid);
        j = Math.round((y-grid/2)/grid);

        if(!checkIndex(i, j)) return;
        if (piece_array[i][j] != '\0') return;

        round++;
        piece_array[i][j] = 'b';

        radius = (float) (grid*0.9/2);
        DrawPiece playerPiece =
                new DrawPiece(context, i*grid+grid/2, j*grid+grid/2, radius, Color.BLACK);
        addContentView(playerPiece,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        display(i, j, Color.BLACK);

        int[] ai = aiMove(piece_array, Color.WHITE);
        if(!checkIndex(ai[0], ai[1])) return;
        if (piece_array[ai[0]][ai[1]] != '\0') return;
        round++;
        piece_array[ai[0]][ai[1]] = 'w';
        radius = (float) (grid*0.9/2);
        DrawPiece aiPiece =
                new DrawPiece(context, ai[0]*grid+grid/2, ai[1]*grid+grid/2, radius, Color.WHITE);
        addContentView(aiPiece,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        display(i, j, Color.WHITE);

    }

    public int[] aiMove(char[][] pArray, int color) {
        int i, j;
        do {
            Random random = new Random();
            i = random.nextInt(size);
            j = random.nextInt(size);
        } while(pArray[i][j] != '\0');
        return new int[] {i, j};
    }

    public void display(int i, int j, int color) {
        TextView textView = (TextView)findViewById(R.id.winText);
        String player;
        if (color == Color.BLACK) {
            player = "Black";
        } else {
            player = "White";
        }
        if(round > 8 && checkWin(i, j, color)) {
            textView.setText(player + " wins!");
            drawBoard.setOnTouchListener(null);

            Button button_play_again = (Button)findViewById(R.id.button_play_again);
            button_play_again.setVisibility(View.VISIBLE);
            button_play_again.setOnClickListener (
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(board.this, board.class);
                            intent.putExtra("SIZE", size);
                            intent.putExtra("MODE", mode);
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
        if (i % 2 == 1) {
            return "Black";
        } else {
            return "White";
        }
    }

}
