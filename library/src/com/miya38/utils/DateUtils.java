package com.miya38.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Date に関するユーティリティメソッドを提供します。
 * 
 * @author y-miyazaki
 * 
 */
public final class DateUtils {
    /**
     * 1秒のmsec
     */
    private static final int TIME_MSEC_SEC = 1000;
    /**
     * 1分間のmsec
     */
    private static final int TIME_MSEC_ONEMIN = 60000;
    /**
     * 1時間のmsec
     */
    private static final int TIME_MSEC_ONEHOUR = 3600000;
    /**
     * 1日のmsec
     */
    private static final int TIME_MSEC_ONEDAY = 86400000;
    /**
     * 曜日配列(日本語)
     */
    public static final String[] WEEK_JP = { "日", "月", "火", "水", "木", "金", "土" };
    /**
     * 曜日配列(英語)
     */
    public static final String[] WEEK_EU = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
    /**
     * フォーマット
     */
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    /**
     * フォーマット<br>
     * yyyy/MM/dd HH:mm:ss z
     */
    public static final String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss z";
    /**
     * フォーマット<br>
     * yyyy/MM/dd HH:mm
     */
    public static final String DATETIME_FORMAT1 = "yyyy/MM/dd HH:mm";
    /**
     * フォーマット<br>
     * yyyy/MM/dd HH:mm:ss
     */
    public static final String DATETIME_FORMAT2 = "yyyy/MM/dd HH:mm:ss";
    /**
     * フォーマット<br>
     * MM/dd
     */
    public static final String DATETIME_FORMAT3 = "MM/dd";
    /**
     * フォーマット<br>
     * yyyyMMdd HH:mm:ss
     */
    public static final String DATETIME_FORMAT4 = "yyyyMMdd HH:mm:ss";
    /**
     * フォーマット<br>
     * HH:mm:ss
     */
    public static final String DATETIME_FORMAT5 = "HH:mm:ss";
    /**
     * フォーマット<br>
     * yyyyMMddHHmmss
     */
    public static final String DATETIME_FORMAT6 = "yyyyMMddHHmmss";
    /**
     * フォーマット<br>
     * yyyy-MM-dd'T'HH:mm:ssZ
     */
    public static final String DATETIME_FORMAT_ISO_RFC822 = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private DateUtils() {
    }

    /**
     * 文字列フォーマット<br>
     * yyyy/MM/dd HH:mm:ss z形式で返却する
     * 
     * @param date
     *            {@link Date}
     * @return フォーマットされた文字列
     */
    public static String toString(final Date date) {
        if (null == date) {
            return null;
        }
        return new SimpleDateFormat(DATETIME_FORMAT, Locale.JAPAN).format(date);
    }

    /**
     * 文字列フォーマット
     * 
     * @param date
     *            {@link Date}
     * @param pattern
     *            日付フォーマット(ex:yyyy/MM/dd HH:mm:ss z)
     * @return フォーマットされた文字列
     */
    public static String toString(final Date date, final String pattern) {
        if (null == date) {
            return null;
        }
        final DateFormat format = new SimpleDateFormat(pattern, Locale.JAPAN);
        return format.format(date);
    }

    /**
     * 数値フォーマット<br>
     * 指定のパターン形式で返却する
     * 
     * @param year
     *            年(yyyy)
     * @param month
     *            月(mm)
     * @param day
     *            日(dd)
     * @param pattern
     *            日付フォーマット(ex:yyyy/MM/dd HH:mm:ss z)
     * @return フォーマットされた文字列
     */
    public static String toString(final int year, final int month, final int day, final String pattern) {
        try {
            final DateFormat format1 = new SimpleDateFormat("yyyymmdd", Locale.JAPAN);
            final DateFormat format2 = new SimpleDateFormat(pattern, Locale.JAPAN);
            return format2.format(format1.parse(String.format("%04d%02d%02d", new ParsePosition(0))));
        } catch (final ParseException e) {
            return null;
        }
    }

    /**
     * 数値フォーマット<br>
     * yyyy/MM/dd HH:mm:ss z形式で返却する
     * 
     * @param year
     *            年(yyyy)
     * @param month
     *            月(mm)
     * @param day
     *            日(dd)
     * @return フォーマットされた文字列
     */
    public static String toString(final int year, final int month, final int day) {
        try {
            final DateFormat format1 = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
            final DateFormat format2 = new SimpleDateFormat(DATETIME_FORMAT, Locale.JAPAN);
            return format2.format(format1.parse(String.format("%04d%02d%02d", new ParsePosition(0))));
        } catch (final ParseException e) {
            return null;
        }
    }

    /**
     * 文字列からDate型へのパース
     * 
     * @param date
     *            日付
     * @return パースされたDate型
     */
    public static Date parse(final String date) {
        return parse(date, (Date) null);
    }

    /**
     * 文字列からDate型へのパース
     * 
     * @param date
     *            日付
     * @param pattern
     *            日付フォーマット(ex:yyyy/MM/dd HH:mm:ss z)
     * @return パースされたDate型
     */
    public static Date parse(final String date, final String pattern) {
        return parse(date, null, pattern);
    }

    /**
     * 文字列からDate型へのパース
     * 
     * @param date
     *            日付
     * @param defaultValue
     *            パース失敗時のデフォルトDate
     * @return パースされたDate型
     */
    public static Date parse(final String date, final Date defaultValue) {
        try {
            return new SimpleDateFormat(DATETIME_FORMAT, Locale.JAPAN).parse(date);
        } catch (final ParseException e) {
            return defaultValue;
        }
    }

    /**
     * 文字列からDate型へのパース
     * 
     * @param date
     *            日付
     * @param defaultValue
     *            パース失敗時のデフォルトDate
     * @param pattern
     *            日付フォーマット(ex:yyyy/MM/dd HH:mm:ss z)
     * @return パースされたDate型
     */
    public static Date parse(final String date, final Date defaultValue, final String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.JAPAN).parse(date);
        } catch (final ParseException e) {
            return defaultValue;
        }
    }

    /**
     * date型で返却
     * 
     * @param year
     *            年(yyyy)
     * @param month
     *            月(mm)
     * @param date
     *            日(dd)
     * @return Date型
     */
    public static Date date(final int year, final int month, final int date) {
        final Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DATE, date);
        setZero(c);
        return c.getTime();
    }

    /**
     * date型で返却
     * 
     * @param year
     *            年(yyyy)
     * @param month
     *            月(mm)
     * @param date
     *            日(dd)
     * @param hour
     *            時間(hh)
     * @param min
     *            分(mm)
     * @param sec
     *            秒(ss)
     * @return Date型
     */
    public static Date date(final int year, final int month, final int date, final int hour, final int min, final int sec) {
        final Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DATE, date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, sec);
        setZero(c);
        return c.getTime();
    }

    /**
     * unixtimeから日付を指定されたフォーマットで返却する
     * 
     * @param unixtime
     *            UNIXTIME
     * @param pattern
     *            {@link Pattern}
     * @return 現在時間文字列
     */
    public static String getDate(final int unixtime, final String pattern) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.JAPAN);
        return simpleDateFormat.format(new Date((long) unixtime * TIME_MSEC_SEC));
    }

    /**
     * 現在時間
     * 
     * @return 現在時間
     */
    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 現在時間
     * 
     * @return 現在時間文字列
     */
    public static String nowToString() {
        return toString(now());
    }

    /**
     * 現在時間
     * 
     * @param pattern
     *            {@link Pattern}
     * @return 現在時間文字列
     */
    public static String nowToString(final String pattern) {
        return toString(now(), pattern);
    }

    /**
     * 元の日付から追加で月を更新する
     * 
     * @param date
     *            元の時間
     * @param addMonths
     *            月
     * @return 更新された時間
     */
    public static Date addMonth(final Date date, final int addMonths) {
        final Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.add(Calendar.MONTH, addMonths);

        return c.getTime();
    }

    /**
     * 元の日付から追加で日付を更新する
     * 
     * @param date
     *            元の時間
     * @param addDays
     *            日付
     * @return 更新された時間
     */
    public static Date addDay(final Date date, final int addDays) {
        final Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.add(Calendar.DATE, addDays);

        return c.getTime();
    }

    /**
     * 元の時間から追加で時間を更新する
     * 
     * @param date
     *            元の時間
     * @param addHours
     *            追加される時間
     * @return 更新された時間
     */
    public static Date addHour(final Date date, final int addHours) {
        final Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.add(Calendar.HOUR, addHours);

        return c.getTime();
    }

    /**
     * 元の時間から追加で分数を更新する
     * 
     * @param date
     *            元の時間
     * @param addMinutes
     *            分数
     * @return 更新された時間
     */
    public static Date addMinutes(final Date date, final int addMinutes) {
        final Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.add(Calendar.MINUTE, addMinutes);

        return c.getTime();
    }

    /**
     * 元の時間から追加で秒数を更新する
     * 
     * @param date
     *            元の時間
     * @param addSeconds
     *            秒数
     * @return 更新された時間
     */
    public static Date addSecond(final Date date, final int addSeconds) {
        final Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.add(Calendar.SECOND, addSeconds);

        return c.getTime();
    }

    /**
     * 指定した日付が「新しい」かどうか判別します。
     * 
     * 「新しい」と判定する期間は、freshDays に指定した日にちの間とします。
     * 
     * @param date
     *            指定されたDate
     * @param freshDays
     *            指定された新しい時間
     * @return 新しい場合はtrue, 古い場合はfalse
     */
    public static boolean isFreshDate(final Date date, final int freshDays) {
        return addDay(now(), -freshDays).before(date);
    }

    /**
     * 指定した日付が「新しい」かどうか判別します。
     * 
     * 「新しい」と判定する期間は、freshHours に指定した時間の間とします。
     * 
     * @param date
     *            指定されたDate
     * @param freshHours
     *            指定された新しい時間
     * @return 新しい場合はtrue, 古い場合はfalse
     */
    public static boolean isFreshHour(final Date date, final int freshHours) {
        return addHour(now(), -freshHours).before(date);
    }

    /**
     * 当月の初日を取得します。
     * 
     * @param date
     *            指定されたDate
     * @return 当月の初日
     */
    public static Date getFirstDateOfMonth(final Date date) {
        final Calendar c = Calendar.getInstance();

        c.setTime(date);
        setZero(c);
        c.set(Calendar.DATE, 1);

        return c.getTime();
    }

    /**
     * 当月の最終日時を取得します。
     * 
     * @param date
     *            指定されたDate
     * @return 最終日の日付
     */
    public static Date getLastDateOfMonth(final Date date) {
        final Calendar c = Calendar.getInstance();

        c.setTime(date);
        setZero(c);

        // 翌月から-1秒する事で当月の最終日時を取得する。
        c.add(Calendar.MONTH, +1);
        c.add(Calendar.SECOND, -1);
        return c.getTime();
    }

    /**
     * 文字列型からDate型に変更する。<br>
     * yyyy/MM/dd HH:mm:ss z
     * 
     * @param date
     *            文字列型の日付
     * @param format
     *            第1引数で指定した日付のフォーマット
     * @return Date Date型
     */
    public static Date getStringToDate(final String date, final String format) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.JAPAN);
        try {
            return simpleDateFormat.parse(date);
        } catch (final ParseException e) {
            return null;
        }
    }

    /**
     * 文字列型からDate型に変更する。<br>
     * デフォルトフォーマットの「yyyy/MM/dd HH:mm:ss z」を元にパースする。
     * 
     * @param date
     *            文字列型の日付
     * @return Date Date型
     */
    public static Date getStringToDate(final String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT, Locale.JAPAN);
        try {
            return simpleDateFormat.parse(date);
        } catch (final ParseException e1) {
            try {
                final String replaceDate = date.replaceAll("JST", "");
                simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT2, Locale.JAPAN);
                return simpleDateFormat.parse(replaceDate);
            } catch (final ParseException e2) {
                return null;
            }
        }
    }

    /**
     * 2つの日付の差を求めます。
     * 
     * 計算方法は以下となります。 1.最初に2つの日付を long 値に変換します。 　※この long 値は 1970 年 1 月 1 日
     * 00:00:00 GMT からの 　経過ミリ秒数となります。 2.次にその差を求めます。
     * 3.上記の計算で出た数量を 1 日の時間で割ることで 　日付の差を求めることができます。 　※1 日 ( 24 時間) は、86,400,000
     * ミリ秒です。
     * 
     * @param date1
     *            日付 java.util.Date
     * @param date2
     *            日付 java.util.Date
     * @return 2つの日付の差
     */
    public static int diffDateDay(final Date date1, final Date date2) {
        final long datetime1 = date1.getTime();
        final long datetime2 = date2.getTime();
        return (int) ((datetime1 - datetime2) / TIME_MSEC_ONEDAY);
    }

    /**
     * 差分の時間を返却する。
     * 
     * @param date1
     *            比較対象の日付(新しい方の日付)
     * @param date2
     *            比較対象の日付(古い方の日付)
     * 
     * @return 時間
     */
    public static int diffDateHour(final Date date1, final Date date2) {
        // ミリ秒単位で差を求める
        final long result = date1.getTime() - date2.getTime();

        // 時間に換算する（ミリ秒→時間）
        return (int) (result / TIME_MSEC_ONEHOUR);
    }

    /**
     * 差分の分数を返却する。
     * 
     * @param date1
     *            比較対象の日付(新しい方の日付)
     * @param date2
     *            比較対象の日付(古い方の日付)
     * 
     * @return 時間
     */
    public static int diffDateMin(final Date date1, final Date date2) {
        // ミリ秒単位で差を求める
        final long result = date1.getTime() - date2.getTime();

        // 分数に換算する（ミリ秒→分）
        return (int) (result / TIME_MSEC_ONEMIN);
    }

    /**
     * 差分のsecを返却する。
     * 
     * @param date1
     *            比較対象の日付(新しい方の日付)
     * @param date2
     *            比較対象の日付(古い方の日付)
     * 
     * @return 時間
     */
    public static int diffDateSec(final Date date1, final Date date2) {
        // ミリ秒単位で差を求める
        final long result = date1.getTime() - date2.getTime();

        // 秒数に換算する（ミリ秒→秒）
        return (int) (result / TIME_MSEC_SEC);
    }

    /**
     * 差分のmsecを返却する。
     * 
     * @param date1
     *            比較対象の日付(新しい方の日付)
     * @param date2
     *            比較対象の日付(古い方の日付)
     * 
     * @return 時間
     */
    public static long diffDateMsec(final Date date1, final Date date2) {
        // ミリ秒単位で差を求める
        return date1.getTime() - date2.getTime();
    }

    /**
     * どちらの日付が進んでいるかを比較する
     * 
     * @param date1
     *            比較対象の日付
     * @param date2
     *            比較対象の日付
     * 
     * @return true:date1の方が新しい時間 false:date2の方が新しい時間
     */
    public static boolean isDateCheck(final Date date1, final Date date2) {
        return (date1.getTime() - date2.getTime()) > 0;
    }

    /**
     * 最低年から現在年までのリストを作成する。
     * 
     * @param minYear
     *            指定された年
     * @param isAsc
     *            true:昇順 false:降順
     * @return 指定された年から最新の年までのList
     */
    public static List<Integer> getYears(final int minYear, final boolean isAsc) {
        final List<Integer> years = new ArrayList<Integer>();
        final Calendar c = Calendar.getInstance();

        final int nowYear = c.get(Calendar.YEAR);
        if (minYear <= nowYear) {
            if (isAsc) {
                for (int i = minYear; i <= nowYear; i++) {
                    years.add(i);
                }
            } else {
                for (int i = nowYear; i >= minYear; i--) {
                    years.add(i);
                }
            }
        }
        return years;
    }

    /**
     * 最低年から現在年までのリストを作成する。
     * 
     * @param minYear
     *            指定された年
     * @param isAsc
     *            true:昇順 false:降順
     * @return 指定された年から最新の年までのList
     */
    public static List<String> getYearsToString(final int minYear, final boolean isAsc) {
        final List<String> years = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();

        final int nowYear = c.get(Calendar.YEAR);
        if (minYear <= nowYear) {
            if (isAsc) {
                for (int i = minYear; i <= nowYear; i++) {
                    years.add(Integer.toString(i));
                }
            } else {
                for (int i = nowYear; i >= minYear; i--) {
                    years.add(Integer.toString(i));
                }
            }
        }
        return years;
    }

    /**
     * 月のリストを作成する。
     * 
     * @param isAsc
     *            true:昇順 false:降順
     * @return 年のList
     */
    public static List<Integer> getMonths(final boolean isAsc) {
        final List<Integer> months = new ArrayList<Integer>();
        if (isAsc) {
            for (int i = 1; i <= 12; i++) {
                months.add(i);
            }
        } else {
            for (int i = 12; i >= 1; i--) {
                months.add(i);
            }
        }
        return months;
    }

    /**
     * 月のリストを作成する。
     * 
     * @param isAsc
     *            true:昇順 false:降順
     * @return 年のList
     */
    public static List<String> getMonthsToString(final boolean isAsc) {
        final List<String> months = new ArrayList<String>();
        if (isAsc) {
            for (int i = 1; i <= 12; i++) {
                months.add(Integer.toString(i));
            }
        } else {
            for (int i = 12; i >= 1; i--) {
                months.add(Integer.toString(i));
            }
        }
        return months;
    }

    /**
     * 日のリストを作成する。
     * 
     * @param isAsc
     *            true:昇順 false:降順
     * @return 年のList
     */
    public static List<Integer> getDays(final boolean isAsc) {
        final List<Integer> days = new ArrayList<Integer>();
        if (isAsc) {
            for (int i = 1; i <= 31; i++) {
                days.add(i);
            }
        } else {
            for (int i = 31; i >= 1; i--) {
                days.add(i);
            }
        }
        return days;
    }

    /**
     * 日のリストを作成する。
     * 
     * @param isAsc
     *            true:昇順 false:降順
     * @return 年のList
     */
    public static List<String> getDaysToString(final boolean isAsc) {
        final List<String> days = new ArrayList<String>();
        if (isAsc) {
            for (int i = 1; i <= 31; i++) {
                days.add(Integer.toString(i));
            }
        } else {
            for (int i = 31; i >= 1; i--) {
                days.add(Integer.toString(i));
            }
        }
        return days;
    }

    /**
     * 曜日表示用の文字列を取得する
     * 
     * @param date
     *            Date
     * @param englishFlg
     *            true:英語<br>
     *            false:日本語
     * @return 曜日文字列
     */
    public static String getWeek(final Date date, final boolean englishFlg) {
        final Calendar c = Calendar.getInstance();

        c.setTime(date);
        if (englishFlg) {
            return WEEK_EU[c.get(Calendar.DAY_OF_WEEK) - 1];
        } else {
            return WEEK_JP[c.get(Calendar.DAY_OF_WEEK) - 1];
        }
    }

    /**
     * 曜日表示用の文字列を取得する
     * 
     * @param cal
     *            Calender
     * @param englishFlg
     *            true:英語<br>
     *            false:日本語
     * @return 曜日文字列
     */
    public static String getWeek(final Calendar cal, final boolean englishFlg) {
        if (englishFlg) {
            return WEEK_EU[cal.get(Calendar.DAY_OF_WEEK) - 1];
        } else {
            return WEEK_JP[cal.get(Calendar.DAY_OF_WEEK) - 1];
        }
    }

    /**
     * 曜日表示用の文字列を取得する
     * 
     * @param year
     *            年
     * @param month
     *            月
     * @param day
     *            日
     * @param englishFlg
     *            true:英語<br>
     *            false:日本語
     * @return 曜日文字列
     */
    public static String getWeek(final int year, final int month, final int day, final boolean englishFlg) {
        final Calendar c = Calendar.getInstance();

        c.set(year, month - 1, day);
        if (englishFlg) {
            return WEEK_EU[c.get(Calendar.DAY_OF_WEEK) - 1];
        } else {
            return WEEK_JP[c.get(Calendar.DAY_OF_WEEK) - 1];
        }
    }

    /**
     * 成年確認
     * 
     * @param year
     *            生年月日:年
     * @param month
     *            生年月日:月
     * @param day
     *            生年月日:日
     * @return true 未成年(20歳未満),false 成年(20歳以上)
     * 
     */
    public static boolean isAdult(final int year, final int month, final int day) {
        final Calendar calendar1 = Calendar.getInstance(); // 現在の日付
        final Calendar calendar2 = Calendar.getInstance(); // 生年月日
        final Calendar calendar3 = Calendar.getInstance(); // 18歳の日付
        calendar2.set(year, month - 1, day, 0, 0);
        calendar3.set(Calendar.YEAR, calendar1.get(Calendar.YEAR) - 20);
        calendar3.set(Calendar.HOUR_OF_DAY, 1);
        if (calendar3.after(calendar2)) {
            return true;
        }
        return false;
    }

    /**
     * hh:mm:ss時間に変換する
     * 
     * @param time
     *            msecを指定する
     * @return 時間(hh:mm:ss)
     */
    @SuppressLint("DefaultLocale")
    public static String getClock(final int time) {
        final int hourCurrentPosition = time / TIME_MSEC_ONEHOUR;
        final int minCurrentPosition = (time - (hourCurrentPosition * TIME_MSEC_ONEHOUR)) / TIME_MSEC_ONEMIN;
        final int secCurrentPosition = (time - (minCurrentPosition * TIME_MSEC_ONEMIN)) / TIME_MSEC_SEC;
        return String.format("%02d:%02d:%02d", hourCurrentPosition, minCurrentPosition, secCurrentPosition);
    }

    /**
     * 指定された時間(msec)からYYYY/MM/DD hh:mm:ss時間に変換する
     * 
     * @param time
     *            msecを指定する
     * @return YYYY/MM/DD hh:mm:ssにて返却する
     */
    @SuppressLint("DefaultLocale")
    public static String getDateClock(final long time) {
        final Date date = new Date(time);
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.JAPAN);
        return df.format(date);
    }

    /**
     * HttpヘッダのExpiredをYYYY/MM/DD hh:mm:ssに変換する
     * 
     * @param expires
     *            ex:Thu, 01 Dec 1994 16:00:00 GMT
     * @return YYYY/MM/DD hh:mm:ssにて返却する
     */
    @SuppressLint("DefaultLocale")
    public static String getExpiresDateTime(final String expires) {
        final String[] expiredSplit = expires.split(" ");
        final String[] month = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        final int length = month.length;
        for (int i = 0; i < length; i++) {
            if (expiredSplit[2].equals(month[i])) {
                return String.format("%04d/%02d/%02d %s", Integer.parseInt(expiredSplit[3]), i + 1, Integer.parseInt(expiredSplit[1]), expiredSplit[4]);
            }
        }
        return "";
    }

    /**
     * 月日時分の数値を2桁にあわせる
     * 
     * @param num
     *            数値
     * @return 2桁数値
     */
    public static String addZero(final int num) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (0 <= num && num <= 9) {
            stringBuilder.append(0);
            stringBuilder.append(num);
        } else {
            stringBuilder.append(num);
        }
        return stringBuilder.toString();
    }

    /**
     * 月日時分の数値を2桁にあわせる
     * 
     * @param num
     *            数値
     * @return 2桁数値
     */
    public static String addZero(final String num) {
        final StringBuilder stringBuilder = new StringBuilder();

        final int number = Integer.parseInt(num);
        if (0 <= number && number <= 9) {
            stringBuilder.append(0);
            stringBuilder.append(number);
        } else {
            stringBuilder.append(number);
        }
        return stringBuilder.toString();
    }

    /**
     * 時間を日付、時間、分、秒まで0にセットする。
     * 
     * @param c
     *            {@link Calendar}
     * @return {@link Calendar}
     */
    private static Calendar setZero(final Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }
}
