package com.example.vit.myrating;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vit on 09.05.2015.
 * Class used for saved parsed html data in comfortable structure
 */
public class Subject implements Parcelable {

    static final String TAG = "myrating";
    static final String CLASS = Subject.class.getSimpleName() + ": ";

    private String title;
    private String type;
    private String totalMark;
    private int semester;
    private List<Module> modules;
    private boolean isCompleted;

    Subject(String name, String type, String totalMark, int semester, List<Module> modules){
        this.title = name;
        this.type = type;
        this.totalMark = totalMark;
        this.semester = semester;
        this.modules = modules;
        this.isCompleted = this.checkComplete();
    }


    private boolean checkComplete(){
        return (getCompletedModulesNum() == modules.size());
    }

    public boolean isCompleted(){
        return isCompleted;
    }

    public String getTitle(){
        return this.title;
    }

    public String getType(){
        return this.type;
    }

    public String getTotalMark(){
        return this.totalMark;
    }

    public int getModulesNum(){
        return modules.size();
    }

    public List<Module> getModules(){
        return this.modules;
    }

    public int getCompletedModulesNum(){
        int completedModules = 0;
        for(int i=0; i<modules.size(); i++){
            if(!modules.get(i).mark.isEmpty())
                completedModules++;
        }
        return completedModules;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeInt(this.semester);
        dest.writeList(this.modules);
        dest.writeInt((this.isCompleted ? 1 : 0));
        dest.writeString(totalMark);
    }

    private Subject(Parcel in){
        this.title = in.readString();
        this.type = in.readString();
        this.semester = in.readInt();
        this.modules = new ArrayList<Module>();
        this.modules = in.readArrayList(Module.class.getClassLoader());
        this.isCompleted = (in.readInt() == 1);
        this.totalMark = in.readString();
    }

    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {

        @Override
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public static class Module implements Parcelable{
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

        public String getContribution(){
            return String.format("%.1f" , getContributionValue());
        }

        public float getContributionValue(){
            if (this.mark.isEmpty() || this.percentage.isEmpty()){
                return 0;
            }
            int markValue = Integer.parseInt(this.mark);
            int percentageValue = Integer.parseInt(this.percentage);
            return (markValue * percentageValue)/100.0f;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(percentage);
            dest.writeString(date);
            dest.writeString(mark);
        }

        public static final Parcelable.Creator<Module> CREATOR = new Parcelable.Creator<Module>() {

            @Override
            public Module createFromParcel(Parcel source) {
                return new Module(source);
            }

            @Override
            public Module[] newArray(int size) {
                return new Module[size];
            }
        };

        Module(Parcel in){
            percentage = in.readString();
            date = in.readString();
            mark = in.readString();
        }


    }
}
