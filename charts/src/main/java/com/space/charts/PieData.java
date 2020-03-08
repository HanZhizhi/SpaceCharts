package com.space.charts;

public class PieData {
    private String msg;
    private float prop;
    private int color;

    public PieData(String info, float proportion,int color){
        this.msg=info;
        this.prop=proportion;
        this.color=color;
    }

    public float getProp() {
        return prop;
    }

    public String getMsg() {
        return msg;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setProp(float prop) {
        this.prop = prop;
    }
}