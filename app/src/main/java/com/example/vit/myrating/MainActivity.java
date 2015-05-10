package com.example.vit.myrating;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


public class MainActivity extends ActionBarActivity implements SubjectList.OnListFragmentInteractionListener {

    static final String TAG = "myrating";
    static final String CLASS = MainActivity.class.getSimpleName() + ": ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, CLASS + " onOptionsItemSelected()");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //delete user and show login activity
            Log.d(TAG, CLASS + " logout");
            SharedPreferenceHelper.deleteCurrentUser(getBaseContext());
            startActivity(
                    new Intent(MainActivity.this, LoginActivity.class).
                            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
            );
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Subject subject) {
        Log.d(TAG, CLASS + " pos= " + subject.getTitle());
        //start detail activity
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("subject", subject);
        startActivity(intent);

    }
}
