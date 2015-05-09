package com.example.vit.myrating;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    TextView tvTest;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        tvTest = (TextView) rootView.findViewById(R.id.tvTest);

        SharedPreferences sharedPreferences = getActivity().
                getSharedPreferences("currentUser", Context.MODE_PRIVATE);
        String page = sharedPreferences.getString("page", "error");

        tvTest.setText(page);

        return rootView;
    }



}
