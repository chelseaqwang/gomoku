package com.clearwater.gomoku;

import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.content.Intent;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Button;


public class board extends ActionBarActivity {
    DrawBoard drawBoard;
    int size, mode;
    public char[][] piece_array;
    int round = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        if(checkWin(i, j, Color.BLACK)) return;

        int[] ai = aiMove();
        if(!checkIndex(ai[0], ai[1])) return;
        if (piece_array[ai[0]][ai[1]] != '\0') return;
        round++;
        piece_array[ai[0]][ai[1]] = 'w';
        radius = (float) (grid*0.9/2);
        DrawPiece aiPiece =
                new DrawPiece(context, ai[0]*grid+grid/2, ai[1]*grid+grid/2, radius, Color.WHITE);
        addContentView(aiPiece,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        display(ai[0], ai[1], Color.WHITE);

    }

    private int getValue(int i,int j){
        char[][] temp = new char[size + 10][size + 10];
        int value;
        for (int x=0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                temp[x + 5][y + 5] = piece_array[x][y];
            }
        }
        i=i+5;
        j=j+5;
        if(((temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='b')&&(temp[i][j+4]=='b')&&(temp[i][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j-2]=='b')&&(temp[i][j-3]=='b')&&(temp[i][j-4]=='b')&&(temp[i][j-5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-3][j]=='b')&&(temp[i-4][j]=='b')&&(temp[i-5][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='b')&&(temp[i+4][j]=='b')&&(temp[i+5][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='b')&&(temp[i+4][j+4]=='b')&&(temp[i+5][j+5]=='\0'))||           //空红红红红空型
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='b')&&(temp[i-4][j+4]=='b')&&(temp[i-5][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='b')&&(temp[i+4][j-4]=='b')&&(temp[i+5][j-5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-3][j-3]=='b')&&(temp[i-4][j-4]=='b')&&(temp[i-5][j-5]=='\0'))){
            value=10;
            return value;
        }
        else if(((temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='w')&&(temp[i][j+4]=='w')&&(temp[i][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j-2]=='w')&&(temp[i][j-3]=='w')&&(temp[i][j-4]=='w')&&(temp[i][j-5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-3][j]=='w')&&(temp[i-4][j]=='w')&&(temp[i-5][j]=='\0'))||                   //空红红红红空型
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='w')&&(temp[i+4][j]=='w')&&(temp[i+5][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='w')&&(temp[i+4][j+4]=='w')&&(temp[i+5][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='w')&&(temp[i-4][j+4]=='w')&&(temp[i-5][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='w')&&(temp[i+4][j-4]=='w')&&(temp[i+5][j-5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-3][j-3]=='w')&&(temp[i-4][j-4]=='w')&&(temp[i-5][j-5]=='\0'))){
            value=1000;
            return value;
        }
        else if(((temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='b')&&(temp[i][j+4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j-2]=='b')&&(temp[i][j-3]=='b')&&(temp[i][j-4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='b')&&(temp[i+4][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-3][j]=='b')&&(temp[i-4][j]=='\0'))||                            //空红红红空型
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='b')&&(temp[i+4][j+4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='b')&&(temp[i+4][j-4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='b')&&(temp[i-4][j+4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-3][j-3]=='b')&&(temp[i-4][j-4]=='\0'))||

                ((temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='\0')&&(temp[i][j+3]=='b')&&(temp[i][j+4]=='b')&&(temp[i][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j-2]=='\0')&&(temp[i][j-3]=='b')&&(temp[i][j-4]=='b')&&(temp[i][j-5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='\0')&&(temp[i+3][j]=='b')&&(temp[i+4][j]=='b')&&(temp[i+5][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='\0')&&(temp[i-3][j]=='b')&&(temp[i-4][j]=='b')&&(temp[i-5][j]=='\0'))||                               //空红空红红空
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='\0')&&(temp[i+3][j+3]=='b')&&(temp[i+4][j+4]=='b')&&(temp[i+5][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='\0')&&(temp[i+3][j-3]=='b')&&(temp[i+4][j-4]=='b')&&(temp[i+5][j-5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='\0')&&(temp[i-3][j+3]=='b')&&(temp[i-4][j+4]=='b')&&(temp[i-5][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='\0')&&(temp[i-3][j-3]=='b')&&(temp[i-4][j-4]=='b')&&(temp[i-5][j-5]=='\0'))||

                ((temp[i][j-2]=='b')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b'))||
                ((temp[i-2][j]=='b')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b'))||
                ((temp[i+2][j-2]=='b')&&(temp[i+1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b'))||
                ((temp[i-2][j+2]=='b')&&(temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b'))||                            //红红空红红型

                ((temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='b'))||
                ((temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='b'))||
                ((temp[i][j-3]=='b')&&(temp[i][j-2]=='b')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b'))||
                ((temp[i-3][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b'))||                            //红空红红红型
                ((temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='b'))||
                ((temp[i-3][j-3]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b'))||
                ((temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='b'))||
                ((temp[i+1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='b'))||

                ((temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='b')&&(temp[i][j+4]=='b')&&(temp[i][j+5]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j-2]=='b')&&(temp[i][j-3]=='b')&&(temp[i][j-4]=='b')&&(temp[i][j-5]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-3][j]=='b')&&(temp[i-4][j]=='b')&&(temp[i-5][j]=='w'))||                   //空红红红红蓝型
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='b')&&(temp[i+4][j]=='b')&&(temp[i+5][j]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='b')&&(temp[i+4][j+4]=='b')&&(temp[i+5][j+5]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='b')&&(temp[i-4][j+4]=='b')&&(temp[i-5][j+5]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='b')&&(temp[i+4][j-4]=='b')&&(temp[i+5][j-5]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-3][j-3]=='b')&&(temp[i-4][j-4]=='b')&&(temp[i-5][j-5]=='w'))||

                ((temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='b')&&(temp[i][j+4]=='w'))||
                ((temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='b')&&(temp[i+4][j]=='w'))||
                ((temp[i][j-4]=='w')&&(temp[i][j-3]=='b')&&(temp[i][j-2]=='b')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b'))||
                ((temp[i][j-4]=='w')&&(temp[i-3][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b'))||                            //红空红红红蓝
                ((temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='b')&&(temp[i+4][j+4]=='w'))||
                ((temp[i-4][j-4]=='w')&&(temp[i-3][j-3]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b'))||
                ((temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='b')&&(temp[i+4][j-4]=='w'))||
                ((temp[i+1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='b')&&(temp[i-4][j+4]=='w'))||

                ((temp[i][j-2]=='w')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='b'))||
                ((temp[i-2][j]=='w')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='b'))||
                ((temp[i][j-3]=='b')&&(temp[i][j-2]=='b')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='w'))||
                ((temp[i-3][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='w'))||                            //红红红空红蓝型
                ((temp[i-2][j-2]=='w')&&(temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='b'))||
                ((temp[i-3][j-3]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='w'))||
                ((temp[i-2][j+2]=='w')&&(temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='b'))||
                ((temp[i+2][j-2]=='w')&&(temp[i+1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='b'))||

                ((temp[i][j-3]=='w')&&(temp[i][j-2]=='b')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b'))||
                ((temp[i-3][j]=='w')&&(temp[i-2][j]=='b')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b'))||
                ((temp[i+3][j-3]=='w')&&(temp[i+2][j-2]=='b')&&(temp[i+1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b'))||
                ((temp[i-3][j+3]=='w')&&(temp[i-2][j+2]=='b')&&(temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b'))||
                ((temp[i][j-2]=='b')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='w'))||                            //红红空红红蓝型
                ((temp[i-2][j]=='b')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='w'))||
                ((temp[i-3][j-3]=='w')&&(temp[i-2][j-2]=='b')&&(temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b'))||
                ((temp[i+3][j+3]=='w')&&(temp[i+2][j+2]=='b')&&(temp[i+1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='b'))||

                ((temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='b')&&(temp[i][j+4]=='b'))||
                ((temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='b')&&(temp[i+4][j]=='b'))||
                ((temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='b')&&(temp[i+4][j+4]=='b'))||
                ((temp[i+1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-3][j-3]=='b')&&(temp[i-4][j-4]=='b'))||
                ((temp[i][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='b')&&(temp[i][j+4]=='b'))||           //红红红红空蓝型
                ((temp[i+1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-3][j]=='b')&&(temp[i-4][j]=='b'))||
                ((temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='b')&&(temp[i+4][j-4]=='b'))||
                ((temp[i+1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='b')&&(temp[i-4][j+4]=='b'))){
            value=200;
            return value;
        }
        else if(((temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='w')&&(temp[i][j+4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j-2]=='w')&&(temp[i][j-3]=='w')&&(temp[i][j-4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='w')&&(temp[i+4][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-3][j]=='w')&&(temp[i-4][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='w')&&(temp[i+4][j+4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='w')&&(temp[i+4][j-4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='w')&&(temp[i-4][j+4]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-3][j-3]=='w')&&(temp[i-4][j-4]=='\0'))||

                ((temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='\0')&&(temp[i][j+3]=='w')&&(temp[i][j+4]=='w')&&(temp[i][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j-2]=='\0')&&(temp[i][j-3]=='w')&&(temp[i][j-4]=='w')&&(temp[i][j-5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='\0')&&(temp[i+3][j]=='w')&&(temp[i+4][j]=='w')&&(temp[i+5][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='\0')&&(temp[i-3][j]=='w')&&(temp[i-4][j]=='w')&&(temp[i-5][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='\0')&&(temp[i+3][j+3]=='w')&&(temp[i+4][j+4]=='w')&&(temp[i+5][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='\0')&&(temp[i+3][j-3]=='w')&&(temp[i+4][j-4]=='w')&&(temp[i+5][j-5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='\0')&&(temp[i-3][j+3]=='w')&&(temp[i-4][j+4]=='w')&&(temp[i-5][j+5]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='\0')&&(temp[i-3][j-3]=='w')&&(temp[i-4][j-4]=='w')&&(temp[i-5][j-5]=='\0'))||

                ((temp[i][j-2]=='w')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w'))||
                ((temp[i-2][j]=='w')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w'))||
                ((temp[i+2][j-2]=='w')&&(temp[i+1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w'))||
                ((temp[i-2][j+2]=='w')&&(temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w'))||

                ((temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='w'))||
                ((temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='w'))||
                ((temp[i][j-3]=='w')&&(temp[i][j-2]=='w')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w'))||
                ((temp[i-3][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w'))||
                ((temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='w'))||
                ((temp[i-3][j-3]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w'))||
                ((temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='w'))||
                ((temp[i+1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='w'))||

                ((temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='w')&&(temp[i][j+4]=='w')&&(temp[i][j+5]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j-2]=='w')&&(temp[i][j-3]=='w')&&(temp[i][j-4]=='w')&&(temp[i][j-5]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-3][j]=='w')&&(temp[i-4][j]=='w')&&(temp[i-5][j]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='w')&&(temp[i+4][j]=='w')&&(temp[i+5][j]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='w')&&(temp[i+4][j+4]=='w')&&(temp[i+5][j+5]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='w')&&(temp[i-4][j+4]=='w')&&(temp[i-5][j+5]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='w')&&(temp[i+4][j-4]=='w')&&(temp[i+5][j-5]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-3][j-3]=='w')&&(temp[i-4][j-4]=='w')&&(temp[i-5][j-5]=='b'))||

                ((temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='w')&&(temp[i][j+4]=='b'))||
                ((temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='w')&&(temp[i+4][j]=='b'))||
                ((temp[i][j-4]=='b')&&(temp[i][j-3]=='w')&&(temp[i][j-2]=='w')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w'))||
                ((temp[i][j-4]=='b')&&(temp[i-3][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w'))||
                ((temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='w')&&(temp[i+4][j+4]=='b'))||
                ((temp[i-4][j-4]=='b')&&(temp[i-3][j-3]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w'))||
                ((temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='w')&&(temp[i+4][j-4]=='b'))||
                ((temp[i+1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='w')&&(temp[i-4][j+4]=='b'))||

                ((temp[i][j-2]=='b')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='w'))||
                ((temp[i-2][j]=='b')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='w'))||
                ((temp[i][j-3]=='w')&&(temp[i][j-2]=='w')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='b'))||
                ((temp[i-3][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='b'))||
                ((temp[i-2][j-2]=='b')&&(temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='w'))||
                ((temp[i-3][j-3]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='b'))||
                ((temp[i-2][j+2]=='b')&&(temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='w'))||
                ((temp[i+2][j-2]=='b')&&(temp[i+1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='w'))||

                ((temp[i][j-3]=='b')&&(temp[i][j-2]=='w')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w'))||
                ((temp[i-3][j]=='b')&&(temp[i-2][j]=='w')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w'))||
                ((temp[i+3][j-3]=='b')&&(temp[i+2][j-2]=='w')&&(temp[i+1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w'))||
                ((temp[i-3][j+3]=='b')&&(temp[i-2][j+2]=='w')&&(temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w'))||
                ((temp[i][j-2]=='w')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='b'))||
                ((temp[i-2][j]=='w')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='b'))||
                ((temp[i-3][j-3]=='b')&&(temp[i-2][j-2]=='w')&&(temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w'))||
                ((temp[i+3][j+3]=='b')&&(temp[i+2][j+2]=='w')&&(temp[i+1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='w'))||

                ((temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='w')&&(temp[i][j+4]=='w'))||
                ((temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='w')&&(temp[i+4][j]=='w'))||
                ((temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='w')&&(temp[i+4][j+4]=='w'))||
                ((temp[i+1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-3][j-3]=='w')&&(temp[i-4][j-4]=='w'))||
                ((temp[i][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='w')&&(temp[i][j+4]=='w'))||
                ((temp[i+1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-3][j]=='w')&&(temp[i-4][j]=='w'))||
                ((temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='w')&&(temp[i+4][j-4]=='w'))||
                ((temp[i+1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='w')&&(temp[i-4][j+4]=='w'))){
            value=500;
            return value;
        }
        else if(((temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j-2]=='b')&&(temp[i][j-3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-3][j]=='\0'))||                            //空红红空型
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-3][j-3]=='\0'))||

                ((temp[i-2][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='\0'))||
                ((temp[i][j-2]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='\0'))||                          //空红空红空型
                ((temp[i-2][j+2]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='\0'))||
                ((temp[i-2][j-2]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='\0'))||

                ((temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='b')&&(temp[i][j+4]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j-2]=='b')&&(temp[i][j-3]=='b')&&(temp[i][j-4]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='b')&&(temp[i+4][j]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-3][j]=='b')&&(temp[i-4][j]=='w'))||                            //空红红红蓝型
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='b')&&(temp[i+4][j+4]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='b')&&(temp[i+4][j-4]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='b')&&(temp[i-4][j+4]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-3][j-3]=='b')&&(temp[i-4][j-4]=='w'))||

                ((temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='w'))||
                ((temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='w'))||
                ((temp[i][j-3]=='w')&&(temp[i][j-2]=='b')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b'))||
                ((temp[i-3][j]=='w')&&(temp[i-2][j]=='b')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b'))||                            //红空红红蓝型
                ((temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='w'))||
                ((temp[i-3][j-3]=='w')&&(temp[i-2][j-2]=='b')&&(temp[i-1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='b'))||
                ((temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='w'))||
                ((temp[i+1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='w'))||

                ((temp[i][j-2]=='b')&&(temp[i][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='w'))||
                ((temp[i-2][j]=='b')&&(temp[i-1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='w'))||
                ((temp[i+2][j-2]=='b')&&(temp[i+1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='w'))||
                ((temp[i-2][j+2]=='b')&&(temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='w'))||                            //红红空红蓝型
                ((temp[i][j+2]=='b')&&(temp[i][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j-2]=='w'))||
                ((temp[i+2][j]=='b')&&(temp[i+1][j]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='w'))||
                ((temp[i-2][j+2]=='b')&&(temp[i-1][j+1]=='b')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='w'))||
                ((temp[i+2][j-2]=='b')&&(temp[i+1][j-1]=='b')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='w'))){
            value=4;
            return value;
        }
        else if(((temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j-2]=='w')&&(temp[i][j-3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-3][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-3][j-3]=='\0'))||

                ((temp[i-2][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='\0'))||
                ((temp[i][j-2]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='\0'))||
                ((temp[i-2][j+2]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='\0'))||
                ((temp[i-2][j-2]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='\0'))||

                ((temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='w')&&(temp[i][j+4]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j-2]=='w')&&(temp[i][j-3]=='w')&&(temp[i][j-4]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='w')&&(temp[i+4][j]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-3][j]=='w')&&(temp[i-4][j]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='w')&&(temp[i+4][j+4]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='w')&&(temp[i+4][j-4]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='w')&&(temp[i-4][j+4]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-3][j-3]=='w')&&(temp[i-4][j-4]=='b'))||

                ((temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='b'))||
                ((temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='b'))||
                ((temp[i][j-3]=='b')&&(temp[i][j-2]=='w')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w'))||
                ((temp[i-3][j]=='b')&&(temp[i-2][j]=='w')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w'))||
                ((temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='b'))||
                ((temp[i-3][j-3]=='b')&&(temp[i-2][j-2]=='w')&&(temp[i-1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j+1]=='w'))||
                ((temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='b'))||
                ((temp[i+1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='b'))||

                ((temp[i][j-2]=='w')&&(temp[i][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='b'))||
                ((temp[i-2][j]=='w')&&(temp[i-1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='b'))||
                ((temp[i+2][j-2]=='w')&&(temp[i+1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='b'))||
                ((temp[i-2][j+2]=='w')&&(temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='b'))||
                ((temp[i][j+2]=='w')&&(temp[i][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j-2]=='b'))||
                ((temp[i+2][j]=='w')&&(temp[i+1][j]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='b'))||
                ((temp[i-2][j+2]=='w')&&(temp[i-1][j+1]=='w')&&(temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='b'))||
                ((temp[i+2][j-2]=='w')&&(temp[i+1][j-1]=='w')&&(temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='b'))){
            value=3;
            return value;
        }
        else if(((temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j-2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='\0'))||                               //空红空
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='\0'))||

                ((temp[i][j]=='\0')&&(temp[i][j+1]=='b')&&(temp[i][j+2]=='b')&&(temp[i][j+3]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='b')&&(temp[i][j-2]=='b')&&(temp[i][j-3]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='b')&&(temp[i+2][j]=='b')&&(temp[i+3][j]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='b')&&(temp[i-2][j]=='b')&&(temp[i-3][j]=='w'))||                            //空红红蓝型
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='b')&&(temp[i+2][j+2]=='b')&&(temp[i+3][j+3]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='b')&&(temp[i+2][j-2]=='b')&&(temp[i+3][j-3]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='b')&&(temp[i-2][j+2]=='b')&&(temp[i-3][j+3]=='w'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='b')&&(temp[i-2][j-2]=='b')&&(temp[i-3][j-3]=='w'))){
            value=2;
            return value;
        }
        else if(((temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j-2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='\0'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='\0'))||

                ((temp[i][j]=='\0')&&(temp[i][j+1]=='w')&&(temp[i][j+2]=='w')&&(temp[i][j+3]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i][j-1]=='w')&&(temp[i][j-2]=='w')&&(temp[i][j-3]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j]=='w')&&(temp[i+2][j]=='w')&&(temp[i+3][j]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j]=='w')&&(temp[i-2][j]=='w')&&(temp[i-3][j]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j+1]=='w')&&(temp[i+2][j+2]=='w')&&(temp[i+3][j+3]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i+1][j-1]=='w')&&(temp[i+2][j-2]=='w')&&(temp[i+3][j-3]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j+1]=='w')&&(temp[i-2][j+2]=='w')&&(temp[i-3][j+3]=='b'))||
                ((temp[i][j]=='\0')&&(temp[i-1][j-1]=='w')&&(temp[i-2][j-2]=='w')&&(temp[i-3][j-3]=='b'))){
            value=1;
            return value;
        }
        else{
            value=0;
            return value;
        }


    }

    private int[] aiMove() {
        int max = 0;
        int maxi = -1, maxj = -1;
        int value;
        for (int i=0;i<size;i++) {
            for (int j = 0; j < size; j++) {
                value= getValue(i, j);
                if (value > max) {
                    max = value;
                    maxi = i;
                    maxj = j;
                }
            }
        }
        return new int[]{maxi, maxj};
    }

    private void display(int i, int j, int color) {
        TextView textView = (TextView)findViewById(R.id.winText);
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        String player;
        if (color == Color.BLACK) {
            player = "Black";
        } else {
            player = "White";
        }
        if(round > 8 && (checkWin(i, j, color) || checkTie())) {
            if (checkWin(i, j, color)) {
                textView.setText(player + " wins!");
            } else if (checkTie()) {
                textView.setText("It's a tie!");
            }
            chronometer.stop();
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
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }

    }

    private boolean checkWin(int i, int j, int color) {

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
        return onlyFive(s.toString(), color);

    }

    private boolean checkTie() {
        if (round < size * size * 0.7) return false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (piece_array[i][j] != '\0') continue;
                String sblack, swhite;
                StringBuffer s = new StringBuffer();
                for (int p = Math.max(i-5, 0); p < Math.min(i+6, size); p++) {
                    s.append(piece_array[p][j]);
                }
                sblack = s.toString().replace('\0', 'b');
                if (sblack.contains("bbbbb")) return false;
                swhite = s.toString().replace('\0', 'w');
                if (swhite.contains("wwwww")) return false;

                s = new StringBuffer();
                for (int q = Math.max(j-5, 0); q < Math.min(j+6, size); q++) {
                    s.append(piece_array[i][q]);
                }
                sblack = s.toString().replace('\0', 'b');
                if (sblack.contains("bbbbb")) return false;
                swhite = s.toString().replace('\0', 'w');
                if (swhite.contains("wwwww")) return false;

                s = new StringBuffer();
                for (int p = i-5, q = j-5; p < i+6; p++, q++) {
                    if (!checkIndex(p, q)) continue;
                    s.append(piece_array[p][q]);
                }
                sblack = s.toString().replace('\0', 'b');
                if (sblack.contains("bbbbb")) return false;
                swhite = s.toString().replace('\0', 'w');
                if (swhite.contains("wwwww")) return false;

                s = new StringBuffer();
                for (int p = i-5, q = j+5; p < i+6; p++, q--) {
                    if (!checkIndex(p, q)) continue;
                    s.append(piece_array[p][q]);
                }
                sblack = s.toString().replace('\0', 'b');
                if (sblack.contains("bbbbb")) return false;
                swhite = s.toString().replace('\0', 'w');
                if (swhite.contains("wwwww")) return false;
            }
        }
    return true;
    }

    private boolean checkIndex(int p, int q) {
        return !(p < 0 || p >= size || q < 0 || q >= size);
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

        return s.contains(five) && !s.contains(six) && !s.contains(seven);
    }

    private String whichPlayer(int i) {
        if (i % 2 == 1) {
            return "Black";
        } else {
            return "White";
        }
    }

}
