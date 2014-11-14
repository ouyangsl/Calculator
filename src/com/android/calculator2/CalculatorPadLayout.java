/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.calculator2;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.view.View;
import android.view.ViewGroup;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A layout that places children in an evenly distributed grid based on the specified
 *  {@link android.R.attr#columnCount} and {@link android.R.attr#rowCount} attributes.
 */
public class CalculatorPadLayout extends ViewGroup {

    private int mRowCount;
    private int mColumnCount;
    private final Resources mResources;

    public CalculatorPadLayout(Context context) {
        this(context, null);
    }

    public CalculatorPadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalculatorPadLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                new int[] { android.R.attr.rowCount, android.R.attr.columnCount }, defStyle, 0);
        mRowCount = a.getInt(0, 1);
        mColumnCount = a.getInt(1, 1);
        mResources = context.getResources();

        a.recycle();
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        final NumberFormat nf = DecimalFormat.getInstance(mResources.getConfiguration().locale);

        ((Button) findViewById(R.id.digit_0)).setText(nf.format(0));
        ((Button) findViewById(R.id.digit_1)).setText(nf.format(1));
        ((Button) findViewById(R.id.digit_2)).setText(nf.format(2));
        ((Button) findViewById(R.id.digit_3)).setText(nf.format(3));
        ((Button) findViewById(R.id.digit_4)).setText(nf.format(4));
        ((Button) findViewById(R.id.digit_5)).setText(nf.format(5));
        ((Button) findViewById(R.id.digit_6)).setText(nf.format(6));
        ((Button) findViewById(R.id.digit_7)).setText(nf.format(7));
        ((Button) findViewById(R.id.digit_8)).setText(nf.format(8));
        ((Button) findViewById(R.id.digit_9)).setText(nf.format(9));
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        final boolean isRTL = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
        final int columnWidth =
                Math.round((float) (right - left - paddingLeft - paddingRight)) / mColumnCount;
        final int rowHeight =
                Math.round((float) (bottom - top - paddingTop - paddingBottom)) / mRowCount;

        int rowIndex = 0, columnIndex = 0;
        for (int childIndex = 0; childIndex < getChildCount(); ++childIndex) {
            final View childView = getChildAt(childIndex);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            if (childView.getId() == R.id.digi)

            final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();

            final int childTop = paddingTop + lp.topMargin + rowIndex * rowHeight;
            final int childBottom = childTop - lp.topMargin - lp.bottomMargin + rowHeight;
            final int childLeft = paddingLeft + lp.leftMargin +
                    (isRTL ? (mColumnCount - 1) - columnIndex : columnIndex) * columnWidth;
            final int childRight = childLeft - lp.leftMargin - lp.rightMargin + columnWidth;

            final int childWidth = childRight - childLeft;
            final int childHeight = childBottom - childTop;
            if (childWidth != childView.getMeasuredWidth() ||
                    childHeight != childView.getMeasuredHeight()) {
                childView.measure(
                        MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
            }
            childView.layout(childLeft, childTop, childRight, childBottom);

            rowIndex = (rowIndex + (columnIndex + 1) / mColumnCount) % mRowCount;
            columnIndex = (columnIndex + 1) % mColumnCount;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }
}

