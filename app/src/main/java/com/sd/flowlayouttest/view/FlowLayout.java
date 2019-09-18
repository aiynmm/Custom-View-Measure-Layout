package com.sd.flowlayouttest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 测量阶段：从上到下递归地调用每个 View 或者 ViewGroup 的 measure() 方法，测量他们的尺寸并计算它们的位置；
     * <p>
     * measure() 方法被父 View 调用，在 measure() 中做一些准备和优化工作后，调用 onMeasure() 来进行实际的自我测量。
     * <p>
     * View 在 onMeasure() 中会计算出自己的尺寸然后保存；
     * <p>
     * ViewGroup 在 onMeasure() 中会调用所有子 View 的 measure() 让它们进行自我测量，并根据子 View 计算出的期望尺寸来计算出它们的实际尺寸和位置,
     * （实际上 99.99% 的父 View 都会使用子 View 给出的期望尺寸来作为实际尺寸）然后保存。同时，它也会根据子 View 的尺寸和位置来计算出自己的尺寸然后保存；
     * <p>
     * for (int i = 0; i < getChildCount(); i++) {
     * View childView = getChildAt(i);
     * int childWidthSpec;
     * //得到子view的宽高（开发者的要求，即写在xml的layout_开头的属性）
     * LayoutParams lp = childView.getLayoutParams();
     * //自己的宽度尺寸限制和测量模式限制
     * int selfWidthSize = MeasureSpec.getSize(widthMeasureSpec);
     * int selfWidthMode = MeasureSpec.getMode(widthMeasureSpec);
     * //根据尺寸和模式限制，可以得到自己的剩余可用空间
     * int availableWidth = 0;//剩余可用空间
     * int usedWidthSize = 0;//已用空间
     * switch (lp.width) {
     * case LayoutParams.MATCH_PARENT://占满父View剩余全部宽度
     * //根据尺寸和模式限制，可以得到自己的剩余可用空间
     * //MeasureSpec.EXACTLY:精确值，我多大，剩余空间就多大
     * //MeasureSpec.AT_MOST:不能超过，剩余空间也是父View的尺寸
     * if (selfWidthMode == MeasureSpec.EXACTLY || selfWidthMode == MeasureSpec.AT_MOST) {
     * childWidthSpec = MeasureSpec.makeMeasureSpec(availableWidth - usedWidthSize, MeasureSpec.EXACTLY);
     * } else {//MeasureSpec.UNSPECIFIED://没有限制，即无限大
     * //这时size无意义，因为子View无法填满一个无限大的空间
     * childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
     * }
     * break;
     * case LayoutParams.WRAP_CONTENT://子View自适应内容
     * //原则是是子View自己测量，但是有一个隐藏的限制条件，就是不能超过父View的边界
     * if (selfWidthMode == MeasureSpec.EXACTLY || selfWidthMode == MeasureSpec.AT_MOST) {
     * //所以size为剩余可用空间，mode为不能超过边界
     * childWidthSpec = MeasureSpec.makeMeasureSpec(availableWidth - usedWidthSize, MeasureSpec.AT_MOST);
     * } else {//MeasureSpec.UNSPECIFIED://可用空间没有限制，即无限大
     * //这时让子View自己随意测量尺寸
     * childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
     * }
     * break;
     * <p>
     * default://固定dp
     * childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
     * break;
     * }
     * childView.measure(childWidthSpec, );
     * <p>
     * }
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();
        int mPaddingBottom = getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineY = mPaddingTop;
        int lineMaxWidth = 0;//行最大宽度
        int lineHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int spaceWidth = 0;//宽度空白，即横向margin距离
            int spaceheight = 0;//高度空白，即纵向margin距离
            LayoutParams childLp = child.getLayoutParams();
            //①调用child的measure方法，使其自我测量。这里使用了ViewGroup封装好的方法
            if (childLp instanceof MarginLayoutParams) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, lineY);
                MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                spaceWidth = mlp.leftMargin + mlp.rightMargin;
                spaceheight = mlp.topMargin + mlp.bottomMargin;
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            spaceWidth += childWidth;
            spaceheight += childHeight;

            if (lineUsed + spaceWidth > widthSize) {//超过最大宽度，换行
                //比较当前行宽度和新添加的item的宽度做比较
                lineMaxWidth = Math.max(lineUsed, childWidth);
                lineY += lineHeight;
                lineUsed = mPaddingLeft + mPaddingRight;//重置
                lineHeight = 0;
            }

            if (spaceheight > lineHeight) {
                lineHeight = spaceheight;
            }
            lineUsed += spaceWidth;//每遍历一个View,宽度叠加
            //如果是最后一个，则将当前记录的最大宽度lineMaxWidth和当前行宽度lineUsed做比较
            if (i == getChildCount() - 1) {
                lineMaxWidth = Math.max(lineMaxWidth, lineUsed);
            }
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : lineMaxWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : lineY + lineHeight + mPaddingBottom
        );

    }

    /**
     * 布局阶段：从上到下递归地调用每个 View 或者 ViewGroup 的 layout() 方法，把测得的它们的尺寸和位置赋值给它们。
     * <p>
     * layout() 方法被父 View 调用，在 layout() 中它会保存父 View 传进来的自己的位置和尺寸，并且调用 onLayout() 来进行实际的内部布局。
     * <p>
     * View: 由于没有子 View，所以 View 的 onLayout() 什么也不做。
     * <p>
     * ViewGroup 在 onLayout() 中会调用自己的所有子 View 的 layout() 方法，把它们的尺寸和位置传给它们，让它们完成自我的内部布局。
     */

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();

        int lineX = mPaddingLeft;
        int lineY = mPaddingTop;
        int lineWidth = r - l;

        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineHeight = 0;

        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int spaceWidth = 0;
            int spaceHeight = 0;
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            LayoutParams childLp = child.getLayoutParams();
            if (childLp instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                spaceWidth = mlp.leftMargin + mlp.rightMargin;
                spaceHeight = mlp.topMargin + mlp.bottomMargin;
                left = lineX + mlp.leftMargin;
                top = lineY + mlp.topMargin;
                right = lineX + mlp.leftMargin + childWidth;
                bottom = lineY + mlp.topMargin + childHeight;
            } else {
                left = lineX;
                top = lineY;
                right = lineX + childWidth;
                bottom = lineY + childHeight;
            }
            spaceWidth += childWidth;
            spaceHeight += childHeight;

            if (lineUsed + spaceWidth > lineWidth) {
                lineY += lineHeight;
                lineUsed = mPaddingLeft + mPaddingRight;
                lineX = mPaddingLeft;
                lineHeight = 0;
                if (childLp instanceof MarginLayoutParams) {
                    MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                    left = lineX + mlp.leftMargin;
                    top = lineY + mlp.topMargin;
                    right = lineX + mlp.leftMargin + childWidth;
                    bottom = lineY + mlp.topMargin + childHeight;
                } else {
                    left = lineX;
                    top = lineY;
                    right = lineX + childWidth;
                    bottom = lineY + childHeight;
                }
            }
            child.layout(left, top, right, bottom);
            if (spaceHeight > lineHeight) {
                lineHeight = spaceHeight;
            }
            lineUsed += spaceWidth;
            lineX += spaceWidth;
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }
}
