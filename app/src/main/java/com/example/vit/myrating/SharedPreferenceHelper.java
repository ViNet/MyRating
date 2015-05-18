package com.example.vit.myrating;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Vit on 09.05.2015.
 */
public class SharedPreferenceHelper {

    static final String TAG = "myrating";
    static final String CLASS = SharedPreferenceHelper.class.getSimpleName() + ": ";

    static final String PREFERENCE_NAME = "currentUser";
    static final String KEY_SUBJECTS_LIST = "subjectsList";
    static final String KEY_COOKIE = "cookie";

    static final String COOKIE_NAME = "cookie_name";
    static final String COOKIE_VALUE = "cookie_value";
    static final String COOKIE_DOMAIN = "cookie_domain";
    static final String COOKIE_PATH = "cookie_path";


    static public void storeSubjectList(Context context, List data){
        // used for store arrayList in json format
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);

        editor.putString(KEY_SUBJECTS_LIST,json);
        editor.apply();
        Log.d(TAG, CLASS + " saved in shared preference");
    }

    static public List<Subject> getSubjectList(Context context){
        // used for retrieving arraylist from json formatted string
        List subjects = null;
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(KEY_SUBJECTS_LIST)){
            String json = preferences.getString(KEY_SUBJECTS_LIST, null);
            Gson gson = new Gson();
            Subject[] subjectItems = gson.fromJson(json, Subject[].class);
            subjects = Arrays.asList(subjectItems);
            subjects = new ArrayList<Subject>(subjects);
        }
        Log.d(TAG, CLASS + " load in shared preference");
        return subjects;
    }

    static public void storeCookies(Context context, Cookie cookie){
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(COOKIE_NAME, cookie.getName());
            jsonObject.put(COOKIE_VALUE, cookie.getValue());
            jsonObject.put(COOKIE_DOMAIN, cookie.getDomain());
            jsonObject.put(COOKIE_PATH, cookie.getPath());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        String cookieStr = cookie.getName() + " " + cookie.getValue() +
                " " + cookie.getDomain() + " " + cookie.getPath() + " " + cookie.getExpiryDate();
                */

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_COOKIE,jsonObject.toString());
        editor.apply();
        Log.d(TAG, CLASS + " stored cookies in shared preferences");
    }

    static public Cookie getCookies(Context context){
        // used for retrieving arraylist from json formatted string

        Log.d(TAG, CLASS + "1");
        BasicClientCookie cookie = null;
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String name = "", value = "", domain = "", path = "";
        Log.d(TAG, CLASS + "2");
        if(preferences.contains(KEY_COOKIE)){
            String jsonCookie = preferences.getString(KEY_COOKIE, "0");
            try {
                JSONObject jsonObject = new JSONObject(jsonCookie);
                name = jsonObject.getString(COOKIE_NAME);
                value = jsonObject.getString((COOKIE_VALUE));
                domain = jsonObject.getString(COOKIE_DOMAIN);
                path = jsonObject.getString(COOKIE_PATH);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cookie = new BasicClientCookie(name, value);
            cookie.setDomain(domain);
            cookie.setPath(path);
        }

        Log.d(TAG, CLASS + " loaded cookies from shared preference");
        return cookie;
    }

    static public boolean isCookieExist(Context context){
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.contains(KEY_COOKIE);
    }

    static public void deleteCurrentUser(Context context){
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
