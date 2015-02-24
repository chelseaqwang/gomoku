package com.clearwater.gomoku;

import android.content.Intent;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class AI extends PlayIF {

    public void setPlayer() {
        player1 = new Player("Peter", Color.BLACK);
        player2 = new Player("Computer", Color.WHITE);
        player1.setOpponent(player2);
        player2.setOpponent(player1);
    }

    public void play(MotionEvent m) {
        float x = m.getX();
        float y = m.getY();
        float grid = 1200/size;
        float radius;
        int i, j;

        i = Math.round((x-grid/2)/grid);
        j = Math.round((y-grid/2)/grid);

        if(!checkIndex(i, j)) return;
        if (move[i][j] != '\0') return;

        round++;
        move[i][j] = player1.getFlag();

        radius = (float) (grid*0.9/2);
        addPiece(i*grid+grid/2, j*grid+grid/2, radius, player1.color);
        Boolean end = display(i, j, player1);
        if(end) return;

        int[] ai = getAIMove();
        if(!checkIndex(ai[0], ai[1])) return;
        if (move[ai[0]][ai[1]] != '\0') return;
        round++;
        move[ai[0]][ai[1]] = player2.getFlag();
        radius = (float) (grid*0.9/2);
        addPiece(ai[0]*grid+grid/2, ai[1]*grid+grid/2, radius, player2.color);
        display(ai[0], ai[1], player2);
    }

    private int getValue(int i,int j){
        char[][] temp = new char[size + 10][size + 10];
        int value;
        for (int x=0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                temp[x + 5][y + 5] = move[x][y];
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

    private int[] getAIMove() {
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

    public void addResetButton() {
        Button button_play_again = (Button)findViewById(R.id.button_play_again);
        button_play_again.setVisibility(View.VISIBLE);
        button_play_again.setOnClickListener (
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        AI.this.finish();
                        Intent intent = new Intent(AI.this, AI.class);
                        intent.putExtra("SIZE", size);
                        startActivity(intent);
                    }
                }
        );
    }
}
