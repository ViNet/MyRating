package com.example.vit.myrating;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
    Fragment without layout used for get data from
    tanet.edu.mod.ua
 */
public class WorkerFragment extends Fragment {

    final static String TAG = "myrating";
    final static String CLASS = "WorkerFragment: ";

    // results codes returned by signInAttempt
    static final int RESULT_NO_INTERNET_ACCESS = 0;
    static final int RESULT_OK = 1;
    static final int RESULT_WRONG_PASS = 2;
    static final int RESULT_SERVER_NOT_RESPOND = 3;
    static final int RESULT_ERROR = 4;

    ConnectTaskCallback callback;

    private boolean isWorking = false;


    public interface ConnectTaskCallback{
        void onConnectTaskResult(int resultCode);
    }

    public WorkerFragment() {
        // Required empty public constructor
        Log.d(TAG, CLASS + " in constructor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, CLASS + "onCreate");
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            callback = (ConnectTaskCallback) activity;
        } catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + "must implement ConnectTaskCallback");
        }
    }

    public void signInAttempt(String login, String pass){

        if(!isWorking){
            if(isNetworkConnected()){
                new ConnectTask().execute(login, pass);
                isWorking = true;
            } else {
                // send result NO_INTERNET_ACCESS
                callback.onConnectTaskResult(RESULT_NO_INTERNET_ACCESS);
            }

        } else {
            Log.d(TAG, CLASS + "workerFragment busy now");
        }
    }

    public boolean isNetworkConnected(){

        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    public class ConnectTask extends AsyncTask<String, Void, Integer>{

        static final String URL = "http://mod.tanet.edu.te.ua/site/login";
        static final String URL2 = "http://mod.tanet.edu.te.ua/ratings/index";
        static final String USERNAME_INPUT_ID = "LoginForm[login]";
        static final String PASS_INPUT_ID = "LoginForm[password]";
        static final String REMEMBER_INPUT_ID = "LoginForm[rememberMe]";
        static final String SUBMIT_INPUT_ID = "yt0";

        @Override
        protected Integer doInBackground(String... params) {
            Log.d(TAG, CLASS + " doInBackground()");

            Integer operationResult = null;

            if(params != null){
                // login attempt
                // input arguguments are login and pass
                operationResult = executePostRequest(params[0], params[1]);
            } else {
                // update request
            }


            return operationResult;
        }

        @Override
        protected void onPostExecute(Integer resultCode) {
            super.onPostExecute(resultCode);
            isWorking=false;
            callback.onConnectTaskResult(resultCode);
        }

        // used for login
        private int executePostRequest(String login, String pass){
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);
            // Create a local instance of cookie store
            CookieStore cookieStore = new BasicCookieStore();
            HttpContext context = new BasicHttpContext();
            // Bind custom cookie store to the local context
            context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair(USERNAME_INPUT_ID, login));
                nameValuePairs.add(new BasicNameValuePair(PASS_INPUT_ID, pass));
                nameValuePairs.add(new BasicNameValuePair(REMEMBER_INPUT_ID, "0"));
                nameValuePairs.add(new BasicNameValuePair(SUBMIT_INPUT_ID, "”‚≥ÈÚË"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost, context);

                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    // status ok
                    // get curent URL
                    HttpUriRequest currentReq = (HttpUriRequest) context
                            .getAttribute(ExecutionContext.HTTP_REQUEST);
                    HttpHost currentHost = (HttpHost) context
                            .getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                    String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq
                            .getURI().toString() : (currentHost.toURI() + currentReq
                            .getURI());

                    //check if sign is success, if page was redirected than sign in success
                    if(!currentUrl.equals(URL)){
                        // sign in success
                        String page = EntityUtils.toString(response.getEntity());
                        Log.d(TAG, CLASS + "succes signIn");
                        SharedPreferenceHelper.storeCookies(getActivity(), cookieStore.getCookies());
                        SharedPreferenceHelper.storeSubjectList(getActivity(), ParserUtils.parsePage(page));
                        return RESULT_OK;
                    } else {
                        // wrong login or pass
                        Log.d(TAG, CLASS + "Wrong login or pass");
                        return RESULT_WRONG_PASS;
                    }
                } else {
                    Log.d(TAG, CLASS + "server not respond");
                    return RESULT_SERVER_NOT_RESPOND;
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } finally {
                httpclient.getConnectionManager().closeExpiredConnections();
            }
            return RESULT_ERROR;
        }

    }

}
