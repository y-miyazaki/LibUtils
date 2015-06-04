/*
 * Copyright (C) 2011 The Android Open Source Project Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.miya38.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Space is a lightweight View subclass that may be used to create gaps between
 * components in general purpose layouts.
 */
public final class CustomSpace extends View {
    /**
     * Constructor
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomSpace(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        if (getVisibility() == VISIBLE) {
            setVisibility(INVISIBLE);
        }
    }

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */
    public CustomSpace(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor
     *
     * @param context
     */
    public CustomSpace(final Context context) {
        // noinspection NullableProblems
        this(context, null);
    }

    /**
     * Draw nothing.
     *
     * @param canvas
     *         an unused parameter.
     */
    @Override
    public void draw(final Canvas canvas) {
        // 何もしない。
    }

    /**
     * Compare to: {@link View#getDefaultSize(int, int)} If mode is AT_MOST,
     * return the child size instead of the parent
     * size (unless it is too big).
     */
    private static int getDefaultSize2(final int size, final int measureSpec) {
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(size, specSize);
            case MeasureSpec.EXACTLY:
                return specSize;
            default:
                break;
        }
        return size;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize2(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize2(getSuggestedMinimumHeight(), heightMeasureSpec));
    }
}
