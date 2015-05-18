package com.example.vit.myrating;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SubjectList extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match

    static final String TAG = "myrating";
    static final String CLASS = SubjectList.class.getSimpleName() + ": ";

    private OnListFragmentInteractionListener mListener;
    private List<Subject> subjects;
    SubjectsAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListItemClick(int position);
        void onSwipeRefresh();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the list fragment's content view by calling the super method
        final View listFragmentView = inflater.inflate(R.layout.fragment_subject_list, container, false);
        // init swipe refresh layout
        swipeRefreshLayout = (SwipeRefreshLayout)listFragmentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        return listFragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mListener = (OnListFragmentInteractionListener) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    "must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        mListener.onListItemClick(position);
        Log.d(TAG, CLASS + "click on " + position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, CLASS + "onActivityCreated()");

        if(subjects == null){
            subjects =
                    SharedPreferenceHelper.getSubjectList(getActivity().getBaseContext());
            adapter = new SubjectsAdapter(getActivity().getBaseContext(), subjects);
            setListAdapter(adapter);
        }

    }


    @Override
    public void onRefresh() {
        Log.d(TAG, CLASS + "onRefresh()");
        mListener.onSwipeRefresh();
        swipeRefreshLayout.setRefreshing(true);
    }

    public void updateData(){
        swipeRefreshLayout.setRefreshing(false);
        subjects =
                SharedPreferenceHelper.getSubjectList(getActivity().getBaseContext());

        ((SubjectsAdapter) getListAdapter()).setNewData(subjects);

    }


    public Subject getSubject(int position){
        return (Subject) getListAdapter().getItem(position);
    }

    class SubjectsAdapter extends BaseAdapter{

        Context context;
        List<Subject> objects;
        LayoutInflater inflater;

        SubjectsAdapter(Context context, List<Subject> data){
            this.context = context;
            this.objects = data;
            this.inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //num of items
        @Override
        public int getCount() {
            return objects.size();
        }

        // item object by position
        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        //item id by position
        @Override
        public long getItemId(int position) {
            return 0;
        }

        // subject bt position
        Subject getSubject(int position) {
            return ((Subject) getItem(position));
        }

        private void setNewData(List<Subject> subjects){
            objects.clear();
            objects.addAll(subjects);
            this.notifyDataSetChanged();
        }

        // create item view
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null){
                view = inflater.inflate(R.layout.subject_list_item, parent, false);
            }

            Subject subject = getSubject(position);

            ((TextView) view.findViewById(R.id.tvSubjectTitle)).setText(subject.getTitle());
            ((TextView) view.findViewById(R.id.tvSubjectType)).setText(subject.getType());
            ((TextView) view.findViewById(R.id.tvSubjectTotalMark)).setText(subject.getTotalMark());
            ((TextView) view.findViewById(R.id.tvSubjectCompletedModules))
                    .setText(subject.getCompletedModulesNum() + "/" + subject.getModulesNum());
            return view;
        }
    }
}
