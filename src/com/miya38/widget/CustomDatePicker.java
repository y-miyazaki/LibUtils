package com.miya38.widget;

import java.util.Calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.Toast;

import com.miya38.R;

/**
 * DatePickerのカスタムクラス
 *
 * @author y-miyazaki
 *
 */
public class CustomDatePicker extends DatePicker {
    /** 未来日チェック有無 */
    private boolean mIsFuture;
    /** エラータイトル名 */
    private String mName;
    /** 18歳チェック有無 */
    private boolean mIsCheck18;

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     * @param defStyle
     *            Default style for this View
     */
    public CustomDatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * init
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    private void init(Context context, AttributeSet attrs) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomDatePicker);
        this.mIsFuture = ta.getBoolean(R.styleable.CustomDatePicker_datepicker_future, false);
        this.mName = ta.getString(R.styleable.CustomDatePicker_datepicker_name);
        this.mIsCheck18 = ta.getBoolean(R.styleable.CustomDatePicker_datepicker_check18, false);
        ta.recycle();
    }

    /**
     * 必須チェック
     *
     * @return true:正常,false:エラー
     */
    public boolean isValid() {
        return isValid(true);
    }

    /**
     * 必須チェック
     *
     * @param isToastDisplay
     *            エラーToastの表示非表示(true:表示,false:非表示)
     * @return true:正常,false:エラー
     *
     */
    public boolean isValid(boolean isToastDisplay) {
        if (!mIsFuture) {
            final Context context = getContext().getApplicationContext();
            final Calendar calendar1 = Calendar.getInstance(); // 現在の日付
            final Calendar calendar2 = Calendar.getInstance(); // DatePickerで入力された日付
            final Calendar calendar3 = Calendar.getInstance(); // 18歳の日付
            calendar2.set(getYear(), getMonth(), getDayOfMonth());
            calendar3.set(Calendar.YEAR, calendar1.get(Calendar.YEAR) - 18);

            if (calendar1.compareTo(calendar2) == -1) {
                if (isToastDisplay) {
                    Toast.makeText(context, context.getString(R.string.error_form_datepicker_future, mName), Toast.LENGTH_LONG).show();
                }
                return false;
            }
            if (this.mIsCheck18) {
                if (calendar2.after(calendar3)) {
                    if (isToastDisplay) {
                        Toast.makeText(context, context.getString(R.string.error_form_datepicker_check18), Toast.LENGTH_LONG).show();
                    }
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
     *            futureをセットする
     */
    public void setFuture(boolean future) {
        this.mIsFuture = future;
    }

    /**
     * @return name
     */
    public String getName() {
        return mName;
    }

    /**
     * @param name
     *            nameをセットする
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * 18歳未満の場合はボタン押下できないようにする
     *
     * @param button
     */
    public void setButtonClickable(final CustomButton button) {
        if (!isValid(false)) {
            button.setClickable(false);
            button.setGrayScaleColorFilter();
        }
        final DatePicker.OnDateChangedListener d = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (isValid(false)) {
                    button.setClickable(true);
                    button.clearColorFilter();
                } else {
                    button.setClickable(false);
                    button.setGrayScaleColorFilter();
                }
            }
        };
        init(getYear(), getMonth(), getDayOfMonth(), d);
    }
}
