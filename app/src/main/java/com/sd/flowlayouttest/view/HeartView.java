package com.sd.flowlayouttest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sd.flowlayouttest.util.ConverUtil;

public class HeartView extends View {
    private Paint paint;
    private Path path;

    private float width = ConverUtil.dp2px(200);
    private float height = ConverUtil.dp2px(300);

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        path = new Path();
    }

    /**
     * 自定义View测量，需要在计算的同时，保证计算结果满足父 View 给出的的尺寸限制
     * <p>
     * 父View的限制：UNSPECIFIED：不限制
     * AT_MOST：限制上限
     * EXACTLY：限制固定值
     * <p>
     * 1. 重新 onMeasure()，并计算出 View 的尺寸；
     * 2. 使用 resolveSize() 来让子 View 的计算结果符合父 View 的限制
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = resolveSize((int) width, widthMeasureSpec);
        int h = resolveSize((int) height, heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        path.moveTo(width / 2, height / 4);
        path.cubicTo(width / 7, height / 9, width / 13, (height * 2) / 5, width / 2, (height * 7) / 12);
        //右半面
        path.moveTo(width / 2, height / 4);
        path.cubicTo((width * 6) / 7, height / 9, (width * 12) / 13, (height * 2) / 5, width / 2, (height * 7) / 12);
        canvas.drawPath(path, paint);
    }
}
