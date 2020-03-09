package com.hrz.spacecharts.ui.barchart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hrz.spacecharts.R;

public class BarchartFragment extends Fragment {

    private BarchartViewModel barchartViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        barchartViewModel =
                ViewModelProviders.of(this).get(BarchartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_barchart, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        barchartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
