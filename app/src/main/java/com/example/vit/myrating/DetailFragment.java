package com.example.vit.myrating;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    Subject subject;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(Subject subject) {
        DetailFragment f = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("subject", subject);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!getArguments().isEmpty())
            subject = getArguments().getParcelable("subject");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        ((TextView) rootView.findViewById(R.id.tvDetailTitle)).setText(subject.getTitle());
        ((TextView) rootView.findViewById(R.id.tvDetailType)).setText(subject.getType());
        ((TextView) rootView.findViewById(R.id.tvDetailTotalMark)).setText(subject.getTotalMark());

        return rootView;
    }
}
