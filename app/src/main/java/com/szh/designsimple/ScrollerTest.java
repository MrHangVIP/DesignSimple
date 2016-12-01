package com.szh.designsimple;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ScrollerTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller_test);
        Log.e("tag","变化的值：");
        ValueAnimator anim=ValueAnimator.ofFloat(0f,1f);
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e("tag","变化的值："+(float)animation.getAnimatedValue());
            }
        });
        anim.start();
    }
}
