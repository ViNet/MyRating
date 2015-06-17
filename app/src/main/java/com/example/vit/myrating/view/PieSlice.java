package com.example.vit.myrating.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

/**
 *
 */
public class PieSlice extends View{

    static final String TAG = "myrating";
    static final String CLASS = PieSlice.class.getSimpleName() + ": ";

    RectF oval;
    float startAngle, sweepAngle;
    Paint paint;
    int id;

    public PieSlice(Context context, int id, float startAngle, float sweepAngle, int color) {
        super(context);
        //Log.d(TAG, CLASS + " PieSlice() id=" + id);
        this.id = id;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        oval = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        oval.set(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawArc(oval, startAngle, sweepAngle, true, paint);
    }

    public boolean containsAngle(double angle){
        if(startAngle < 0){
            startAngle +=180;
        }
        double endAngle = startAngle + sweepAngle;
        //Log.d(TAG, CLASS + "start = " +startAngle + "end = " + endAngle + " contains = " + angle);

        if(angle > startAngle && angle <= endAngle){
            return true;
        }
        return false;
    }
}
