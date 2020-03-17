package com.space.charts;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PieChart extends View {
    private static final String TAG="PieChart";
    private int width,height,bgColor;
    private Context context;
    private Paint paint;
    private TextPaint textPaint;
    private Rect rectBackground;
    private boolean hollow;

    //动画相关
    private ValueAnimator vAnimator;
    private int startDegree=0;     //起始角度

    // touchEvent相关，实现点击
    private OnPieChartClickListener clickListener;
    private boolean clickMoved=false;

    //数据相关
    private List<PieChartData> data;     //通过setData赋值，为空时不可点击
    private float[] proportions;
    private int[] degreeParts;          //绘制的每个扇形角度终点

    public PieChart(Context context) {
        this(context,null);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initAttr(attrs);
        initPaint();
        //setClickable(false);
    }

    private void initAttr(AttributeSet attributeSet){
        TypedArray ta=context.obtainStyledAttributes(attributeSet,R.styleable.PieChart);
        width=(int)ta.getDimension(R.styleable.PieChart_width,ChartUtils.dp2px(context,400));
        height=(int)ta.getDimension(R.styleable.PieChart_height,ChartUtils.dp2px(context,200));
        bgColor=ta.getColor(R.styleable.PieChart_background_color,Color.WHITE);
        hollow=ta.getBoolean(R.styleable.PieChart_hollow,false);
    }

    private void initPaint()
    {
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        textPaint=new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAlpha(90);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<PieChartData> d){
        float totalVlome=0;
        for(PieChartData p:d){
            totalVlome+=p.getVolume();
        }

        this.data=d;
        degreeParts=new int[data.size()];
        proportions=new float[data.size()];
        int base=0;        //角度划分的起始点，仅用于计算，不是绘制的零度
        for(int i=0;i<data.size();++i){
            proportions[i]=data.get(i).getVolume() / totalVlome;
            degreeParts[i]=base+(int)(proportions[i] * 360);
            base=degreeParts[i];
            Log.i(TAG, "setData: propor--"+proportions[i]+",终点角度："+degreeParts[i]);
        }
        Log.i(TAG, "setData: dataLen--"+data.size());
        //TODO:优化计算
        if (degreeParts[data.size()-1] != 360){
            degreeParts[data.size()-1] = 360;
        }

        /*float p;
        for(int i=0;i<data.size();++i){
            p=data.get(i).getVolume() * 360;
            degreeParts[i]=base+(int)p;
            base+=p;
        }
        for(int dd:degreeParts)
            Log.i(TAG, "setData: degrePart--"+dd);*/
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);//宽的测量大小，模式
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);//高的测量大小，模式
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int w = widthSpecSize;   //定义测量宽，高(不包含测量模式),并设置默认值
        int h = heightSpecSize;

        //处理wrap_content的几种特殊情况
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            w = 700;  //单位是px
            h = 700;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            //只要宽度布局参数为wrap_content， 宽度给固定值200dp(处理方式不一，按照需求来)
            h = heightSpecSize;
            w= h;
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            w = widthSpecSize;
            h = w;
        }

        height=h;
        width=w;
        setMeasuredDimension(w,h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectBackground=new Rect(0,0,width,height);
        paint.setColor(bgColor);
        canvas.drawRect(rectBackground,paint);

        int centerX=getWidth()/2, centerY=getHeight()/2;

        if(data!=null){
            RectF oval = new RectF( 0,0,getWidth() , getHeight() );
            int s=startDegree;
            for(int i=0;i<data.size();++i){
                paint.setColor(data.get(i).getColor());
                Log.i(TAG, "onDraw: s="+s+",span:"+degreeParts[i]);
                canvas.drawArc(oval,s,degreeParts[i]-s,true,paint);
                s=degreeParts[i];
            }

            // 先画中空，再写文字
            if (hollow){
                RectF hollowRect = new RectF( 0,0,getWidth()/2 , getHeight()/2 );
                paint.setColor(bgColor);
                canvas.drawCircle(centerX , centerY,centerX/2 , paint);
            }

            s=startDegree;
            for(int i=0;i<data.size();++i){
                int midDegree=(s+degreeParts[i])/2, r=centerX-100;
                Point textPoint=ChartUtils.polarCoordToRect(midDegree,r);
                Log.i(TAG, "onDraw: origin text--x:"+textPoint.x+",y:"+textPoint.y);
                textPoint.x += centerX;
                textPoint.y += centerY;
                int textRot= 90+midDegree; // 90-(midDegree%90);
                Log.i(TAG, "onDraw: added text--x:"+textPoint.x+",y:"+textPoint.y+",midDegree:"+midDegree+",textRotation:"+textRot);
                canvas.rotate(textRot,textPoint.x,textPoint.y);
                canvas.drawText(data.get(i).getMsg(),textPoint.x,textPoint.y,textPaint);
                canvas.rotate(-textRot,textPoint.x,textPoint.y);
                s=degreeParts[i];
            }
        }
        else{     //为添加数据
            paint.setColor(Color.BLUE);
            //canvas.drawArc(500,500,1000,1000,0,120,true,paint);
            float x = getWidth() / 4;
            float y = getHeight() / 4;

            RectF oval = new RectF( 0,0,getWidth() , getHeight() );
            canvas.drawArc(oval,startDegree,120,true,paint);

            //canvas.rotate(120,x,y);
            paint.setColor(Color.GREEN);
            oval=new RectF(50,50,width-50,height-50);
            canvas.drawArc(oval,120,180,true,paint);
            paint.setColor(Color.RED);
            canvas.drawArc(oval,200,80,true,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //Log.i(TAG, "onTouchEvent: x:"+event.getX()+",y:"+event.getY());
                clickMoved=false;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX=(int)event.getX();
                int moveY=(int)event.getY();
                //Log.i(TAG, "onMove: x:"+moveX+",y:"+moveY+",在园内："+ DegreeInCircle(moveX,moveY));
                clickMoved=true;
                return false;
            case MotionEvent.ACTION_UP:
                if(clickMoved) return false;
                int upX=(int)event.getX();
                int upY=(int)event.getY();
                Log.i(TAG, "onUp: x:"+upX+",y:"+upY);
                if(clickListener!=null && data!=null)
                {
                    clickListener.PieChartClickedAt(calcPointToIndex(upX,upY));
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //startAnim();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //stopAnim();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility==VISIBLE){
            //startAnim();
        }
        else{
            //stopAnim();
        }
    }

    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener=new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            startDegree=(int)vAnimator.getAnimatedValue();
            invalidate();
        }
    };

    private void startAnim(){
        if(vAnimator==null){
            vAnimator=ValueAnimator.ofInt(0,355);
            vAnimator.addUpdateListener(animatorUpdateListener);
            vAnimator.setDuration(1500);
            vAnimator.setRepeatMode(ValueAnimator.RESTART);
            vAnimator.setRepeatCount(3);
            vAnimator.setInterpolator(new LinearInterpolator());
            vAnimator.start();
        }
        else if(!vAnimator.isStarted()){
            vAnimator.start();
        }
    }

    private void stopAnim(){
        if(vAnimator!=null){
            vAnimator.removeUpdateListener(animatorUpdateListener);
            vAnimator.removeAllUpdateListeners();
            vAnimator.cancel();
            vAnimator=null;
        }
    }

    // 判断触摸点是否在圆内并返回角度
    private double DegreeInCircle(int x, int y){
        int xx=x-getWidth()/2;
        int yy=y-getHeight()/2;
        double c= Math.sqrt( Math.pow(xx,2) + Math.pow(yy,2) );
        if(c > getHeight()/2) return -1;        //触摸点在圆以外
        double degree=Math.toDegrees(Math.acos(xx/c));        //反余弦函数计算角度
        if(yy<0){             //y轴正半轴情况--canvas中零角度为x轴正方向，顺时针；
            degree=360-degree;
        }
        //Log.i(TAG, "inCircle: degree:"+degree);     //
        return degree;
    }

    private int calcPointToIndex(int x, int y){
        double deg=DegreeInCircle(x,y);
        int i=0;
        while(degreeParts[i]<deg)
            ++i;
        Log.i(TAG, "calcPointToIndex: "+i);
        return i;
    }

    public void setOnPieChartClickListener(OnPieChartClickListener l){
        this.clickListener=l;
    }

    public interface OnPieChartClickListener{
        void PieChartClickedAt(int position);
    }

}
