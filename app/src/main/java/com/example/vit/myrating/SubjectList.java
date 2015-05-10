package com.example.vit.myrating;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class SubjectList extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match

    static final String TAG = "myrating";
    static final String CLASS = SubjectList.class.getSimpleName() + ": ";

    private OnListFragmentInteractionListener mListener;
    private List<Subject> subjects;

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Subject subject);
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

        mListener.onFragmentInteraction((Subject)getListAdapter().getItem(position));
        Log.d(TAG, CLASS + "click on " + position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        subjects =
                SharedPreferenceHelper.getSubjectList(getActivity().getBaseContext());
        SubjectsAdapter adapter = new SubjectsAdapter(getActivity().getBaseContext(), subjects);
        setListAdapter(adapter);
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
