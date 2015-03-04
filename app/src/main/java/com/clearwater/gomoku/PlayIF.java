package com.clearwater.gomoku;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.graphics.Color;

public abstract class PlayIF extends ActionBarActivity {
    public DrawBoard drawBoard;
    public int size;
    public char[][] move;
    public Player player1, player2;
    public int round = 0;
    protected int i, j;
    public DrawHighlight drawHighlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        size = intent.getIntExtra("SIZE", 10);
        move = new char[size][size];
        setPlayer();
        player1.win = intent.getIntExtra("WIN1", 0);
        player2.win = intent.getIntExtra("WIN2", 0);


        drawBoard = new DrawBoard(this, size);
        addContentView(drawBoard,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // initial display
        TextView textView = (TextView)findViewById(R.id.winText);
        textView.setText(player1.win + " : " + player2.win);
        GridLayout grid = (GridLayout)findViewById(R.id.info_grid);
        grid.setVisibility(View.VISIBLE);
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

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

    protected abstract void play(MotionEvent m);

    protected abstract void setPlayer();

    protected abstract void addResetButton();

    protected void addPiece(float x, float y, float radius, int color){
        Context context = getApplicationContext();
        DrawPiece drawPiece =
                new DrawPiece(context, x, y, radius, color);
        addContentView(drawPiece,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * Update the status of the program when a new piece is added
     * THe location of new added piece:
     *  @param i
     *  @param j
     *  @param player, the player who added the piece*
     */
    protected boolean display(int i, int j, Player player) {
        TextView winText = (TextView)findViewById(R.id.winText);
        TextView turnTxt = (TextView)findViewById(R.id.turnTxt);
        TextView turnTxt2 = (TextView)findViewById(R.id.turnTxt2);
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        Chronometer chronometer2 = (Chronometer) findViewById(R.id.chronometer2);
        if (checkWin(i, j, player.color) || checkTie()) {
            // play an alert sound when game ends
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (checkWin(i, j, player.color)) {
                winText.setText(player.getColorName() + " wins!");
                player.recordWin();
            } else if (checkTie()) {
                winText.setText("It's a tie!");
            }
            chronometer.stop();
            chronometer2.stop();
            chronometer.setVisibility(View.INVISIBLE);
            chronometer2.setVisibility(View.INVISIBLE);
            turnTxt.setVisibility(View.INVISIBLE);
            turnTxt2.setVisibility(View.INVISIBLE);
            drawBoard.setOnTouchListener(null);
            addResetButton();
            return true;
        } else {
            if (player.color == Color.WHITE) {
                turnTxt.setVisibility(View.VISIBLE);
                turnTxt2.setVisibility(View.INVISIBLE);
                chronometer2.stop();
                chronometer2.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.VISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            } else {
                turnTxt2.setVisibility(View.VISIBLE);
                turnTxt.setVisibility(View.INVISIBLE);
                chronometer.stop();
                chronometer.setVisibility(View.INVISIBLE);
                chronometer2.setVisibility(View.VISIBLE);
                chronometer2.setBase(SystemClock.elapsedRealtime());
                chronometer2.start();
            }
            return false;
        }

    }

    protected Boolean checkWin(int i, int j, int color) {
        if (round <= 8) return false;
        StringBuffer s = new StringBuffer();
        for (int p = Math.max(i-5, 0); p < Math.min(i+6, size); p++) {
            s.append(move[p][j]);
        }
        if (onlyFive(s.toString(), color)) return true;

        s = new StringBuffer();
        for (int q = Math.max(j-5, 0); q < Math.min(j+6, size); q++) {
            s.append(move[i][q]);
        }
        if (onlyFive(s.toString(), color)) return true;

        s = new StringBuffer();
        for (int p = i-5, q = j-5; p < i+6; p++, q++) {
            if (!checkIndex(p, q)) continue;
            s.append(move[p][q]);
        }
        if (onlyFive(s.toString(), color)) return true;

        s = new StringBuffer();
        for (int p = i-5, q = j+5; p < i+6; p++, q--) {
            if (!checkIndex(p, q)) continue;
            s.append(move[p][q]);
        }
        return onlyFive(s.toString(), color);

    }

    protected boolean checkTie() {
        if (round < size * size * 0.7) return false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (move[i][j] != '\0') continue;
                String sblack, swhite;
                StringBuffer s = new StringBuffer();
                for (int p = Math.max(i-5, 0); p < Math.min(i+6, size); p++) {
                    s.append(move[p][j]);
                }
                sblack = s.toString().replace('\0', 'b');
                if (sblack.contains("bbbbb")) return false;
                swhite = s.toString().replace('\0', 'w');
                if (swhite.contains("wwwww")) return false;

                s = new StringBuffer();
                for (int q = Math.max(j-5, 0); q < Math.min(j+6, size); q++) {
                    s.append(move[i][q]);
                }
                sblack = s.toString().replace('\0', 'b');
                if (sblack.contains("bbbbb")) return false;
                swhite = s.toString().replace('\0', 'w');
                if (swhite.contains("wwwww")) return false;

                s = new StringBuffer();
                for (int p = i-5, q = j-5; p < i+6; p++, q++) {
                    if (!checkIndex(p, q)) continue;
                    s.append(move[p][q]);
                }
                sblack = s.toString().replace('\0', 'b');
                if (sblack.contains("bbbbb")) return false;
                swhite = s.toString().replace('\0', 'w');
                if (swhite.contains("wwwww")) return false;

                s = new StringBuffer();
                for (int p = i-5, q = j+5; p < i+6; p++, q--) {
                    if (!checkIndex(p, q)) continue;
                    s.append(move[p][q]);
                }
                sblack = s.toString().replace('\0', 'b');
                if (sblack.contains("bbbbb")) return false;
                swhite = s.toString().replace('\0', 'w');
                if (swhite.contains("wwwww")) return false;
            }
        }
        return true;
    }

    protected boolean checkIndex(int p, int q) {
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

    protected Player whoseTurn(int i) {
        if (i % 2 == 1) {
            return player1;
        } else {
            return player2;
        }
    }

}
