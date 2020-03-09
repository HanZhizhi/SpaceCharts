package com.space.charts;

public class PieDataBean {
    private String msg;      //图上显示的信息
    private float volume;    //量
    private int color;       //绘制颜色

    public PieDataBean(String info, float volume, int color){
        this.msg=info;
        this.volume =volume;
        this.color=color;
    }

    public float getVolume() {
        return volume;
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

    public void setVolume(float volume) {
        this.volume = volume;
    }
}