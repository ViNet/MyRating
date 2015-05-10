package com.example.vit.myrating;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Subject s = getIntent().getParcelableExtra("subject");
        if(s != null)
            s.print();

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            DetailFragment details = DetailFragment.newInstance(s);
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_detail_container, details).commit();
        }
    }
}


