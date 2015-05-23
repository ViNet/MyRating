package com.example.vit.myrating;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SubjectList.OnListFragmentInteractionListener,
WorkerFragment.ConnectTaskCallback{

    static final String TAG = "myrating";
    static final String CLASS = MainActivity.class.getSimpleName() + ": ";

    boolean twoPane = false;
    // selected item position in subject list (for two-pane layout)
    private int currentPosition = ListView.INVALID_POSITION;

    private String KEY_CURRENT_POSITION = "currentPosition";

    WorkerFragment workerFragment;
    static final  String FR_WORKER_TAG = "fragment_worker_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Log.d(TAG, CLASS + "onCreate()");

        // check if user data exist.
        if(!SharedPreferenceHelper.isCookieExist(getBaseContext())) {
            // delete static fragment
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_list_subject);
            getSupportFragmentManager().beginTransaction().remove(f).commit();

            // if no stored cookie then go to login activity
            startActivity(
                    new Intent(MainActivity.this, LoginActivity.class).
                            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
            );
            finish();
        }

        if(savedInstanceState == null){
            workerFragment = new WorkerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(workerFragment, FR_WORKER_TAG)
                    .commit();
        }else {
            workerFragment = (WorkerFragment) getSupportFragmentManager().findFragmentByTag(FR_WORKER_TAG);
        }

        twoPane = getResources().getBoolean(R.bool.has_two_panes);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, CLASS + "onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, CLASS + "onResume()");
        if(twoPane && currentPosition == ListView.INVALID_POSITION){
            showDetails(0);
        } else if (twoPane && currentPosition != ListView.INVALID_POSITION) {
            showDetails(currentPosition);
        }
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, CLASS + "onSaveInstanceState()");
        // Save selected position
        savedInstanceState.putInt(KEY_CURRENT_POSITION, currentPosition);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, CLASS + "onRestoreInstanceState");
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION);
        } else {
            // Probably initialize members with default values for a new instance
            currentPosition=0;
        }
    }

    private void showDetails(int position){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.subject_detail_container,
                        DetailFragment.newInstance(getSubject(position)))
                .commit();
    }

    private Subject getSubject(int position){
        return ((SubjectList) getSupportFragmentManager().
                findFragmentById(R.id.fragment_list_subject)).getSubject(position);
    }

    @Override
    public void onListItemClick(int position) {
        Log.d(TAG, CLASS + " position = " + position);
        if(twoPane && currentPosition != position){
            // show fragment
            showDetails(position);
        } else{
            //start detail activity
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("subject", getSubject(position));
            startActivity(intent);
        }
        currentPosition = position;
    }

    @Override
    public void onSwipeRefresh(){
        Log.d(TAG, CLASS + "onSwipeRefresh()");

        workerFragment.updateAttempt();

    }

    @Override
    public void onConnectTaskResult(int resultCode) {
        Log.d(TAG, CLASS + "onConnectTaskResult");
        Toast.makeText(this, "result - " + resultCode, Toast.LENGTH_SHORT).show();

        switch (resultCode){
            case WorkerFragment.RESULT_OK:
                Log.d(TAG, CLASS + "Success update");
                ((SubjectList) getSupportFragmentManager().
                        findFragmentById(R.id.fragment_list_subject)).updateData();
                break;
            default:
                break;
        }
    }
}
