package com.hrz.spacecharts;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.space.charts.PieChart;
import com.space.charts.PieData;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private PieChart pieChart;
    private List<PieData> pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pieChart=findViewById(R.id.spacePie);
        pieData=new ArrayList<>();
        pieData.add(new PieData("哈哈哈",0.2f, Color.LTGRAY));
        pieData.add(new PieData("非典",0.1f,Color.CYAN));
        pieData.add(new PieData("否定对方的",0.3f,Color.BLUE));
        pieData.add(new PieData("的深度",0.1f,Color.RED));
        pieData.add(new PieData("二维",0.2f,Color.GREEN));
        pieData.add(new PieData("让他认同",0.1f,Color.YELLOW));
        try {
            pieChart.setData(pieData);
        }
        catch (Exception e){
            Log.i(TAG, "onCreate: "+e.getMessage());
        }


        PieChart.OnPieChartClickListener pieListener=new PieChart.OnPieChartClickListener() {
            @Override
            public void PieChartClickedAt(int position) {
                Toast.makeText(MainActivity.this,pieData.get(position).getMsg(),Toast.LENGTH_SHORT).show();
            }
        };
        pieChart.setOnPieChartClickListener(pieListener);
    }
}
