package com.miya38.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ListView.FixedViewInfo;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.miya38.widget.CustomListView;

/**
 * ビューを利用する際のヘルパーを提供します。
 * 
 * @author y-miyazaki
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public final class ViewHelper {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** ステータスバーの高さ(low) */
    private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
    /** ステータスバーの高さ(medium) */
    private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
    /** ステータスバーの高さ(high) */
    private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
    /** ステータスバーの高さ(xhigh) */
    private static final int XHIGH_DPI_STATUS_BAR_HEIGHT = 50;

    /**
     * アニメーションの種類
     * 
     * @author y-miyazaki
     */
    public enum AnimationKind {
        /** フェードアニメーション */
        FADE,
        /** スケールアニメーション */
        SCALE,
        /** トランジションアニメーション */
        TRANSLATE
    }

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ViewHelper() {
    }

    /**
     * 自動型変換する findViewById。
     * 
     * @param <V>
     *            View
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @return View
     */
    public static <V extends View> V findView(final Activity activity, final int id) {
        return (V) activity.findViewById(id);
    }

    /**
     * 自動型変換する findViewById。
     * 
     * @param <V>
     *            View
     * @param view
     *            view
     * @param id
     *            id
     * @return View
     */
    public static <V extends View> V findView(final View view, final int id) {
        return (V) view.findViewById(id);
    }

    /**
     * 自動型変換する findViewById。
     * 
     * @param <V>
     *            View
     * @param dialog
     *            dialog
     * @param id
     *            id
     * @return View
     */
    public static <V extends View> V findView(final Dialog dialog, final int id) {
        return (V) dialog.findViewById(id);
    }

    /**
     * 自動型変換する findViewById。
     * 
     * @param <V>
     *            View
     * @param window
     *            ウィンドウ
     * @param id
     *            id
     * @return View
     */
    public static <V extends View> V findView(final Window window, final int id) {
        return (V) window.findViewById(id);
    }

    /**
     * 自動型変換する setOnClickListerner。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param l
     *            {@link OnClickListener}
     */
    public static void setOnClickListener(final Activity activity, final int id, final OnClickListener l) {
        activity.findViewById(id).setOnClickListener(l);
    }

    /**
     * 自動型変換する setOnClickListerner。
     * 
     * @param view
     *            view
     * @param id
     *            id
     * @param l
     *            {@link OnClickListener}
     */
    public static void setOnClickListener(final View view, final int id, final OnClickListener l) {
        view.findViewById(id).setOnClickListener(l);
    }

    /**
     * 自動型変換する setOnClickListerner。
     * 
     * @param dialog
     *            dialog
     * @param id
     *            id
     * @param l
     *            {@link OnClickListener}
     */
    public static void setOnClickListener(final Dialog dialog, final int id, final OnClickListener l) {
        dialog.findViewById(id).setOnClickListener(l);
    }

    /**
     * TextView に String リソースを設定するヘルパー。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param stringId
     *            文字列リソースID
     */
    public static void setText(final View v, final int id, final int stringId) {
        if (stringId != 0) {
            ((TextView) v.findViewById(id)).setText(stringId);
        }
    }

    /**
     * CustomTextView にテキストを設定するヘルパー。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param text
     *            テキスト
     */
    public static void setText(final Activity activity, final int id, final CharSequence text) {
        ((TextView) activity.findViewById(id)).setText(text);
    }

    /**
     * TextView に String リソースを設定するヘルパー。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param stringId
     *            文字列リソースID
     */
    public static void setText(final Activity activity, final int id, final int stringId) {
        if (stringId != 0) {
            ((TextView) activity.findViewById(id)).setText(stringId);
        }
    }

    /**
     * CustomTextView にテキストを設定するヘルパー。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param text
     *            テキスト
     */
    public static void setText(final View v, final int id, final CharSequence text) {
        ((TextView) v.findViewById(id)).setText(text);
    }

    /**
     * TextView に String リソースを設定するヘルパー。
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param stringId
     *            文字列リソースID
     */
    public static void setText(final Dialog dialog, final int id, final int stringId) {
        if (stringId != 0) {
            ((TextView) dialog.findViewById(id)).setText(stringId);
        }
    }

    /**
     * CustomTextView にテキストを設定するヘルパー。
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param text
     *            テキスト
     */
    public static void setText(final Dialog dialog, final int id, final CharSequence text) {
        ((TextView) dialog.findViewById(id)).setText(text);
    }

    /**
     * TextView にテキスト(HTML)を設定するヘルパー。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param text
     *            テキスト
     */
    public static void setHtml(final Activity activity, final int id, final CharSequence text) {
        ((TextView) activity.findViewById(id)).setText(Html.fromHtml(text
                .toString()));
    }

    /**
     * TextView にテキスト(HTML)を設定するヘルパー。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param text
     *            テキスト
     */
    public static void setHtml(final View v, final int id, final CharSequence text) {
        ((TextView) v.findViewById(id)).setText(Html.fromHtml(text.toString()));
    }

    /**
     * TextView にテキスト(HTML)を設定するヘルパー。
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param text
     *            テキスト
     */
    public static void setHtml(final Dialog dialog, final int id, final CharSequence text) {
        ((TextView) dialog.findViewById(id)).setText(Html.fromHtml(text
                .toString()));
    }

    /**
     * TextView からテキストを取得するヘルパー
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @return 文字列
     */
    public static String getText(final Activity activity, final int id) {
        return ((TextView) activity.findViewById(id)).getText().toString();
    }

    /**
     * TextView からテキストを取得するヘルパー
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @return 文字列
     */
    public static String getText(final View v, final int id) {
        return ((TextView) v.findViewById(id)).getText().toString();
    }

    /**
     * TextView からテキストを取得するヘルパー
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @return 文字列
     */
    public static String getText(final Dialog dialog, final int id) {
        return ((TextView) dialog.findViewById(id)).getText().toString();
    }

    /**
     * EditText からテキストを取得するヘルパー
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @return 文字列
     */
    public static String getEditText(final Activity activity, final int id) {
        return ((EditText) activity.findViewById(id)).getText().toString();
    }

    /**
     * EditText からテキストを取得するヘルパー
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @return 文字列
     */
    public static String getEditText(final View v, final int id) {
        return ((EditText) v.findViewById(id)).getText().toString();
    }

    /**
     * EditText からテキストを取得するヘルパー
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @return 文字列
     */
    public static String getEditText(final Dialog dialog, final int id) {
        return ((EditText) dialog.findViewById(id)).getText().toString();
    }

    /**
     * ImageView に Drawable リソースを設定するヘルパー。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param resourceId
     *            リソースID
     */
    public static void setImage(final Activity activity, final int id, final int resourceId) {
        ((ImageView) activity.findViewById(id)).setImageResource(resourceId);
    }

    /**
     * ImageView に Drawable オブジェクトを設定するヘルパー。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param drawable
     *            Drawable
     */
    public static void setImage(final Activity activity, final int id, final Drawable drawable) {
        ((ImageView) activity.findViewById(id)).setImageDrawable(drawable);
    }

    /**
     * ImageView に Bitmapを設定するヘルパー。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param bitmap
     *            Bitmap
     */
    public static void setImage(final Activity activity, final int id, final Bitmap bitmap) {
        ((ImageView) activity.findViewById(id)).setImageBitmap(bitmap);
    }

    /**
     * ImageView に Drawable リソースを設定するヘルパー。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param resourceId
     *            リソースID
     */
    public static void setImage(final View v, final int id, final int resourceId) {
        ((ImageView) v.findViewById(id)).setImageResource(resourceId);
    }

    /**
     * ImageView に Drawable オブジェクトを設定するヘルパー。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param drawable
     *            Drawable
     */
    public static void setImage(final View v, final int id, final Drawable drawable) {
        ((ImageView) v.findViewById(id)).setImageDrawable(drawable);
    }

    /**
     * ImageView Bitmapを設定するヘルパー。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param bitmap
     *            Bitmap
     */
    public static void setImage(final View v, final int id, final Bitmap bitmap) {
        ((ImageView) v.findViewById(id)).setImageBitmap(bitmap);
    }

    /**
     * ImageView に Drawable リソースを設定するヘルパー。
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param resourceId
     *            リソースID
     */
    public static void setImage(final Dialog dialog, final int id, final int resourceId) {
        ((ImageView) dialog.findViewById(id)).setImageResource(resourceId);
    }

    /**
     * ImageView に Drawable オブジェクトを設定するヘルパー。
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param drawable
     *            Drawable
     */
    public static void setImage(final Dialog dialog, final int id, final Drawable drawable) {
        ((ImageView) dialog.findViewById(id)).setImageDrawable(drawable);
    }

    /**
     * ImageView Bitmapを設定するヘルパー。
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param bitmap
     *            Bitmap
     */
    public static void setImage(final Dialog dialog, final int id, final Bitmap bitmap) {
        ((ImageView) dialog.findViewById(id)).setImageBitmap(bitmap);
    }

    /**
     * 背景を設定するヘルパー。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param resourceId
     *            リソースID
     */
    public static void setBackground(final Activity activity, final int id, final int resourceId) {
        activity.findViewById(id).setBackgroundResource(resourceId);
    }

    /**
     * 背景を設定するヘルパー。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param drawable
     *            Drawable
     */
    public static void setBackground(final Activity activity, final int id, final Drawable drawable) {
        activity.findViewById(id).setBackgroundDrawable(drawable);
    }

    /**
     * 背景を設定するヘルパー。
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param bitmap
     *            Bitmap
     */
    public static void setBackground(final Activity activity, final int id, final Bitmap bitmap) {
        activity.findViewById(id).setBackgroundDrawable(
                new BitmapDrawable(bitmap));
    }

    /**
     * 背景を設定するヘルパー。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param resourceId
     *            リソースID
     */
    public static void setBackground(final View v, final int id, final int resourceId) {
        v.findViewById(id).setBackgroundResource(resourceId);
    }

    /**
     * 背景を設定するヘルパー。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param drawable
     *            Drawable
     */
    public static void setBackground(final View v, final int id, final Drawable drawable) {
        v.findViewById(id).setBackgroundDrawable(drawable);
    }

    /**
     * 背景を設定するヘルパー。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param bitmap
     *            Bitmap
     */
    public static void setBackground(final View v, final int id, final Bitmap bitmap) {
        v.findViewById(id).setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    /**
     * 背景を設定するヘルパー。
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param resourceId
     *            リソースID
     */
    public static void setBackground(final Dialog dialog, final int id, final int resourceId) {
        dialog.findViewById(id).setBackgroundResource(resourceId);
    }

    /**
     * 背景を設定するヘルパー。
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param drawable
     *            Drawable
     */
    public static void setBackground(final Dialog dialog, final int id, final Drawable drawable) {
        dialog.findViewById(id).setBackgroundDrawable(drawable);
    }

    /**
     * 背景を設定するヘルパー。
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param bitmap
     *            Bitmap
     */
    public static void setBackground(final Dialog dialog, final int id, final Bitmap bitmap) {
        dialog.findViewById(id).setBackgroundDrawable(
                new BitmapDrawable(bitmap));
    }

    /**
     * ViewのVisibirityを設定するヘルパー
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param visibility
     *            {@link View#VISIBLE}、{@link View#INVISIBLE}、{@link View#GONE}
     */
    public static void setVisibility(final Activity activity, final int id, final int visibility) {
        activity.findViewById(id).setVisibility(visibility);
    }

    /**
     * ViewのVisibirityを設定するヘルパー
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param visibility
     *            {@link View#VISIBLE}、{@link View#INVISIBLE}、{@link View#GONE}
     */
    public static void setVisibility(final View v, final int id, final int visibility) {
        v.findViewById(id).setVisibility(visibility);
    }

    /**
     * ViewのVisibirityを設定するヘルパー
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param visibility
     *            {@link View#VISIBLE}、{@link View#INVISIBLE}、{@link View#GONE}
     */
    public static void setVisibility(final Dialog dialog, final int id, final int visibility) {
        dialog.findViewById(id).setVisibility(visibility);
    }

    /**
     * ViewのVisibilityを設定するヘルパー
     * 
     * @param activity
     *            アクティビティ
     * @param id
     *            id
     * @param visibility
     *            {@link View#VISIBLE}、{@link View#INVISIBLE}、{@link View#GONE}
     * @param animationKind
     *            アニメーション種類
     * @param duration
     *            アニメーション時間
     */
    public static void setVisibility(final Activity activity, final int id, final int visibility, final AnimationKind animationKind, final int duration) {
        setVisibility(activity.findViewById(id), visibility, animationKind, duration);
    }

    /**
     * ViewのVisibilityを設定するヘルパー
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param visibility
     *            {@link View#VISIBLE}、{@link View#INVISIBLE}、{@link View#GONE}
     * @param animationKind
     *            アニメーション種類
     * @param duration
     *            アニメーション時間
     */
    public static void setVisibility(final View v, final int id, final int visibility, final AnimationKind animationKind, final int duration) {
        setVisibility(v.findViewById(id), visibility, animationKind, duration);
    }

    /**
     * ViewのVisibilityを設定するヘルパー
     * 
     * @param dialog
     *            ダイアログ
     * @param id
     *            id
     * @param visibility
     *            {@link View#VISIBLE}、{@link View#INVISIBLE}、{@link View#GONE}
     * @param animationKind
     *            アニメーション種類
     * @param duration
     *            アニメーション時間
     */
    public static void setVisibility(final Dialog dialog, final int id, final int visibility, final AnimationKind animationKind, final int duration) {
        setVisibility(dialog.findViewById(id), visibility, animationKind, duration);
    }

    /**
     * フェードイン・フェードアウトしながら表示設定をするヘルパー
     * 
     * @param v
     *            View
     * @param visibility
     *            {@link View#VISIBLE}、{@link View#INVISIBLE}、{@link View#GONE}
     * @param animationKind
     *            {@link AnimationKind}
     * @param duration
     *            アニメーション時間
     */
    public static void setVisibility(final View v, final int visibility, final AnimationKind animationKind, final int duration) {
        // 同一の場合は何もしない。
        if (v.getVisibility() == visibility) {
            return;
        }
        // 指定以外の値が設定された場合は何もしない。
        if (visibility != View.VISIBLE && visibility != View.INVISIBLE
                && visibility != View.GONE) {
            return;
        }

        Animation animation = null;
        if (animationKind == AnimationKind.FADE) {
            if (visibility == View.VISIBLE) {
                animation = new AlphaAnimation(0, 1);
            } else {
                animation = new AlphaAnimation(1, 0);
            }
        } else if (animationKind == AnimationKind.SCALE) {
            if (visibility == View.VISIBLE) {
                animation = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 1);
            } else {
                animation = new ScaleAnimation(1, 1, 1, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
            }
        } else if (animationKind == AnimationKind.TRANSLATE) {
            if (visibility == View.VISIBLE) {
                animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
            } else {
                animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
            }
        }
        if (animation != null) {
            animation.setDuration(duration);
            animation.setRepeatCount(0);
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(final Animation animation) {
                    v.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(final Animation animation) {
                    // 何もしない。
                }

                @Override
                public void onAnimationEnd(final Animation animation) {
                    v.setVisibility(visibility);
                }
            });
            v.startAnimation(animation);
        }
    }

    /**
     * アルファアニメーションで表示する。
     * 
     * @param v
     *            View
     * @param duration
     *            アニメーション時間
     */
    public static void setAlphaInAnimation(final View v, final int duration) {
        Animation animation = null;
        animation = new AlphaAnimation(0, 1);
        animation.setDuration(duration);
        animation.setRepeatCount(0);
        v.startAnimation(animation);
    }

    /**
     * アルファアニメーションで非表示にする。
     * 
     * @param v
     *            View
     * @param duration
     *            アニメーション時間
     */
    public static void setAlphaOutAnimation(final View v, final int duration) {
        Animation animation = null;
        animation = new AlphaAnimation(1, 0);
        animation.setDuration(duration);
        animation.setRepeatCount(0);
        v.startAnimation(animation);
    }

    /**
     * 文字を指定回数で点滅させる
     * 
     * @param v
     *            View
     * @param duration
     *            点滅間隔
     * @param blinkCount
     *            点滅回数
     */
    public static void setBlink(final View v, final int duration, final int blinkCount) {
        final AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(duration);
        alpha.setRepeatMode(Animation.REVERSE);
        alpha.setRepeatCount(blinkCount);
        alpha.setFillEnabled(true);
        alpha.setFillAfter(true);
        alpha.setFillBefore(false);
        v.startAnimation(alpha);
    }

    /**
     * 無限に文字を点滅させる
     * 
     * @param v
     *            View
     * @param duration
     *            点滅間隔
     */
    public static void setBlink(final View v, final int duration) {
        final AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(duration);
        alpha.setRepeatMode(Animation.REVERSE);
        alpha.setRepeatCount(Animation.INFINITE);
        alpha.setFillEnabled(true);
        alpha.setFillAfter(true);
        alpha.setFillBefore(false);
        v.startAnimation(alpha);
    }

    /**
     * クリック・プレス・有効有無を設定するヘルパー<br>
     * 主にボタンに使用し、ボタンを有効無効を制御する。
     * 
     * @param v
     *            View
     * @param id
     *            id
     * @param b
     *            true:クリック許容/false:クリック不可
     */
    public static void setClickable(final View v, final int id, final boolean b) {
        if (b) {
            v.findViewById(id).setClickable(true);
            v.findViewById(id).setPressed(false);
            v.findViewById(id).setEnabled(true);
        } else {
            v.findViewById(id).setClickable(false);
            v.findViewById(id).setPressed(true);
            v.findViewById(id).setEnabled(false);
        }
    }

    /**
     * クリック・プレス・有効有無を設定するヘルパー<br>
     * 主にボタンに使用し、ボタンを有効無効を制御する。
     * 
     * @param v
     *            View
     * @param b
     *            true:クリック許容/false:クリック不可
     */
    public static void setClickable(final View v, final boolean b) {
        if (b) {
            v.setClickable(true);
            v.setPressed(false);
            v.setEnabled(true);
        } else {
            v.setClickable(false);
            v.setPressed(true);
            v.setEnabled(false);
        }
    }

    /**
     * CustomTextViewをリンク可能にさせる
     * 
     * @param <T>
     *            起動クラス
     * 
     * @param textView
     *            TextView
     * @param linkString
     *            リンク文字列
     * @param clazz
     *            クラス
     */
    public static <T> void addLinks(final TextView textView, final String linkString, final Class<T> clazz) {
        final Pattern pattern = Pattern.compile(linkString);
        final SpannableString spannable = SpannableString.valueOf(textView
                .getText());
        final Matcher matcher = pattern.matcher(spannable);

        // Create class for each match
        while (matcher.find()) {
            try {
                spannable.setSpan(clazz.newInstance(), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (final IllegalAccessException e) {
                // 握りつぶす
            } catch (final InstantiationException e) {
                // 握りつぶす
            }
        }

        // Set new spans in CustomTextView
        textView.setText(spannable);

        // Listen for spannable clicks, if not already
        final MovementMethod m = textView.getMovementMethod();
        if (!(m instanceof LinkMovementMethod)) {
            if (textView.getLinksClickable()) {
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    /**
     * View配下の内容を全てクリアする
     * 
     * @param context
     *            Context
     * @param object
     *            対象のActivity/Fragment等
     * @param view
     *            View
     */
    public static void cleanView(final Context context, final Object object, final View view) {
        if (view != null) {
            // ---------------------------------------------------------------
            // CompoundButton
            // ---------------------------------------------------------------
            if (view instanceof CompoundButton) {
                final CompoundButton compoundButton = (CompoundButton) view;
                compoundButton.setOnCheckedChangeListener(null);
                compoundButton.setButtonDrawable(null);
                compoundButton.setCompoundDrawables(null, null, null, null);
            }
            // ---------------------------------------------------------------
            // ImageView
            // ---------------------------------------------------------------
            else if (view instanceof ImageView) {
                final ImageView imageView = (ImageView) view;

                if (imageView.getDrawable() instanceof AnimationDrawable) {
                    final AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                            .getDrawable();
                    final int frames = animationDrawable
                            .getNumberOfFrames();
                    for (int i = 0; i < frames; ++i) {
                        final Drawable frame = animationDrawable.getFrame(i);
                        frame.setCallback(null);
                    }
                }

                if (imageView.getBackground() instanceof AnimationDrawable) {
                    final AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                            .getBackground();
                    final int frames = animationDrawable
                            .getNumberOfFrames();
                    for (int i = 0; i < frames; ++i) {
                        final Drawable frame = animationDrawable
                                .getFrame(i);
                        frame.setCallback(null);
                    }
                }

                if (imageView.getDrawable() != null) {
                    imageView.getDrawable().setCallback(null);
                }
                imageView.setImageDrawable(null);
                // } else if (view instanceof CustomTextView) {
            }
            // ---------------------------------------------------------------
            // CustomListView(PullToRefreshListView)
            // ---------------------------------------------------------------
            else if (view instanceof CustomListView) {
                final CustomListView customListView = (CustomListView) view;
                customListView.setOnRefreshListener((OnRefreshListener2<ListView>) null);
                customListView.setOnItemClickListener(null);
                customListView.setOnLastItemVisibleListener(null);
                customListView.setOnScrollListener(null);

                ListView listView = customListView.getRefreshableView();
                if (listView != null) {
                    try {
                        final Field field = ClassUtils.getField(
                                listView.getClass(), "mHeaderViewInfos");
                        final ArrayList<FixedViewInfo> mHeaderViewInfos = (ArrayList<FixedViewInfo>) field
                                .get(listView);
                        if (!CollectionUtils.isNullOrEmpty(mHeaderViewInfos)) {
                            for (final FixedViewInfo mHeaderViewInfo : mHeaderViewInfos) {
                                listView.removeHeaderView(mHeaderViewInfo.view);
                                cleanView(context, object, mHeaderViewInfo.view);
                            }
                        }
                    } catch (final Exception e) {
                        // 握りつぶす
                    }
                    try {
                        final Field field = ClassUtils.getField(
                                listView.getClass(), "mFooterViewInfos");
                        final ArrayList<FixedViewInfo> mFooterViewInfos = (ArrayList<FixedViewInfo>) field
                                .get(listView);
                        if (!CollectionUtils.isNullOrEmpty(mFooterViewInfos)) {
                            for (final FixedViewInfo mFooterViewInfo : mFooterViewInfos) {
                                listView.removeHeaderView(mFooterViewInfo.view);
                                cleanView(context, object, mFooterViewInfo.view);
                            }
                        }
                    } catch (final Exception e) {
                        // 握りつぶす
                    }
                    final int count = listView.getCount();
                    for (int i = 0; i < count; i++) {
                        cleanView(context, object, listView.getChildAt(i));
                    }
                    listView.setOnScrollListener(null);
                    listView.setAdapter(null);
                }
            }
            // ---------------------------------------------------------------
            // ListView
            // ---------------------------------------------------------------
            else if (view instanceof ListView) {
                final ListView listView = (ListView) view;
                try {
                    final Field field = ClassUtils.getField(
                            listView.getClass(), "mHeaderViewInfos");
                    final ArrayList<FixedViewInfo> mHeaderViewInfos = (ArrayList<FixedViewInfo>) field
                            .get(listView);
                    if (!CollectionUtils.isNullOrEmpty(mHeaderViewInfos)) {
                        for (final FixedViewInfo mHeaderViewInfo : mHeaderViewInfos) {
                            listView.removeHeaderView(mHeaderViewInfo.view);
                            cleanView(context, object, mHeaderViewInfo.view);
                        }
                    }
                } catch (final Exception e) {
                    // 握りつぶす
                }
                try {
                    final Field field = ClassUtils.getField(
                            listView.getClass(), "mFooterViewInfos");
                    final ArrayList<FixedViewInfo> mFooterViewInfos = (ArrayList<FixedViewInfo>) field
                            .get(listView);
                    if (!CollectionUtils.isNullOrEmpty(mFooterViewInfos)) {
                        for (final FixedViewInfo mFooterViewInfo : mFooterViewInfos) {
                            listView.removeHeaderView(mFooterViewInfo.view);
                            cleanView(context, object, mFooterViewInfo.view);
                        }
                    }
                } catch (final Exception e) {
                    // 握りつぶす
                }
                final int count = listView.getCount();
                for (int i = 0; i < count; i++) {
                    cleanView(context, object, listView.getChildAt(i));
                }
                listView.setOnScrollListener(null);
                listView.setOnItemClickListener(null);
                listView.setAdapter(null);
            }
            // ---------------------------------------------------------------
            // GridView
            // ---------------------------------------------------------------
            else if (view instanceof GridView) {
                final GridView gridView = (GridView) view;
                final int count = gridView.getCount();
                for (int i = 0; i < count; i++) {
                    cleanView(context, object, gridView.getChildAt(i));
                }
                gridView.setOnItemClickListener(null);
                gridView.setAdapter(null);
            }
            // ---------------------------------------------------------------
            // Gallery
            // ---------------------------------------------------------------
            else if (view instanceof Gallery) {
                final Gallery gallery = (Gallery) view;
                final int count = gallery.getCount();
                for (int i = 0; i < count; i++) {
                    cleanView(context, object, gallery.getChildAt(i));
                }
                gallery.setAdapter(null);
            }
            // ---------------------------------------------------------------
            // RadioGroup
            // ---------------------------------------------------------------
            else if (view instanceof RadioGroup) {
                final RadioGroup radioGroup = (RadioGroup) view;
                radioGroup.setOnCheckedChangeListener(null);
            }
            // ---------------------------------------------------------------
            // SeekBar
            // ---------------------------------------------------------------
            else if (view instanceof SeekBar) {
                ((SeekBar) view).setProgressDrawable(null);
                ((SeekBar) view).setThumb(null);
            }
            // ---------------------------------------------------------------
            // Spinner
            // ---------------------------------------------------------------
            else if (view instanceof Spinner) {
                final Spinner spinner = (Spinner) view;
                spinner.setOnItemSelectedListener(null);
            }
            // ---------------------------------------------------------------
            // ViewPager
            // ---------------------------------------------------------------
            else if (view instanceof ViewPager) {
                try {
                    final ViewPager viewPager = (ViewPager) view;
                    viewPager.setOnPageChangeListener(null);
                    viewPager.setAdapter(null);
                } catch (final Throwable mayHappen) {
                    // 何もしない。
                }
            }
            // ---------------------------------------------------------------
            // WebView
            // ---------------------------------------------------------------
            else if (view instanceof WebView) {
                try {
                    final WebView webView = (WebView) view;
                    webView.setWebChromeClient(null);
                    webView.setWebViewClient(null);
                } catch (final Exception e) {
                    // 何もしない。
                }
                try {

                    Field field = WebView.class.getDeclaredField("mWebViewCore");
                    field = field.getType().getDeclaredField("mBrowserFrame");
                    field = field.getType().getDeclaredField("sConfigCallback");
                    field.setAccessible(true);
                    final Object configCallback = field.get(null);

                    if (configCallback != null) {
                        field = field.getType().getDeclaredField("mWindowManager");
                        field.setAccessible(true);
                        if (context instanceof Activity) {
                            field.set(configCallback, ((Activity) context).getWindowManager());
                        }
                    }

                } catch (final Exception e) {
                    // 何もしない。
                }

                try {
                    Field field = WebView.class.getDeclaredField("mWebView");
                    field = field.getType().getDeclaredField("mBrowserFrame");
                    field = field.getType().getDeclaredField("sConfigCallback");
                    field.setAccessible(true);
                    field.get(null);
                    final Object configCallback = field.get(null);
                    field = field.getType().getDeclaredField("mWindowManager");
                    field.setAccessible(true);
                    if (configCallback != null) {
                        field = field.getType().getDeclaredField("mWindowManager");
                        field.setAccessible(true);
                        if (context instanceof Activity) {
                            field.set(configCallback, ((Activity) context).getWindowManager());
                        }
                    }
                } catch (final Exception e) {
                    // 何もしない。
                }
            }
        }
        // ---------------------------------------------------------------
        // TextView
        // ---------------------------------------------------------------
        if (view instanceof TextView) {
            final TextView textView = (TextView) view;
            textView.setCompoundDrawables(null, null, null, null);
            textView.setOnEditorActionListener(null);
        }
        // ---------------------------------------------------------------
        // View共通
        // ---------------------------------------------------------------
        if (view != null) {
            try {
                view.setOnClickListener(null);
            } catch (final Throwable mayHappen) {
                // 何もしない。
            }
            try {
                view.setOnTouchListener(null);
            } catch (final Throwable mayHappen) {
                // 何もしない。
            }
            try {
                view.setOnCreateContextMenuListener(null);
            } catch (final Throwable mayHappen) {
                // 何もしない。
            }
            try {
                view.setOnFocusChangeListener(null);
            } catch (final Throwable mayHappen) {
                // 何もしない。
            }
            try {
                view.setOnKeyListener(null);
            } catch (final Throwable mayHappen) {
                // 何もしない。
            }
            try {
                view.setOnLongClickListener(null);
            } catch (final Throwable mayHappen) {
                // 何もしない。
            }
            try {
                view.clearAnimation();
            } catch (final Throwable mayHappen) {
                // 何もしない。
            }

            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            view.setBackgroundDrawable(null);
            view.setTag(null);
        }
        if (view instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) view;
            final int size = vg.getChildCount();
            for (int i = 0; i < size; i++) {
                final View childView = vg.getChildAt(i);
                cleanView(context, object, childView);
            }
        }
    }

    /**
     * 3.0以前のバックグラウンドのrepeatが効かないのを対応する。
     * 
     * @param view
     *            {@link View}
     */
    public static void fixBackgroundRepeat(final View view) {
        if (view != null) {
            final Drawable bg = view.getBackground();
            if (bg instanceof BitmapDrawable) {
                final BitmapDrawable bmp = (BitmapDrawable) bg;
                bmp.mutate(); // make sure that we aren't sharing state anymore
                bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            }
        }
    }

    /**
     * String型でリソースIDを取得する
     * 
     * @param targetResourcesName
     *            指定したいリソースID<br>
     *            R.id.Button01ならば、Button01を指定する
     * 
     * @param targetResources
     *            指定したいリソースID<br>
     *            R.id.Button01ならば、idを指定する
     * 
     * @param context
     *            getApplicationContext()を設定する。
     * 
     * @return リソースID
     */
    public static int getResourcesId(final String targetResourcesName, final String targetResources, final Context context) {
        // リソースIDを取得
        return context.getResources().getIdentifier(targetResourcesName, targetResources, context.getPackageName());
    }

    /**
     * ステータスバーの高さ取得
     * 
     * @param context
     *            Context
     * @return ステータスバーの高さ
     */
    public static int getStatusBarHeight(final Context context) {
        int statusBarHeight = 0;
        final Window w = ((Activity) context).getWindow();
        if (w != null) {
            final Rect r = new Rect();
            w.getDecorView().getWindowVisibleDisplayFrame(r);
            statusBarHeight = r.top;
        }
        if (statusBarHeight == 0) {
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getMetrics(displayMetrics);
            switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                statusBarHeight = HIGH_DPI_STATUS_BAR_HEIGHT;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
                break;
            case DisplayMetrics.DENSITY_LOW:
                statusBarHeight = LOW_DPI_STATUS_BAR_HEIGHT;
                break;
            case DisplayMetrics.DENSITY_XHIGH: // XHIGH
                statusBarHeight = XHIGH_DPI_STATUS_BAR_HEIGHT;
                break;
            default:
                statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
            }
        }
        return statusBarHeight;
    }
}
