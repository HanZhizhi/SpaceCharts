package com.hrz.spacecharts.ui.snailchart;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hrz.spacecharts.R;
import com.space.charts.SpiralChart;
import com.space.charts.SpiralChartData;

import java.util.List;

public class SnailFragment extends Fragment {

    private SpiralChart snailChart;

    private SnailViewModel mViewModel;

    public static SnailFragment newInstance() {
        return new SnailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_snail, container, false);
        snailChart=root.findViewById(R.id.snail_chart);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SnailViewModel.class);
        // TODO: Use the ViewMod.setVisibility(View.VISIBLE);
        mViewModel.getSnailChartData().observe(getViewLifecycleOwner(), new Observer<List<SpiralChartData>>() {
            @Override
            public void onChanged(List<SpiralChartData> snailChartData) {
                snailChart.setData(snailChartData);
            }
        });
    }

}
