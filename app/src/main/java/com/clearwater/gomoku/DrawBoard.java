package com.clearwater.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawBoard extends View {
    Paint paint = new Paint();
    float size;

    public DrawBoard(Context context, int size) {
        super(context);
        this.size = (float)size;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        float padding = 1200/size/2;

        for (float i = padding; i < 1200; i+=1200/size) {
            canvas.drawLine(padding, i, 1200-padding, i, paint);
            canvas.drawLine(i, padding, i, 1200-padding, paint);
        }

    }
}
