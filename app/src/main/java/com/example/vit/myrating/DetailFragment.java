package com.example.vit.myrating;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vit.myrating.view.PieSliceData;
import com.example.vit.myrating.view.PieChart;
import com.example.vit.myrating.view.ProgressView;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    Subject subject;

    final static String TAG = "myrating";
    final static String CLASS = DetailFragment.class.getSimpleName() + ": ";

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
        Log.d(TAG, CLASS + " onCreate() ");
        if (!getArguments().isEmpty())
            subject = getArguments().getParcelable("subject");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_layout, container, false);
        ((TextView) rootView.findViewById(R.id.tvDetailTitle)).setText(subject.getTitle());
        ((TextView) rootView.findViewById(R.id.tvDetailType)).setText(subject.getType());
        ListView lvModules = (ListView) rootView.findViewById(R.id.lvModules);

        PieChart pieChart = (PieChart) rootView.findViewById(R.id.pieChart);

        List<PieSliceData> pieChartData = new ArrayList<PieSliceData>(3);
        pieChartData.add(
                new PieSliceData(getString(R.string.gained), subject.getTotalContributionValue(),
                        getResources().getColor(R.color.light_blue)));
        pieChartData.add(
                new PieSliceData(getString(R.string.available), subject.getAvailableContribution(),
                        getResources().getColor(R.color.xlight_blue)));
        pieChartData.add(
                new PieSliceData(getString(R.string.wasted), subject.getWastedContribution(),
                        Color.parseColor("#FF4D4D4D")));

        pieChart.setData(pieChartData);


        ModulesAdapter adapter = new ModulesAdapter(getActivity(), subject.getModules());
        lvModules.setAdapter(adapter);
        return rootView;
    }

    public class ModulesAdapter extends BaseAdapter {

        Context context;
        List<Subject.Module> modules;
        LayoutInflater inflater;

        final static int TYPE_COMPLETED = 0;
        final static int TYPE_UNCOMPLETED = 1;

        public ModulesAdapter(Context context, List<Subject.Module> data) {
            this.context = context;
            this.modules = data;
            this.inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return modules.size();
        }

        @Override
        public Object getItem(int position) {
            return modules.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (!modules.get(position).mark.isEmpty()) {
                return TYPE_COMPLETED;
            } else {
                return TYPE_UNCOMPLETED;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            int type = getItemViewType(position);
            if (view == null) {
                Subject.Module module = getModule(position);;
                switch (type) {
                    case TYPE_COMPLETED:
                        view = inflater.inflate(R.layout.module_list_item, parent, false);

                        ProgressView pvModuleMark = (ProgressView) view.findViewById(R.id.pvModuleMark);
                        pvModuleMark.setProgress(Integer.parseInt(module.mark));

                        ((TextView) view.findViewById(R.id.tvModuleNum))
                                .setText(getString(R.string.module_x_num, position + 1));
                        ((TextView) view.findViewById(R.id.tvModuleDate)).setText(module.date);
                        ((TextView) view.findViewById(R.id.tvModuleMark)).setText(module.mark);
                        ((TextView) view.findViewById(R.id.tvModulePercentage))
                                .setText(getString(R.string.module_x_percentage, module.percentage));
                        ((TextView) view.findViewById(R.id.tvModuleContribution))
                                .setText(getString(R.string.module_x_contribution, module.getContribution()));
                        break;
                    case TYPE_UNCOMPLETED:
                        view = inflater.inflate(R.layout.module_list_item_uncompleted, parent, false);

                        ((TextView) view.findViewById(R.id.tvModuleNum))
                                .setText(getString(R.string.module_x_num, position + 1));
                        ((TextView) view.findViewById(R.id.tvModuleDate)).setText(module.date);
                        ((TextView) view.findViewById(R.id.tvModulePercentage))
                                .setText(getString(R.string.module_percentage, module.percentage));
                        break;
                    default:
                        Log.d(TAG, CLASS + "unknown layout type");
                        break;
                }

            }

            return view;
        }

        private Subject.Module getModule(int position) {
            return modules.get(position);
        }
    }
}
