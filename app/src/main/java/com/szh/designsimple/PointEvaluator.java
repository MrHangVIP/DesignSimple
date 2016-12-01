package com.szh.designsimple;

import android.animation.TypeEvaluator;

/**
 * Created by moram on 2016/11/24.
 */
public class PointEvaluator implements TypeEvaluator{
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint=(Point)startValue;
        Point endPoint=(Point)endValue;
        float x=startPoint.getX()+(endPoint.getX()-startPoint.getX())*fraction;
        float y=startPoint.getY()+(endPoint.getY()-startPoint.getY())*fraction;
        return new Point(x,y);
    }
}
