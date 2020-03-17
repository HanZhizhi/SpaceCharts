package com.space.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpiralChart extends View{
    private static final String TAG = "SnailChart";
    private int bgColor,colorStyle,viewWidth,viewHeight;
    private boolean verticalMode;        //横纵方向
    private Context context;
    private Paint paint;
    private TextPaint textPaint;
    private List<SpiralChartData> data;
    private float angelPerItem;

    private ScaleGestureDetector scaleGestureDetector;

    public SpiralChart(Context context) {
        this(context,null);
    }

    public SpiralChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SpiralChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initAttr(attrs);
        initPaint();
        scaleGestureDetector=new ScaleGestureDetector(context,new ChartScaleListener());
    }

    private void initAttr(AttributeSet attributeSet){
        TypedArray ta=context.obtainStyledAttributes(attributeSet,R.styleable.SpiralChart);
        bgColor=ta.getColor(R.styleable.SpiralChart_background_color, Color.WHITE);
        colorStyle=ta.getInteger(R.styleable.SpiralChart_style,1);
    }

    private void initPaint()
    {
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        textPaint=new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAlpha(90);
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    public void setData(List<SpiralChartData> data){
        if(data==null || data.size()==0)
            return;
        this.data=data;
        Comparator<SpiralChartData> comparator=new Comparator<SpiralChartData>() {
            @Override
            public int compare(SpiralChartData o1, SpiralChartData o2) {
                if(o1.getValue()<o2.getValue()) return 1;
                else return -1;
            }
        };
        Collections.sort(this.data,comparator);
        angelPerItem = (float)360.0 / data.size();
        Log.i(TAG, "setData: angelPerItem--"+angelPerItem+",size:"+data.size());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(viewWidth,viewHeight);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);//宽的测量大小，模式
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);//高的测量大小，模式
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int w = widthSpecSize;   //定义测量宽，高(不包含测量模式),并设置默认值
        int h = heightSpecSize;

        //处理wrap_content的几种特殊情况
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            w = 1400;  //单位是px
            h = 700;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            //只要宽度布局参数为wrap_content， 宽度给固定值200dp(处理方式不一，按照需求来)
            h = heightSpecSize;
            w= h/2;
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            w = widthSpecSize;
            h = w*2;
        }

        viewHeight=h;
        viewWidth=w;
        verticalMode = viewHeight >= viewWidth;
        Log.i(TAG, "onMeasure: height"+viewHeight+",width:"+viewWidth);

        setMeasuredDimension(w,h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Bitmap bmp=Bitmap.createBitmap(verticalMode?viewHeight:viewWidth,verticalMode?viewWidth:viewHeight, Bitmap.Config.ARGB_8888);

        // 绘制背景
        paint.setColor(bgColor);
        RectF backRect=new RectF(0,0,viewWidth,viewHeight);
        canvas.drawRect(backRect,paint);

        if (data == null){
            int centerX=getWidth()/2, centerY=getHeight()/2;
            canvas.drawText("SpiralChart \n data not set !!",centerX,centerY,textPaint);
        }
        else {
            textPaint.setTextSize(35);
            if (verticalMode){  // View高大于宽，纵向绘制
                canvas.translate(200,300);
                drawSpiral(canvas,270,50,20);
            }else{      // view宽大于高，横向绘制
                canvas.translate(300,200);
                drawSpiral(canvas,0,50,20);
            }
        }
    }

    /*
     * 绘制起始角度在canvas坐标中的角度，垂直图为270，水平为0；
     * 最小的半径长度
     * 半径增长
     */
    private void drawSpiral(Canvas canvas, int startDegreeOfCanvas, int baseRadius, int stepRadius){
        int centerX=getWidth()/2, centerY=getHeight()/2;
        String colors[]=ChartColors.getColors(colorStyle);
        int numColors=colors.length;

        for (int i=0;i<data.size();++i){
            float centerDegree=i*angelPerItem + angelPerItem/2;
            float drawStart=(startDegreeOfCanvas+i*angelPerItem)%360;
            int drawCenter=(int)(drawStart+angelPerItem/2);
            baseRadius += stepRadius;// + stepRadius*centerDegree/360;
            RectF r2=new RectF(centerX-baseRadius,centerY-baseRadius,centerX+baseRadius,centerY+baseRadius);
            paint.setColor(Color.parseColor(colors[i%numColors]));
            canvas.drawArc(r2,drawStart,angelPerItem+0.5f,true,paint);

            Point textCenter=ChartUtils.polarCoordToRect(drawCenter,baseRadius+20);
            textCenter.x += centerX;
            textCenter.y += centerY;
            int textRot=drawCenter;

            Log.i(TAG, "onDraw: added text--i:"+i+",,base:"+baseRadius+",,x:"+textCenter.x+",y:"+textCenter.y+",midDegree:"+centerDegree+",textRotation:"+textRot);
            canvas.rotate(textRot,textCenter.x,textCenter.y);
            canvas.drawText(data.get(i).getName()+"\n"+data.get(i).getValue(),textCenter.x,textCenter.y,textPaint);
            canvas.rotate(-textRot,textCenter.x,textCenter.y);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if(event.getPointerCount() > 1){

        }*/
        //return super.onTouchEvent(event);
        return scaleGestureDetector.onTouchEvent(event);
    }
}
