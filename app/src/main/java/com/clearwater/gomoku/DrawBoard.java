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
        int width = getResources().getDisplayMetrics().widthPixels;
        float padding = width/size/2;
        paint.setColor(0xFFFFED2C);
        canvas.drawRect(padding/3, padding/3, width - padding/3, width - padding/3, paint);
        paint.setColor(0xFFFFB203);
//        canvas.drawRect(padding, padding, width - padding, width - padding, paint);

/*
        for (float i = padding; i < width-width/size; i += 2 * width/size) {
            for (float j = padding; j < width-width/size; j += 2 * width/size) {
                canvas.drawRect(i, j, i+width/size, j+width/size, paint);
            }
        }

        for (float i = 3*padding; i < width-width/size; i += 2 * width/size) {
            for (float j = 3*padding; j < width-width/size; j += 2 * width/size) {
                canvas.drawRect(i, j, i+width/size, j+width/size, paint);
            }
        }
*/
        paint.setStrokeWidth(5);
        //paint.setColor(Color.BLACK);

        for (float i = padding; i < width; i+=width/size) {
            canvas.drawLine(padding, i, width-padding, i, paint);
            canvas.drawLine(i, padding, i, width-padding, paint);
        }

    }
}
