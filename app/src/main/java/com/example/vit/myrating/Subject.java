package com.example.vit.myrating;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vit on 09.05.2015.
 * Class used for saved parsed html data in comfortable structure
 */
public class Subject {

    static final String TAG = "myrating";
    static final String CLASS = Subject.class.getSimpleName() + ": ";

    private String title;
    private String type;
    private String totalMark;
    private int semester;
    private List<Module> modules;
    boolean isCompleted;

    Subject(String name, String type, String totalMark, int semester, List<Module> modules){
        this.title = name;
        this.type = type;
        this.totalMark = totalMark;
        this.semester = semester;
        this.modules = modules;
        this.isCompleted = this.isCompleted();
    }


    private boolean isCompleted(){
        int completedModules = 0;
        for(int i=0; i<modules.size(); i++){
            if(!modules.get(i).mark.isEmpty())
                completedModules++;
        }
        return (completedModules == modules.size());
    }

    public String getTitle(){
        return this.title;
    }

    // for debug
    public void print(){
        Log.d(TAG, CLASS + "title: " + this.title);
        Log.d(TAG, CLASS + "type: " + this.type);
        Log.d(TAG, CLASS + "total: " + this.totalMark);
        Log.d(TAG, CLASS + "semester: " + this.semester);
        Log.d(TAG, CLASS + "completed: " + this.isCompleted);
        for(int i=0; i<modules.size(); i++){
            modules.get(i).print();
        }
    }

    public static class Module{
        String percentage;
        String date;
        String mark;

        public Module(String percentage, String date, String mark){
            this.percentage = percentage;
            this.date = date;
            this.mark = mark;
        }

        //for debug
        public void print(){
            Log.d(TAG, CLASS + "p=" + percentage + ", d=" + date + ", m=" + mark);
        }
    }
}
