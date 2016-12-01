package com.szh.designsimple;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity {

    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart();
            }
        });

        textview=(TextView)findViewById(R.id.textview);

    }

    public void animationStart(){
        ObjectAnimator moveIn=ObjectAnimator.ofFloat(textview,"translationX",0f,500f,0f);
        ObjectAnimator scale=ObjectAnimator.ofFloat(textview,"scaleY",1f,3f);
        ObjectAnimator oanimation=ObjectAnimator.ofFloat(textview,"rotation",0f,360f);
//        oanimation.setRepeatMode();
        oanimation.setDuration(2000);
        AnimatorSet asets=new AnimatorSet();
        asets.play(scale).with(oanimation).after(moveIn);
        asets.setDuration(5000);
        asets.start();

        asets.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("tag","onAnimationEnd==go");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        Point startPoint=new Point(0,0);
        Point endPoint=new Point(300,300);
        ValueAnimator anima=ValueAnimator.ofObject(new PointEvaluator(),startPoint,endPoint);
        anima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e("tag",animation.getAnimatedValue()+"");
            }
        });
        anima.setDuration(2000);
        anima.start();
    }
}
