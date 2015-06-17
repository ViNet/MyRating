package com.example.vit.myrating.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PieChart extends FrameLayout implements View.OnTouchListener {

    static final String TAG = "myrating";
    static final String CLASS = PieChart.class.getSimpleName() + ": ";

    float total = 0;
    float dividerStrokeWidth = 2.0f;
    PieInterior pieInterior;
    List<PieSliceData> data;


    public PieChart(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int side = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(side, side);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        this.setOnTouchListener(this);
    }

    ArrayList<PieSlice> pieSlices;

    public void setData(List<PieSliceData> data) {
        this.data = data;
        pieSlices = new ArrayList<PieSlice>();
        if (data == null) {
            invalidate();
            return;
        }
        total = 0;
        int sectors = 0;
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).getValue() > 0){
                total += data.get(i).getValue();
                sectors++;
            }

        }
        float startAngle = 0, sweepAngle;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getValue() <= 0) {
                continue;
            }

            //create and add divider
            //PieDivider pieDivider = new PieDivider(getContext(), startAngle, dividerStrokeWidth);
            //addView(pieDivider);

            startAngle += dividerStrokeWidth;
            sweepAngle = data.get(i).getValue() * ((360f - sectors * dividerStrokeWidth) / total);
            // create and add slice
            PieSlice pieSlice = new PieSlice(getContext(), i, startAngle, sweepAngle, data.get(i).getColor());

            addView(pieSlice);
            pieSlices.add(pieSlice);

            startAngle += sweepAngle;
        }

        // add interior hole
        pieInterior = new PieInterior(getContext(), this);
        pieInterior.setData(data.get(0), pieSlices.get(0).startAngle, pieSlices.get(0).sweepAngle);
        addView(pieInterior);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            double x0 = pieSlices.get(0).oval.width()/2;
            double y0 = pieSlices.get(0).oval.height()/2;

            double angle =  Math.atan2(y0 - event.getY(), x0 - event.getX());
            double radius = Math.sqrt(event.getX()*event.getX()+event.getY()*event.getY());

            angle = angle * 180/ Math.PI;
            angle += 180;

            //Log.d(TAG, CLASS + "angle = " + angle + "radius = " + radius);
            for(PieSlice slice : pieSlices){
                if(slice.containsAngle(angle)) {
                    pieInterior.setData(data.get(slice.id), slice.startAngle, slice.sweepAngle);
                    break;
                }
            }
        }
        return true;
    }
}
