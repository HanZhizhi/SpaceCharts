package com.hrz.spacecharts.ui.piechart;

import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.space.charts.PieChartData;

import java.util.ArrayList;
import java.util.List;

public class PiechartViewModel extends ViewModel {
    private List<PieChartData> pieDatumBeans;

    private MutableLiveData<String> mText;
    private MutableLiveData<List<PieChartData>> piechartData;

    public PiechartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");

        piechartData=new MutableLiveData<>();


        pieDatumBeans =new ArrayList<>();
        pieDatumBeans.add(new PieChartData("哈哈哈",2f, Color.LTGRAY));
        pieDatumBeans.add(new PieChartData("非典",1f,Color.CYAN));
        pieDatumBeans.add(new PieChartData("否定对方的",3f,Color.BLUE));
        pieDatumBeans.add(new PieChartData("的深度",4f,Color.RED));
        pieDatumBeans.add(new PieChartData("二维",2f,Color.GREEN));
        pieDatumBeans.add(new PieChartData("让他认同",1f,Color.YELLOW));

        piechartData.setValue(pieDatumBeans);
        /*try {
            pieChart.setData(pieData);
        }
        catch (Exception e){
            Log.i("TAG", "onCreate: "+e.getMessage());
        }*/
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<PieChartData>> getPieDatumBeans() {
        return piechartData;
    }
}