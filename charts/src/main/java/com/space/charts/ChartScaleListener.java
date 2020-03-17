package com.space.charts;

import android.util.Log;
import android.view.ScaleGestureDetector;

public class ChartScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = "ChartScaleListener";
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.i(TAG, "onScale: "+"sacling");
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
