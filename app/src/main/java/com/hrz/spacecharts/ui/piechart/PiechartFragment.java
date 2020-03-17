package com.hrz.spacecharts.ui.piechart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hrz.spacecharts.R;
import com.space.charts.PieChart;
import com.space.charts.PieChartData;

import java.util.List;

public class PiechartFragment extends Fragment {
    private PieChart pieChart;

    private PiechartViewModel piechartViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        piechartViewModel =
                ViewModelProviders.of(this).get(PiechartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_piechart, container, false);

        pieChart=root.findViewById(R.id.piechart_show);
        piechartViewModel.getPieDatumBeans().observe(getViewLifecycleOwner(), new Observer<List<PieChartData>>() {
            @Override
            public void onChanged(List<PieChartData> pieDatumBeans) {
                pieChart.setData(pieDatumBeans);
            }
        });

        PieChart.OnPieChartClickListener pieListener=new PieChart.OnPieChartClickListener() {
            @Override
            public void PieChartClickedAt(int position) {
                Toast.makeText(getActivity(),piechartViewModel.getPieDatumBeans().getValue().get(position).getMsg(),Toast.LENGTH_SHORT).show();
            }
        };
        pieChart.setOnPieChartClickListener(pieListener);

        return root;
    }
}
