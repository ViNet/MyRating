package com.example.vit.myrating;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity implements WorkerFragment.ConnectTaskCallback{

    static final String TAG = "myrating";
    static final String CLASS = "LoginActivity: ";

    WorkerFragment workerFragment;
    static final  String FR_WORKER_TAG = "fragment_worker_tag";

    // views
    EditText etUserName;
    EditText etPassword;
    Button btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, CLASS + "onCreate()");

        if(savedInstanceState == null){
            workerFragment = new WorkerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(workerFragment, FR_WORKER_TAG)
                    .commit();
        }else {
            workerFragment = (WorkerFragment) getSupportFragmentManager().findFragmentByTag(FR_WORKER_TAG);
        }


        initUI();


    }

    public void initUI(){

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSingIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = etUserName.getText().toString();
                final String password = etPassword.getText().toString();

                Log.d(TAG, CLASS + "onSignInClick() userName/Pass = " + userName + "/" + password);
                if(isDataValid()){
                    Log.d(TAG, CLASS + "blasba");
                    //attempt to sign in
                    if(workerFragment != null){
                        workerFragment.signInAttempt(userName, password);
                    } else {
                        Log.d(TAG, CLASS + " NULL ");
                    }

                } else {
                    // show error
                }
            }
        });
    }





    private boolean isDataValid(){
        boolean isUserNameValid = etUserName.getText().toString().isEmpty();
        boolean isPasswordValid = etPassword.getText().toString().isEmpty();

        return !isUserNameValid && !isPasswordValid;
    }

    @Override
    public void onConnectTaskResult(int resultCode) {
        Toast.makeText(this, "Receive result + " + resultCode, Toast.LENGTH_SHORT).show();
/*
        switch (resultCode){
            case WorkerFragment.RESULT_OK:
                startActivity(new Intent(this, MainActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            default:
                break;
        }
        */
    }
}

