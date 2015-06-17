package com.example.vit.myrating.view;

/**
 * Created by Vit on 16.06.2015.
 */
public class PieSliceData {

    private String inscription;
    private float value;
    private int color;

    public PieSliceData(String label, float value, int color){
        this.inscription = label;
        this.value = value;
        this.color = color;
    }

    public String getTitle(){
        return this.inscription;
    }

    public float getValue(){
        return this.value;
    }

    public int getColor(){
        return this.color;
    }
}
