package com.space.charts;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PieChart extends View {
    private static final String TAG="PieChart";
    private int width,height,bgColor;
    private Context context;
    private Paint paint;
    private Rect rectBackground;

    //动画相关
    private ValueAnimator vAnimator;
    private int startDegree=0;     //起始角度

    // touchEvent相关，实现点击
    private OnPieChartClickListener clickListener;
    private boolean clickMoved=false;

    //数据相关
    private List<PieData> data;
    private int[] degreeParts;

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
    }

    private void initAttr(AttributeSet attributeSet){
        TypedArray ta=context.obtainStyledAttributes(attributeSet,R.styleable.PieChart);
        width=(int)ta.getDimension(R.styleable.PieChart_width,dp2px(200));
        height=(int)ta.getDimension(R.styleable.PieChart_height,dp2px(200));
        bgColor=ta.getColor(R.styleable.PieChart_background_color,Color.WHITE);
    }

    private void initPaint()
    {
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setData(List<PieData> d) throws Exception{
        float sum=0;
        for(PieData p:d){
            sum+=p.getProp();
        }
        if(sum!=1)
            throw new Exception("各部分比例之和不为一！！");

        this.data=d;
        degreeParts=new int[data.size()];
        Log.i(TAG, "setData: dataLen--"+data.size());

        int base=0;
        float p;
        for(int i=0;i<data.size();++i){
            p=data.get(i).getProp() * 360;
            degreeParts[i]=base+(int)p;
            base+=p;
        }
        for(int dd:degreeParts)
            Log.i(TAG, "setData: degrePart--"+dd);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectBackground=new Rect(0,0,width,height);
        paint.setColor(bgColor);
        canvas.drawRect(rectBackground,paint);

        if(data!=null){
            RectF oval = new RectF( 0,0,getWidth() , getHeight() );
            int s=startDegree;
            for(int i=0;i<data.size();++i){
                paint.setColor(data.get(i).getColor());
                Log.i(TAG, "onDraw: s="+s+",span:"+degreeParts[i]);
                canvas.drawArc(oval,s,degreeParts[i]-s,true,paint);
                int midDegree=(s+degreeParts[i])/2, r=getHeight()/2-100;
                float textX = midDegree>180 ? (float)Math.cos(Math.toRadians(360-midDegree))*r : (float)Math.cos(Math.toRadians(midDegree))*r;
                textX = textX + getWidth()/2;
                //float textY= midDegree>180 ? (float)Math.sin(Math.toRadians(360-midDegree))*r : (float)Math.sin(Math.toRadians(midDegree))*r;
                float textY= (float)Math.sin(Math.toRadians(midDegree))*r;
                Log.i(TAG, "onDraw: textY 1="+textY);
                textY=textY + getHeight()/2;
                Log.i(TAG, "onDraw: textY 2="+textY);
                paint.setColor(Color.BLACK);
                paint.setTextSize(50);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(data.get(i).getMsg(),textX,textY,paint);
                s=degreeParts[i];
            }
        }
        else{
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
                return true;
            case MotionEvent.ACTION_MOVE:
                int moveX=(int)event.getX();
                int moveY=(int)event.getY();
                //Log.i(TAG, "onMove: x:"+moveX+",y:"+moveY+",在园内："+ DegreeInCircle(moveX,moveY));
                clickMoved=true;
                break;
            case MotionEvent.ACTION_UP:
                if(clickMoved) return true;
                int upX=(int)event.getX();
                int upY=(int)event.getY();
                Log.i(TAG, "onUp: x:"+upX+",y:"+upY);
                if(clickListener!=null)
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
        return i;
    }

    private int dp2px(float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }

    public void setOnPieChartClickListener(OnPieChartClickListener l){
        this.clickListener=l;
    }

    public interface OnPieChartClickListener{
        void PieChartClickedAt(int position);
    }

}
