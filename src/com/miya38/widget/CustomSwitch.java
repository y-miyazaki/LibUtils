/*
 * Copyright (C) 2011 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.miya38.widget;

import org.jraf.android.backport.switchwidget.Switch;

import android.content.Context;
import android.util.AttributeSet;

/**
 * カスタムSwitchクラス
 * <p>
 * Switch backportを使用する。
 * </p>
 *
 * @author y-miyazaki
 */
public class CustomSwitch extends Switch {

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     */
    public CustomSwitch(Context context) {
        super(context);
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     */
    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     * @param defStyle
     *            Default style for this View
     */
    public CustomSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
