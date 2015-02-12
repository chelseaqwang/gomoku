package com.clearwater.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class DrawPiece extends View {
    Paint paint = new Paint();
    float radius;
    float x;
    float y;
    int color;

    public DrawPiece(Context context, float x, float y, float radius, int color) {
        super(context);
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(color);
        canvas.drawCircle(x, y, radius,paint);

    }
}

