package com.hrz.spacecharts.ui.snailchart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.space.charts.SpiralChartData;
import java.util.ArrayList;
import java.util.List;

public class SnailViewModel extends ViewModel {
    private List<SpiralChartData> data;
    private MutableLiveData<List<SpiralChartData>> liveData;

    // TODO: Implement the ViewModel
    public SnailViewModel(){
        liveData=new MutableLiveData<>();

        data=new ArrayList<>();
        data.add(new SpiralChartData("意大利",21157));
        data.add(new SpiralChartData("伊朗",12927));
        data.add(new SpiralChartData("韩国",8162));
        data.add(new SpiralChartData("西班牙",6394));
        data.add(new SpiralChartData("发过",4983));
        data.add(new SpiralChartData("德国",8382));
        data.add(new SpiralChartData("美国",3249));
        data.add(new SpiralChartData("挪威",1980));
        data.add(new SpiralChartData("荷兰",986));
        data.add(new SpiralChartData("韩国",8162));
        data.add(new SpiralChartData("西班牙",6394));
        data.add(new SpiralChartData("美国",3249));
        data.add(new SpiralChartData("挪威",1980));
        data.add(new SpiralChartData("西班牙",6394));
        data.add(new SpiralChartData("发过",4983));
        data.add(new SpiralChartData("德国",8382));
        data.add(new SpiralChartData("美国",3249));
        data.add(new SpiralChartData("挪威",1980));
        data.add(new SpiralChartData("伊朗",12927));
        data.add(new SpiralChartData("丹麦",654));
        data.add(new SpiralChartData("韩国",8162));
        data.add(new SpiralChartData("西班牙",6394));
        data.add(new SpiralChartData("发过",4983));
        data.add(new SpiralChartData("德国",8382));
        data.add(new SpiralChartData("美国",3249));
        data.add(new SpiralChartData("挪威",1980));

        liveData.setValue(data);
    }

    public MutableLiveData<List<SpiralChartData>> getSnailChartData() {
        return liveData;
    }
}
