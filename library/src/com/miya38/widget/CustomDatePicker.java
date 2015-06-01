package com.miya38.widget;

import java.util.Calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.DatePicker;

import com.miya38.R;

/**
 * DatePickerのカスタムクラス
 */
public class CustomDatePicker extends DatePicker {
    /** 未来日チェック有無 */
    private boolean mIsFuture;
    /** 年齢より若い場合エラーとする。 */
    private int mAge = -1;

    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     */
    public CustomDatePicker(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
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
    public CustomDatePicker(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * init
     * 
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     */
    private void init(final Context context, final AttributeSet attrs) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomDatePicker);
        this.mIsFuture = ta.getBoolean(R.styleable.CustomDatePicker_datepicker_future, false);
        this.mAge = ta.getInt(R.styleable.CustomDatePicker_datepicker_age, -1);
        ta.recycle();
    }

    /**
     * 必須チェック
     * 
     * @return true:正常,false:エラー
     */
    public boolean isValid() {
        if (!mIsFuture) {
            final Calendar calendar1 = Calendar.getInstance(); // 現在の日付
            final Calendar calendar2 = Calendar.getInstance(); // DatePickerで入力された日付
            final Calendar calendar3 = Calendar.getInstance(); // 18歳の日付
            calendar2.set(getYear(), getMonth(), getDayOfMonth());

            if (calendar1.compareTo(calendar2) == -1) {
                return false;
            }
            if (mAge > 0) {
                calendar3.set(Calendar.YEAR, calendar1.get(Calendar.YEAR) - mAge);
                if (calendar2.after(calendar3)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return future
     */
    public boolean isFuture() {
        return mIsFuture;
    }

    /**
     * @param future
     *            true:未来日を許容する。false:未来日を許容しない。
     */
    public void setFuture(final boolean future) {
        this.mIsFuture = future;
    }

}
