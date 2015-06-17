package com.example.vit.myrating.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Vit on 13.06.2015.
 */
public class PieDivider extends View {

    Paint paint;
    RectF oval;

    float startAngle, sweepAngle;
    static final float D_TO_R = 0.0174532925f;
    Rect labelRect;

    public PieDivider(Context context, float startAngle, float sweepAngle) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        paint.setColor(Color.WHITE);

        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;

        oval = new RectF();
        labelRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        oval.set(0, 0, canvas.getWidth(), canvas.getHeight());
        float angle = (startAngle + (sweepAngle / 2)) * D_TO_R;

        float x = oval.centerX() + (float) Math.cos(angle) * (oval.width() / 2 );
        float y = oval.centerY() + (float) Math.sin(angle) * (oval.height() / 2);
        canvas.drawLine(oval.centerX(), oval.centerY(), x , y, paint);
    }
}
