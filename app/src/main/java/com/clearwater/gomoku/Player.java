package com.clearwater.gomoku;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class Player {
    public String name;
    public int color;
    public int win;
    public Player opponent;

    public Player(String name, int color) {
        this.name = name;
        this.color = color;
        win = 0;
    }

    public String getColorName() {
        if (color == Color.BLACK) {
            return "Black";
        } else {
            return "White";
        }
    }

    public char getFlag() {
        if (color == Color.BLACK) {
            return 'b';
        } else {
            return 'w';
        }
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void recordWin() {

        win++;
    }
}
