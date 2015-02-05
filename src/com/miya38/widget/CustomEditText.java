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
    /** メールアドレスチェックをするか？ */
    private boolean mIsMailAddress;
    /** 電話番号チェックをするか？ */
    private boolean mIsPhone;
    /** URLチェックをするか？ */
    private boolean mIsUrl;

    /** Filter */
    private class Filter implements InputFilter {
        @Override
        public CharSequence filter(final CharSequence source, final int start, final int end, final Spanned dest, final int dstart, final int dend) {
            return source.toString().matches("^[a-zA-Z0-9]+$") ? source : "";
        }
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
    public CustomEditText(final Context context, final AttributeSet attrs) {
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
    public CustomEditText(final Context context, final AttributeSet attrs, final int defStyle) {
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
        setFilter();

        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        this.mIsRequired = ta.getBoolean(R.styleable.CustomEditText_required, false);
        this.mIsAlphanumeric = ta.getBoolean(R.styleable.CustomEditText_alphanumeric, false);
        this.mName = ta.getString(R.styleable.CustomEditText_name);
        this.mIsMailAddress = ta.getBoolean(R.styleable.CustomEditText_mailaddress, false);
        this.mIsPhone = ta.getBoolean(R.styleable.CustomEditText_phone, false);
        this.mIsUrl = ta.getBoolean(R.styleable.CustomEditText_url, false);

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
        if (mIsAlphanumeric) {
            validate.addValidator(new AlnumValidator(context, context.getString(R.string.error_form_edittext_alpha_and_numeric, mName)));
        }
        if (mIsMailAddress) {
            validate.addValidator(new EmailValidator(context, context.getString(R.string.error_form_edittext_mailaddress, mName)));
        }
        if (mIsPhone) {
            validate.addValidator(new PhoneValidator(context, context.getString(R.string.error_form_edittext_phone, mName)));
        }
        if (mIsUrl) {
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

    /**
     * フィルタ設定
     */
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

        super.onDetachedFromWindow();
    }

    /**
     * @return mIsRequired
     */
    public boolean isIsRequired() {
        return mIsRequired;
    }

    /**
     * @param isRequired
     *            mIsRequiredを設定する。
     */
    public void setIsRequired(final boolean isRequired) {
        this.mIsRequired = isRequired;
    }

    /**
     * @return 英数字チェックの有無を取得する。
     */
    public boolean isIsAlphanumeric() {
        return mIsAlphanumeric;
    }

    /**
     * @param isAlphanumeric
     *            英数字チェックの有無を設定する。
     */
    public void setIsAlphanumeric(final boolean isAlphanumeric) {
        this.mIsAlphanumeric = isAlphanumeric;
    }

    /**
     * @return エラー時等で使用する名前を取得する。
     */
    public String getName() {
        return mName;
    }

    /**
     * @param name
     *            エラー時等で使用する名前を設定する。
     */
    public void setName(final String name) {
        this.mName = name;
    }

    /**
     * @return メールアドレスチェックの有無を返却する。
     */
    public boolean isMailAddress() {
        return mIsMailAddress;
    }

    /**
     * @param isMailAddress
     *            メールアドレスチェックの有無を設定する。
     */
    public void setMailAddress(final boolean isMailAddress) {
        this.mIsMailAddress = isMailAddress;
    }

    /**
     * @return 電話番号チェックの有無を返却する。
     */
    public boolean isPhone() {
        return mIsPhone;
    }

    /**
     * @param isPhone
     *            電話番号チェックの有無を設定する。
     */
    public void setPhone(final boolean isPhone) {
        this.mIsPhone = isPhone;
    }

    /**
     * @return URLチェックの有無を返却する。
     */
    public boolean isUrl() {
        return mIsUrl;
    }

    /**
     * @param isUrl
     *            Urlチェックの有無を設定する。
     */
    public void setUrl(final boolean isUrl) {
        this.mIsUrl = isUrl;
    }
}
