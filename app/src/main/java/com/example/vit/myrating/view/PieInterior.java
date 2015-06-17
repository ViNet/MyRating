package com.example.vit.myrating.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import java.util.Locale;

/**
 * Created by Vit on 13.06.2015.
 */
public class PieInterior extends View {
    Paint paint;
    PieChart pieChart;
    RectF oval;
    Paint textPaint;
    Rect textBounds;
    Paint inscriptionPaint;
    Paint arcPaint;
    RectF interiorArcBounds;
    int arcColor;

    String value = "";
    String inscription = "";

    float startAngle = 0.0f;
    float sweepAngle = 0.0f;

    final static String TAG = "myrating";
    final static String CLASS = PieInterior.class.getSimpleName() + ": ";

    public PieInterior(Context context, PieChart pieChart) {
        super(context);
        this.pieChart = pieChart;
        oval = new RectF();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#FF4D4D4D"));
        textPaint.setTextAlign(Paint.Align.CENTER);

        inscriptionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inscriptionPaint.setTextAlign(Paint.Align.CENTER);
        inscriptionPaint.setColor(Color.parseColor("#FF4D4D4D"));

        textBounds = new Rect();
        interiorArcBounds = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //(getWidth()/15) - circle's stroke width
        int radius = (getWidth() / 2) - (getWidth()/15);
        canvas.drawCircle(canvas.getHeight() / 2, canvas.getWidth() / 2, radius, paint);

        // get side length of square inscribed in a circle
        // diameter is a hypotenuse, so
        double squareSideLength = Math.sqrt((2 * 2 * radius * radius) / 2) / 2;

        // calculate left top of square inscribed in a circle
        float leftTop = (float) ((canvas.getHeight() / 2) - squareSideLength);
        // calculate right bottom of square inscribed in a circle
        float rightBottom = (float) ((canvas.getHeight() / 2) + squareSideLength);
        textBounds.set((int) leftTop, (int) leftTop, (int) rightBottom, (int) rightBottom);

        textPaint.setTextSize(radius / 2);

        canvas.drawText(value
                , textBounds.centerX()
                , textBounds.centerY()
                , textPaint);

        inscriptionPaint.setTextSize(radius / 4);

        canvas.drawText(inscription
                , textBounds.centerX()
                , textBounds.centerY() + radius / 4
                , inscriptionPaint);

        // draw interior arc (selector)
        if(sweepAngle != 0){
            arcColor = Color.parseColor("#FF4D4D4D");
            arcPaint.setColor(arcColor);
            interiorArcBounds.set(getWidth()/15 + getWidth()/60
                    , getWidth()/15 + getWidth()/60
                    , getWidth() - (getWidth()/15 + getWidth()/60)
                    , getHeight() - (getWidth()/15 + getWidth()/60));
            arcPaint.setStrokeWidth(getWidth()/60);
            canvas.drawArc(interiorArcBounds, startAngle, sweepAngle, false, arcPaint);
        }
    }

    public void setData(PieSliceData data, float startAngle, float sweepAngle){
        this.value = String.format(Locale.US, "%2.1f", data.getValue());
        this.inscription = data.getTitle();
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        invalidate();
    }
}
