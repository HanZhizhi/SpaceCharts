package com.space.charts;

public class SpiralChartData {
    private String name;
    private float value;

    public SpiralChartData(String name, float value){
        this.name=name;
        this.value=value;
    }

    public float getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
