package com.space.charts;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

public class ChartUtils {
    // 屏幕分辨率，dp转px
    public static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dpValue + 0.5f);
    }

    //绘制角度计算，角度起始为三点向
    public static Point polarCoordToRect(int angel, int radius){
        int x = angel>180 ? (int)(Math.cos(Math.toRadians(360-angel))*radius) : (int)(Math.cos(Math.toRadians(angel))*radius);
        //textX = textX + getWidth()/2;
        //float textY= midDegree>180 ? (float)Math.sin(Math.toRadians(360-midDegree))*r : (float)Math.sin(Math.toRadians(midDegree))*r;
        int y= (int)(Math.sin(Math.toRadians(angel))*radius);
        //textY=textY + getHeight()/2;
        Log.i("polar to:","x:"+x+",y:"+y);
        return  new Point(x,y);
    }

}
