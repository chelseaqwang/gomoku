package com.clearwater.gomoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
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
        int gradient = 0xFFE6E6E6;
        paint.setColor(color);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setShader(new RadialGradient(x + radius/3, y + radius/3,
                radius, gradient, color, Shader.TileMode.MIRROR));
        if (color == Color.BLACK) {
            paint.setShader(new RadialGradient(x + radius/3, y + radius/3,
                    radius, gradient, color, Shader.TileMode.MIRROR));;
        } else {
            paint.setShader(new RadialGradient(x - radius/3, y - radius/3,
                    radius, gradient, color, Shader.TileMode.MIRROR));;
        }
        canvas.drawCircle(x, y, radius, paint);

    }
}

