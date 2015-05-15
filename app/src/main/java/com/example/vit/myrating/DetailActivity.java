package com.example.vit.myrating;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Subject s = getIntent().getParcelableExtra("subject");
        //if (s != null)
            //s.print();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && isLargeScreen()){
            // close details activity if screen sw >= 600 dp and in portrait orientation
            finish();
            return;
        }


        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            DetailFragment details = DetailFragment.newInstance(s);
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_detail_container, details).commit();
        }
    }

        private boolean isLargeScreen(){
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics ();
            display.getMetrics(outMetrics);
            float density  = getResources().getDisplayMetrics().density;
            float dpHeight = outMetrics.heightPixels / density;
            float dpWidth  = outMetrics.widthPixels / density;

            return (Math.min(dpHeight,dpWidth) >= 600);

    }
}


