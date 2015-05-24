package com.example.vit.myrating;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vit on 24.05.2015.
 */
public class MainListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    static final String TAG = "myrating";
    static final String CLASS = MainListFragment.class.getSimpleName() + ": ";

    private OnMainListFragmentInteractionListener mListener;
    private List<Subject> subjects;

    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvMainList;
    LinearLayoutManager llm;

    public interface OnMainListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListItemClick(int position);
        void onSwipeRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the list fragment's content view by calling the super method
        final View listFragmentView = inflater.inflate(R.layout.fragment_main_list, container, false);
        // init swipe refresh layout
        swipeRefreshLayout = (SwipeRefreshLayout) listFragmentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        // find recyclerView
        rvMainList = (RecyclerView) listFragmentView.findViewById(R.id.rvMainList);
        // init layout manager for recycle view
        llm = new LinearLayoutManager(getActivity());

        // if subjects == null then get data from shared preferences
        if(subjects == null) {
            subjects =
                    SharedPreferenceHelper.getSubjectList(getActivity().getBaseContext());
        }

        // create adapter
        RecycleAdapter mAdapter = new RecycleAdapter(subjects);
        //set linear layout manager
        rvMainList.setLayoutManager(llm);
        //set adapter
        rvMainList.setAdapter(mAdapter);
        // set item animator to DefaultAnimator
        rvMainList.setItemAnimator(new DefaultItemAnimator());

        return listFragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMainListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    "must implement OnMainListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        mListener.onSwipeRefresh();
    }
    public void updateData(){
        swipeRefreshLayout.setRefreshing(false);

        subjects =
                SharedPreferenceHelper.getSubjectList(getActivity().getBaseContext());
        ((RecycleAdapter) rvMainList.getAdapter()).setNewData(subjects);
    }

    public Subject getSubject(int position){
        return (Subject) subjects.get(position);
    }

    public class RecycleAdapter extends RecyclerView.Adapter<ViewHolder>{

        List<Subject> subjects;

        RecycleAdapter(List<Subject> subjects){
            this.subjects = subjects;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
            // set on click listener
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.subjectTitle.setText(subjects.get(position).getTitle());
            holder.subjectType.setText(subjects.get(position).getType());
        }

        @Override
        public int getItemCount() {
            return subjects.size();
        }

        public Subject getSubject(int position){
            return subjects.get(position);
        }

        public void setNewData(List<Subject> subjects){
            this.subjects.clear();
            this.subjects.addAll(subjects);
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView listItemIcon;
        TextView subjectTitle;
        TextView subjectType;

        ViewHolder(View itemView) {
            super(itemView);
            listItemIcon = (ImageView)itemView.findViewById(R.id.list_item_icon);
            subjectTitle = (TextView)itemView.findViewById(R.id.tvSubjectTitle);
            subjectType = (TextView)itemView.findViewById(R.id.tvSubjectType);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onListItemClick(getAdapterPosition());
        }
    }
}
