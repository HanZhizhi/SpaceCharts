package com.hrz.spacecharts.ui.piechart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hrz.spacecharts.R;
import com.space.charts.PieChart;
import com.space.charts.PieDataBean;

import java.util.List;

public class PiechartFragment extends Fragment {
    private PieChart pieChart;


    private PiechartViewModel piechartViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        piechartViewModel =
                ViewModelProviders.of(this).get(PiechartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_piechart, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        piechartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        pieChart=root.findViewById(R.id.piechart_show);
        piechartViewModel.getPieDatumBeans().observe(getViewLifecycleOwner(), new Observer<List<PieDataBean>>() {
            @Override
            public void onChanged(List<PieDataBean> pieDatumBeans) {
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
