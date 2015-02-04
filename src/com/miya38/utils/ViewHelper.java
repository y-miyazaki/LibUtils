package com.miya38.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ListView.FixedViewInfo;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * ビューを利用する際のヘルパーを提供します。
 * 
 * @author y-miyazaki
 */
@SuppressWarnings("unchecked")
public final class ViewHelper {
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
	 */
	public static void setText(final View v, final int id, final int stringId) {
		if (stringId > 0) {
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
	 */
	public static void setText(final Activity activity, final int id, final int stringId) {
		if (stringId > 0) {
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
	 */
	public static void setText(final Dialog dialog, final int id, final int stringId) {
		if (stringId > 0) {
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
	 */
	public static void setHtml(final Activity activity, final int id, final CharSequence text) {
		((TextView) activity.findViewById(id)).setText(Html.fromHtml(text.toString()));
	}

	/**
	 * TextView にテキスト(HTML)を設定するヘルパー。
	 * 
	 * @param v
	 *            View
	 * @param id
	 *            id
	 * @param text
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
	 */
	public static void setHtml(final Dialog dialog, final int id, final CharSequence text) {
		((TextView) dialog.findViewById(id)).setText(Html.fromHtml(text.toString()));
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
	 */
	@SuppressWarnings("deprecation")
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
	 */
	@SuppressWarnings("deprecation")
	public static void setBackground(final Activity activity, final int id, final Bitmap bitmap) {
		activity.findViewById(id).setBackgroundDrawable(new BitmapDrawable(bitmap));
	}

	/**
	 * 背景を設定するヘルパー。
	 * 
	 * @param v
	 *            View
	 * @param id
	 *            id
	 * @param resourceId
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
	 */
	@SuppressWarnings("deprecation")
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
	 */
	@SuppressWarnings("deprecation")
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
	 */
	@SuppressWarnings("deprecation")
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
	 */
	@SuppressWarnings("deprecation")
	public static void setBackground(final Dialog dialog, final int id, final Bitmap bitmap) {
		dialog.findViewById(id).setBackgroundDrawable(new BitmapDrawable(bitmap));
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
	 * @param duration
	 *            アニメーション時間
	 */
	public static void setVisibility(final View v, final int visibility, final AnimationKind animationKind, final int duration) {
		// 同一の場合は何もしない。
		if (v.getVisibility() == visibility) {
			return;
		}
		// 指定以外の値が設定された場合は何もしない。
		if (visibility != View.VISIBLE && visibility != View.INVISIBLE && visibility != View.GONE) {
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
	 * クリック・プレス・有効有無を設定するヘルパー<br>
	 * 主にボタンに使用し、ボタンを有効無効を制御する。
	 * 
	 * @param v
	 * @param id
	 *            id
	 * @param b
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
	 * @param b
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
	 * 
	 * @param textView
	 * @param linkString
	 * @param clazz
	 */
	public static <T> void addLinks(final TextView textView, final String linkString, final Class<T> clazz) {
		final Pattern pattern = Pattern.compile(linkString);
		final SpannableString spannable = SpannableString.valueOf(textView.getText());
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
	 * 横サイズを変化させるアニメーションを実行します。
	 * 
	 * @param v
	 * @param duration
	 * @param fromWidth
	 * @param toWidth
	 * @return Animation
	 */
	public static Animation widthAnimation(final View v, final int duration, final int fromWidth, final int toWidth) {
		try {
			final Method m = v.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
			m.setAccessible(true);
			m.invoke(v, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(((View) v.getParent()).getMeasuredWidth(), MeasureSpec.AT_MOST));
		} catch (final Exception e) {
		}
		final boolean isIncrement = fromWidth < toWidth;

		v.getLayoutParams().width = fromWidth;
		v.setVisibility(View.VISIBLE);
		final Animation a = new Animation() {
			@Override
			protected void applyTransformation(final float interpolatedTime, final Transformation t) {
				int diff;
				if (isIncrement) {
					diff = toWidth - fromWidth;
				} else {
					diff = fromWidth - toWidth;
				}
				final int newWidth = isIncrement ? //
				(int) (fromWidth + diff * interpolatedTime) //
						: (int) (toWidth + diff * (1 - interpolatedTime));
				v.getLayoutParams().width = newWidth;
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setDuration(duration);
		v.startAnimation(a);
		return a;
	}

	/**
	 * 縦サイズを変化させるアニメーションを実行します。
	 * 
	 * @param v
	 * @param duration
	 * @param fromHeight
	 * @param toHeight
	 * @return Animation
	 */
	public static Animation heightAnimation(final View v, final int duration, final int fromHeight, final int toHeight) {
		try {
			final Method m = v.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
			m.setAccessible(true);
			m.invoke(v, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(((View) v.getParent()).getMeasuredWidth(), MeasureSpec.AT_MOST));
		} catch (final Exception e) {
			// 何もしない。
		}
		final boolean isIncrement = fromHeight < toHeight;

		v.getLayoutParams().height = fromHeight;
		v.setVisibility(View.VISIBLE);
		final Animation a = new Animation() {
			@Override
			protected void applyTransformation(final float interpolatedTime, final Transformation t) {
				int diff;
				if (isIncrement) {
					diff = toHeight - fromHeight;
				} else {
					diff = fromHeight - toHeight;
				}
				final int newHeight = isIncrement ? //
				(int) (fromHeight + diff * interpolatedTime) //
						: (int) (toHeight + diff * (1 - interpolatedTime));
				v.getLayoutParams().height = newHeight;
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setDuration(duration);
		v.startAnimation(a);
		return a;
	}

	/**
	 * View配下の内容を全てクリアする
	 * 
	 * @param object
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public static void cleanView(final Object object, final View view) {
		if (view != null) {
			if (view instanceof ImageButton) {
				final ImageButton imageButton = (ImageButton) view;
				if (imageButton.getDrawable() != null) {
					imageButton.getDrawable().setCallback(null);
				}
				imageButton.setImageDrawable(null);
			} else if (view instanceof ImageView) {
				final ImageView imageView = (ImageView) view;

				if (imageView.getDrawable() instanceof AnimationDrawable) {
					final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
					final int frames = animationDrawable.getNumberOfFrames();
					for (int i = 0; i < frames; ++i) {
						final Drawable frame = animationDrawable.getFrame(i);
						if (frame instanceof BitmapDrawable) {
							// ((BitmapDrawable) frame).getBitmap().recycle();
						}
						frame.setCallback(null);
					}
				}

				if (imageView.getBackground() instanceof AnimationDrawable) {
					final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
					final int frames = animationDrawable.getNumberOfFrames();
					for (int i = 0; i < frames; ++i) {
						final Drawable frame = animationDrawable.getFrame(i);
						if (frame instanceof BitmapDrawable) {
							// ((BitmapDrawable) frame).getBitmap().recycle();
						}
						frame.setCallback(null);
					}
				}

				if (imageView.getDrawable() != null) {
					imageView.getDrawable().setCallback(null);
				}
				imageView.setImageDrawable(null);
				// } else if (view instanceof CustomTextView) {
			} else if (view instanceof SeekBar) {
				((SeekBar) view).setProgressDrawable(null);
				((SeekBar) view).setThumb(null);
			} else if (view instanceof ListView) {
				final ListView listView = (ListView) view;
				try {
					final Field field = ClassUtils.getField(listView.getClass(), "mHeaderViewInfos");
					final ArrayList<FixedViewInfo> mHeaderViewInfos = (ArrayList<FixedViewInfo>) field.get(listView);
					if (!CollectionUtils.isNullOrEmpty(mHeaderViewInfos)) {
						for (final FixedViewInfo mHeaderViewInfo : mHeaderViewInfos) {
							listView.removeHeaderView(mHeaderViewInfo.view);
							cleanView(object, mHeaderViewInfo.view);
						}
					}
				} catch (final Exception e) {
					// 握りつぶす
				}
				try {
					final Field field = ClassUtils.getField(listView.getClass(), "mFooterViewInfos");
					final ArrayList<FixedViewInfo> mFooterViewInfos = (ArrayList<FixedViewInfo>) field.get(listView);
					if (!CollectionUtils.isNullOrEmpty(mFooterViewInfos)) {
						for (final FixedViewInfo mFooterViewInfo : mFooterViewInfos) {
							listView.removeHeaderView(mFooterViewInfo.view);
							cleanView(object, mFooterViewInfo.view);
						}
					}
				} catch (final Exception e) {
					// 握りつぶす
				}
				final int count = listView.getCount();
				for (int i = 0; i < count; i++) {
					cleanView(object, listView.getChildAt(i));
				}
				listView.setOnItemClickListener(null);
				listView.setAdapter(null);
			} else if (view instanceof GridView) {
				final GridView gridView = (GridView) view;
				final int count = gridView.getCount();
				for (int i = 0; i < count; i++) {
					cleanView(object, gridView.getChildAt(i));
				}
				gridView.setOnItemClickListener(null);
				gridView.setAdapter(null);
			} else if (view instanceof Gallery) {
				final Gallery gallery = (Gallery) view;
				final int count = gallery.getCount();
				for (int i = 0; i < count; i++) {
					cleanView(object, gallery.getChildAt(i));
				}
				gallery.setAdapter(null);
			} else if (view instanceof WebView) {
				final WebView webView = (WebView) view;
				webView.stopLoading();
				webView.setWebChromeClient(null);
				webView.setWebViewClient(null);
				webView.destroyDrawingCache();
				webView.removeAllViews();
				webView.destroy();
			}
			// } else if (view instanceof LinearLayout) {
			// // 何もしない。
			// } else if (view instanceof RelativeLayout) {
			// // 何もしない。
			// } else if (view instanceof TextView) {
			// // 何もしない。
			// } else if (view instanceof Button) {
			// // 何もしない。
			// }
			try {
				view.setOnClickListener(null);
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
				view.setOnClickListener(null);
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

			if (view instanceof ViewGroup) {
				final ViewGroup vg = (ViewGroup) view;
				final int size = vg.getChildCount();
				for (int i = 0; i < size; i++) {
					final View childView = vg.getChildAt(i);
					vg.removeView(childView);
					cleanView(object, childView);
				}
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
			((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
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
			case 320: // XHIGH
				statusBarHeight = XHIGH_DPI_STATUS_BAR_HEIGHT;
				break;
			default:
				statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
			}
		}
		return statusBarHeight;
	}
}
