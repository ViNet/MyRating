package com.example.vit.myrating;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vit on 09.05.2015.
 */
public class SharedPreferenceHelper {

    static final String TAG = "myrating";
    static final String CLASS = SharedPreferenceHelper.class.getSimpleName() + ": ";

    static final String PREFERENCE_NAME = "currentUser";
    static final String KEY_SUBJECTS_LIST = "subjectsList";
    static final String KEY_COOKIES = "cookies";


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

    static public void storeCookies(Context context, List<Cookie> cookies ){
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cookies);

        editor.putString(KEY_COOKIES,json);
        editor.apply();
        Log.d(TAG, CLASS + " stored cookies in shared preferences");
    }

    static public List<Cookie> getCookies(Context context){
        // used for retrieving arraylist from json formatted string
        List cookies = null;
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(KEY_COOKIES)){
            String json = preferences.getString(KEY_COOKIES, null);
            Gson gson = new Gson();
            Cookie[] cookiesArray = gson.fromJson(json, Cookie[].class);
            cookies = Arrays.asList(cookiesArray);
            cookies = new ArrayList<Cookie>(cookies);
        }
        Log.d(TAG, CLASS + " loaded cookies from shared preference");
        return cookies;
    }

    static public boolean isCookieExist(Context context){
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.contains(KEY_COOKIES);
    }

    static public void deleteCurrentUser(Context context){
        SharedPreferences preferences = context.
                getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
