package com.clearwater.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawHighlight extends View {
    Paint paint = new Paint();
    float radius, hl_radius;
    float x;
    float y;

    public DrawHighlight(Context context, float x, float y, float hl_radius, float radius) {
        super(context);
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.hl_radius = hl_radius;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y, hl_radius, paint);
    }
}

