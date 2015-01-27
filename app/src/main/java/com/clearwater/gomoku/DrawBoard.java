package com.clearwater.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawBoard extends View {
    Paint paint = new Paint();
    int size;

    public DrawBoard(Context context, int size) {
        super(context);
        this.size = size;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.YELLOW);
        canvas.drawRect(10, 10, 790, 790, paint);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);

        for (int i = 10; i < 800; i+=780/size) {
            canvas.drawLine(10, i, 790, i, paint);
            canvas.drawLine(i, 10, i, 790, paint);
        }


    }
}
