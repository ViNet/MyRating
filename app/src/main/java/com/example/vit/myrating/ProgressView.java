package com.example.vit.myrating;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Vit on 09.06.2015.
 */
public class ProgressView extends View{

    static final String TAG = "myrating";
    static final String CLASS = ProgressView.class.getSimpleName() + ": ";

    Paint linePaint;
    Paint textPaint;
    Paint progressPaint;

    float progressLine = 0.0f;

    int indent = 15;
    int scaleStrokeWidth = 0;
    int graphHeight = 0;
    int numHeight = 0;
    int textMargin = 0;
    int textSize = 0;
    int progressStrokeWidth = 0;

    public ProgressView(Context context) {
        super(context);
        initTools();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTools();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        Log.d(TAG, CLASS + "onDraw canvas w=" + canvas.getWidth() + " h=" + canvas.getHeight());

        calculateDimens(canvas.getHeight(), canvas.getWidth());
        drawScale(canvas);
        drawProgressLine(canvas);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setProgress(int value){
        Log.d(TAG, CLASS + "setProgress() value = " + value);
        this.progressLine = value/100.0f;
        requestLayout();
        invalidate();
    }

    private void initTools(){
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);

        progressPaint = new Paint();
        progressPaint.setColor(getResources().getColor(R.color.orange));
    }

    private void drawScale(Canvas canvas){
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        linePaint.setStrokeWidth(scaleStrokeWidth);
        textPaint.setTextSize(textSize);

        // horizontal line
        canvas.drawLine(indent, height - scaleStrokeWidth / 2, width - indent, height - scaleStrokeWidth / 2, linePaint);

        // vertical serifs
        //start
        canvas.drawLine(indent, height, indent, graphHeight, linePaint);
        // end
        canvas.drawLine(width - indent, height, width - indent, graphHeight, linePaint);

        // start text value
        canvas.drawText("0", indent, numHeight, textPaint);
        // end text value
        canvas.drawText("100", width - indent, numHeight, textPaint);
    }

    private void drawProgressLine(Canvas canvas){
        int maxWidth = canvas.getWidth() - 2*indent - 2*scaleStrokeWidth;
        int height = canvas.getHeight();

        progressPaint.setStrokeWidth(graphHeight/2);

        canvas.drawLine(indent + scaleStrokeWidth
                , height - scaleStrokeWidth - graphHeight/3
                , maxWidth * this.progressLine
                , height - scaleStrokeWidth - graphHeight/3
                , progressPaint);
    }

    private void calculateDimens(int height, int width){
        this.scaleStrokeWidth = 2;
        this.graphHeight = (height * 3) / 5;
        this.numHeight = (height * 2) / 5;
        this.textMargin = 3;
        this.textSize = numHeight - textMargin;
    }
}
