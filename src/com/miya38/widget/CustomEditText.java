package com.miya38.widget;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

import com.miya38.R;
import com.miya38.validator.Validate;
import com.miya38.validator.validator.AlnumValidator;
import com.miya38.validator.validator.EmailValidator;
import com.miya38.validator.validator.MaxLengthValidator;
import com.miya38.validator.validator.MinLengthValidator;
import com.miya38.validator.validator.NotEmptyValidator;
import com.miya38.validator.validator.PhoneValidator;

/**
 * 英数字フィルタリング用EditTextクラス
 *
 * @author y-miyazaki
 *
 */
public class CustomEditText extends EditText {
    /** 必須とするか？ */
    private boolean mIsRequired;
    /** アルファベット・数値のみの入力とするか？ */
    private boolean mIsAlphanumeric;
    /** エラー時に表示される名称 */
    private String mName;
    /** 入力最小文字数 */
    private int mMinLength = -1;
    /** 入力最大文字数 */
    private int mMaxLength = -1;
    /** メールアドレスチェックをするか？ */
    private boolean mMailAddress;
    /** 電話番号チェックをするか？ */
    private boolean mPhone;
    /** URLチェックをするか？ */
    private boolean mUrl;
    /** 確認する対象のEditText */
    private CustomEditText mCustomEditText;

    /** Filter */
    private class Filter implements InputFilter {
        /*
         * (非 Javadoc)
         * @see android.text.InputFilter#filter(java.lang.CharSequence, int, int, android.text.Spanned, int, int)
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return source.toString().matches("^[a-zA-Z0-9]+$") ? source : "";
        }
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    public CustomEditText(Context context, AttributeSet attrs) {
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
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
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
        setFilter();

        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        this.mIsRequired = ta.getBoolean(R.styleable.CustomEditText_required, false);
        this.mIsAlphanumeric = ta.getBoolean(R.styleable.CustomEditText_alphanumeric, false);
        this.mName = ta.getString(R.styleable.CustomEditText_name);
        this.mMinLength = ta.getInt(R.styleable.CustomEditText_min_length, -1);
        this.mMaxLength = ta.getInt(R.styleable.CustomEditText_max_length, -1);
        this.mMailAddress = ta.getBoolean(R.styleable.CustomEditText_mailaddress, false);
        this.mPhone = ta.getBoolean(R.styleable.CustomEditText_phone, false);
        this.mUrl = ta.getBoolean(R.styleable.CustomEditText_url, false);

        ta.recycle();

    }

    /**
     * Validate取得
     *
     * @return Validate
     */
    private Validate getValidate() {
        final Context context = getContext().getApplicationContext();
        final Validate validate = new Validate(this);

        if (mIsRequired) {
            validate.addValidator(new NotEmptyValidator(context, context.getString(R.string.error_form_edittext_required, mName)));
        }
        if (mMinLength != -1) {
            validate.addValidator(new MinLengthValidator(context, context.getString(R.string.error_form_edittext_min_size, mName, mMinLength), mMinLength));
        }
        if (mMaxLength != -1) {
            validate.addValidator(new MaxLengthValidator(context, context.getString(R.string.error_form_edittext_max_size, mName, mMaxLength), mMaxLength));
        }
        if (mIsAlphanumeric) {
            validate.addValidator(new AlnumValidator(context, context.getString(R.string.error_form_edittext_alpha_and_numeric, mName)));
        }
        if (mMailAddress) {
            validate.addValidator(new EmailValidator(context, context.getString(R.string.error_form_edittext_mailaddress, mName)));
        }
        if (mPhone) {
            validate.addValidator(new PhoneValidator(context, context.getString(R.string.error_form_edittext_phone, mName)));
        }
        if (mUrl) {
            validate.addValidator(new PhoneValidator(context, context.getString(R.string.error_form_edittext_url, mName)));
        }
        return validate;
    }

    /**
     * バリデーションチェック
     *
     * @return true:正常,false:エラー
     */
    public boolean isValid() {
        final Validate validate = getValidate();
        if (validate.isValid(getText().toString())) {
            return true;
        }
        return false;
    }

    /**
     * バリデーションエラーメッセージ
     *
     * @return エラーメッセージの配列
     */
    public List<String> getErrorMessages() {
        final Validate validate = getValidate();
        return validate.getMessages();
    }

    private void setFilter() {
        if (mIsAlphanumeric) {
            int count = 0;
            final InputFilter[] inputFilter = getFilters();
            final InputFilter[] inputFilter2 = new InputFilter[getFilters().length + 1];

            for (final InputFilter tmp : inputFilter) {
                inputFilter2[count++] = tmp;
            }
            inputFilter2[inputFilter2.length - 1] = new Filter();
            setFilters(inputFilter2);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onDetachedFromWindow() {
        setBackgroundDrawable(null);
        setOnClickListener(null);
        mCustomEditText = null;

        super.onDetachedFromWindow();
    }

    /**
     * @return mIsRequired
     */
    public boolean isIsRequired() {
        return mIsRequired;
    }

    /**
     * @param mIsRequired
     *            mIsRequiredを設定する。
     */
    public void setIsRequired(boolean mIsRequired) {
        this.mIsRequired = mIsRequired;
    }

    /**
     * @return mIsAlphanumeric
     */
    public boolean isIsAlphanumeric() {
        return mIsAlphanumeric;
    }

    /**
     * @param mIsAlphanumeric
     *            mIsAlphanumericを設定する。
     */
    public void setIsAlphanumeric(boolean mIsAlphanumeric) {
        this.mIsAlphanumeric = mIsAlphanumeric;
    }

    /**
     * @return mName
     */
    public String getName() {
        return mName;
    }

    /**
     * @param mName
     *            mNameを設定する。
     */
    public void setName(String mName) {
        this.mName = mName;
    }

    /**
     * @return mMinLength
     */
    public int getMinLength() {
        return mMinLength;
    }

    /**
     * @param mMinLength
     *            mMinLengthを設定する。
     */
    public void setMinLength(int mMinLength) {
        this.mMinLength = mMinLength;
    }

    /**
     * @return mMaxLength
     */
    public int getMaxLength() {
        return mMaxLength;
    }

    /**
     * @param mMaxLength
     *            mMaxLengthを設定する。
     */
    public void setMaxLength(int mMaxLength) {
        this.mMaxLength = mMaxLength;
    }

    /**
     * @return mMailAddress
     */
    public boolean isMailAddress() {
        return mMailAddress;
    }

    /**
     * @param mMailAddress
     *            mMailAddressを設定する。
     */
    public void setMailAddress(boolean mMailAddress) {
        this.mMailAddress = mMailAddress;
    }

    /**
     * @return mPhone
     */
    public boolean isPhone() {
        return mPhone;
    }

    /**
     * @param mPhone
     *            mPhoneを設定する。
     */
    public void setPhone(boolean mPhone) {
        this.mPhone = mPhone;
    }

    /**
     * @return mUrl
     */
    public boolean isUrl() {
        return mUrl;
    }

    /**
     * @param mUrl
     *            mUrlを設定する。
     */
    public void setUrl(boolean mUrl) {
        this.mUrl = mUrl;
    }

    /**
     * @return mCustomEditText
     */
    public CustomEditText getCustomEditText() {
        return mCustomEditText;
    }

    /**
     * @param mCustomEditText
     *            mCustomEditTextを設定する。
     */
    public void setCustomEditText(CustomEditText mCustomEditText) {
        this.mCustomEditText = mCustomEditText;
    }
}
