package com.szh.designsimple;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Scroller;

/**
 * Created by moram on 2016/11/15.
 */
public class ScrollerLayout  extends ViewGroup{
    private static final String TAG=ScrollerLayout.class.getName();

    private Scroller scroller;
    /**
     * 滑动最小像素
     */
    private int mTouchSlop;

    /**
     * 触摸屏幕的
     */
    private float mDownX;
    /**
     * 移动此时的X
     */
    private float mMoveX;
    /**
     * 最后的X
     */
    private float mLastX;
    /**
     * 左边界
     */
    private int leftBorder;
    /**
     * 右边界
     */
    private int rightBorder;

    public ScrollerLayout(Context context) {
        super(context);
        init(context);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
         scroller = new Scroller(context);
        ViewConfiguration mViewConfig=ViewConfiguration.get(context);
        //获取滑动手势的像素
        mTouchSlop= mViewConfig.getScaledTouchSlop();
        Log.e(TAG,mTouchSlop+"");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount=getChildCount();
        for (int i=0;i<childCount;i++){
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            int childCount=getChildCount();
            for (int i=0;i<childCount;i++){
                View childView=getChildAt(i);
                childView.layout(childView.getMeasuredWidth()*i,0,(i+1)*childView.getMeasuredWidth(),childView.getMeasuredWidth());
            }
            leftBorder=getChildAt(0).getLeft()-100;
            rightBorder=getChildAt(childCount-1).getRight()+100;
        }
        Log.e(TAG,"leftBorder:"+leftBorder+";rightBorder:"+rightBorder);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action=ev.getAction();
        switch (action){

            case MotionEvent.ACTION_DOWN:
                mDownX=ev.getRawX();
                mLastX=mDownX;
                Log.e(TAG,"ev.getRawX():"+mDownX+";getX():"+getX());
                break;

            case MotionEvent.ACTION_MOVE:
                mMoveX=ev.getRawX();
                float move=Math.abs(mMoveX-mDownX);
                //是滑动拦截事件，自己消费
                if(move>mTouchSlop){
                    return true;
                }
                break;
        }


        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        switch(action){
            //移动的时候应该随着滑动的距离进行scrollto。我们需要检测当前是否是到边界了，如果是那就不滑动了。
            case MotionEvent.ACTION_MOVE:
                //只是在当前屏幕上的位置，不能超过屏幕的宽度
                mMoveX = event.getRawX();
                int scrolledX = (int) (mLastX - mMoveX);
                //获取当前View相对于屏幕滑动的偏移量单位是px像素
                Log.e(TAG,"getScrollX:"+getScrollX());
                Log.e(TAG,"scrolledX:"+scrolledX);
                //当前view已经位移和手滑动的偏移量如果已经小于边界说明不能继续滑动了。
                //当没有达到边界的时候getScrollX()的值就等于scrolledX的总和
                //但是getScrollX()只能为正 即view的任意一点的相对于初始状态的位移。
                if (getScrollX() + scrolledX < leftBorder) {
//                    scrollTo(leftBorder, 0);
                    return true;
                } else if (getScrollX() + getWidth() + scrolledX > rightBorder) {
                    //scrollTo相对初始位置偏移
//                    scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }
                //scrollBy相对当前位置发生位移
                scrollBy(scrolledX, 0);
                mLastX = mMoveX;
                break;
            //手抬起的时候我们需要检测当前位置是否达到我们需要的效果，如果不是我们需要继续完成滑动
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                //获取当前的页面的index 通过加上0.5 当滑动当前页面超过一般就会到下一页，如果小于0.5就保留在当前页
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                //当前到达每个页面的边界位移量获取
                int dx = targetIndex * getWidth() - getScrollX();
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
//                scroller.startScroll(getScrollX(), 0, dx, 0); //默认时间长是250毫秒
                scroller.startScroll(getScrollX(), 0, dx, 0,200);
                invalidate();
                if(targetIndex==0){
                    final View view=getChildAt(0);
                    // 开启移动动画
                    TranslateAnimation ta = new TranslateAnimation(view.getLeft(), view.getRight()/2, 0,0);
                    ta.setDuration(500);
                    ta.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            TranslateAnimation ta = new TranslateAnimation(view.getRight()/2,0, 0,0);
                            ta.setDuration(500);
                            view.startAnimation(ta);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.startAnimation(ta);
                }else if(targetIndex==getChildCount()-1){
                        View view=getChildAt(targetIndex);
                        // 开启移动动画
                        TranslateAnimation ta = new TranslateAnimation(view.getRight(),view.getLeft(),0,0);
                        ta.setDuration(500);
                        view.startAnimation(ta);
                }

                break;

        }


        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.e(TAG,"go");
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (scroller.computeScrollOffset()) {
            Log.e(TAG,"scroller.getCurrX():"+scroller.getCurrX());
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
//            if(scroller.getCurrX()==0){
//                scroller.startScroll(getScrollX(), 0, 200, 0,1000);
//                invalidate();
//            }

        }
    }
}
