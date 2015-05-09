package com.example.vit.myrating;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

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


    static public void saveToSharedPreference(Context context, List data){
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

    static public List<Subject> getFromSharedPreference(Context context){
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
}
