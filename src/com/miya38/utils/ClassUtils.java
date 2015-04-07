package com.miya38.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * 
 * クラスユーティリティ
 * 
 * @author y-miyazaki
 * 
 */
public final class ClassUtils {
    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ClassUtils() {
    }

    /**
     * 指定したパッケージを含むクラス名のクラスオブジェクトを取得する。
     * 
     * @param name
     *            パッケージを含むクラス名
     * @return クラスオブジェクト
     * @throws IllegalArgumentException
     *             クラス名が null の場合
     * @throws IllegalStateException
     *             指定された名称のクラスがない場合
     */
    public static Class<?> getClassObject(final String name) {
        // nullの場合は例外
        if (name == null) {
            throw new IllegalArgumentException();
        }
        try {
            // 指定名称のクラスオブジェクトを返す
            return Class.forName(name);
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 指定クラスの全スーパークラスを取得する。
     * 
     * 指定クラスの全スーパークラスを取得し、一覧として返却する。 最上位のスーパークラスであるObject型を除いた全スーパークラスが返される。
     * 
     * また、返却一覧へ指定した検索開始・終了クラスの情報を含めるかどうかを設定できる。
     * 
     * @param startClass
     *            検索開始クラス
     * @param endClass
     *            検索終了クラス
     * @param includeStartClass
     *            返却一覧へ検索開始クラスを含めるかどうか
     * @param includeEndClass
     *            返却一覧へ検索終了クラスを含めるかどうか
     * @return 指定クラスの全スーパークラス
     * @throws IllegalArgumentException
     *             検索開始クラスが null の場合
     */
    public static List<Class<?>> getSuperClasses(final Class<?> startClass, final Class<?> endClass, final boolean includeStartClass, final boolean includeEndClass) {
        // 指定クラスが null の場合は例外
        if (startClass == null) {
            throw new IllegalArgumentException();
        }
        final List<Class<?>> retClasses = new ArrayList<Class<?>>(); // 返却クラス一覧
        Class<?> nowClass; // 現在のクラス
        // 返却一覧へ検索開始クラス情報を含める場合
        if (includeStartClass) {
            // 現在のクラスに指定された検索開始クラスを設定できる
            nowClass = startClass;
        } else {
            // 指定されたクラスのスーパークラスを設定する
            nowClass = startClass.getSuperclass();
        }
        // 検索終了クラスが指定されていない場合
        if (endClass == null) {
            // スーパークラスが null
            // または Object になるまで繰り返し
            while (nowClass != null && !Object.class.equals(nowClass)) {
                // 一覧へ取得したスーパークラスを追加する
                retClasses.add(nowClass);
                // 現在のクラスのスーパークラスを取得する
                nowClass = nowClass.getSuperclass();
            }
        } else {
            // スーパークラスが null
            // または 検索終了クラス
            // または Object になるまで繰り返し
            while (nowClass != null && !endClass.equals(nowClass) && !Object.class.equals(nowClass)) {
                // 一覧へ取得したスーパークラスを追加する
                retClasses.add(nowClass);
                // 現在のクラスのスーパークラスを取得する
                nowClass = nowClass.getSuperclass();
            }
            // 返却一覧へ検索終了クラス情報を含める場合
            if (includeEndClass) {
                // 現在のクラスに指定された検索終了クラスを設定できる
                retClasses.add(nowClass);
            }
        }
        // 作成した一覧を返却する
        return retClasses;
    }

    /**
     * 指定クラスの全インスタンスフィールドを取得する。
     * 
     * 指定クラスの非staticフィールドの一覧を取得する。
     * 
     * @param clazz
     *            クラス
     * @return 指定クラスのフィールド一覧
     * @throws IllegalArgumentException
     *             クラスが null の場合
     * @throws IllegalStateException
     *             フィールド取得失敗エラー時
     */
    public static List<Field> getClassInstanceFields(final Class<?> clazz) {
        // 引数が不正の場合は例外
        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        try {
            // 公開フィールドと非公開フィールドを取得する
            final Field[] fields = clazz.getFields();
            final Field[] declaredFields = clazz.getDeclaredFields();
            // 返却一覧を作成する
            final List<Field> retFields = new ArrayList<Field>();
            // 公開フィールド分だけ処理をする
            for (final Field field : fields) {
                // staticの場合
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                // フィールドを返却一覧へ追加する
                retFields.add(field);
            }
            // 非公開フィールド分だけ処理をする
            for (final Field field : declaredFields) {
                // staticの場合
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                // アクセス可能に設定する
                field.setAccessible(true);
                // フィールドを返却一覧へ追加する
                retFields.add(field);
            }
            // 返却一覧を返す
            return retFields;
        } catch (final Throwable e) {
            // 失敗のため例外を返す
            throw new IllegalStateException(e);
        }
    }

    /**
     * 指定インスタンスの指定フィールド(公開フィールド対象)を取得する。
     * 
     * @param instance
     *            インスタンス
     * @param name
     *            取得するフィールド名
     * @return 指定インスタンスの指定フィールド
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             フィールド取得失敗エラー時
     */
    public static Field getInstancePublicField(final Object instance, final String name) {
        // インスタンスまたはフィールド名が null の場合は例外
        if (instance == null || name == null) {
            throw new IllegalArgumentException();
        }
        // インスタンスからクラスを取得する
        final Class<?> localClass = instance.getClass();
        try {
            // 公開フィールドを返す
            return localClass.getField(name);
        } catch (final Throwable e) {
            // 失敗のため例外を返す
            throw new IllegalStateException(e);
        }
    }

    /**
     * 指定インスタンスの指定フィールド(publicフィールド対象)を取得する。
     * 
     * @param instance
     *            インスタンス
     * @param name
     *            取得するフィールド名
     * @return 指定インスタンスの指定フィールド
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             フィールド取得失敗エラー時
     */
    public static Field getInstanceDeclaredField(final Object instance, final String name) {
        // インスタンスまたはフィールド名が null の場合
        if (instance == null || name == null) {
            throw new IllegalArgumentException();
        }
        // インスタンスからクラスを取得する
        final Class<?> localClass = instance.getClass();
        try {
            // 非公開フィールドを取得する
            final Field retField = localClass.getDeclaredField(name);
            // アクセス可能に設定する
            retField.setAccessible(true);
            // フィールドを返却する
            return retField;
        } catch (final Throwable e) {
            // 失敗のため例外を返す
            throw new IllegalStateException(e);
        }
    }

    /**
     * 指定されたインスタンスフィールドへ値を設定する。(Object)
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @param value
     *            設定する値
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(final Class<?> clazz, final Object instance, final String name, final Object value) {
        final Field field = getClassField(clazz, instance, name);
        // プリミティブ型のチェックを行い、プリミティブ型の場合はフィールドのセットはしない。
        if (!field.getType().isPrimitive()) {
            try {
                field.set(instance, value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 指定されたインスタンスフィールドへ値を設定する。(boolean)
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @param value
     *            設定する値
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(final Class<?> clazz, final Object instance, final String name, final Boolean value) {
        final Field field = getClassField(clazz, instance, name);
        final Class<?> clazz1 = field.getType();

        if (clazz1.getSimpleName().equals("boolean") || clazz1.getSimpleName().equals("Boolean")) {
            try {
                field.set(instance, value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 指定されたインスタンスフィールドへ値を設定する。(byte)
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @param value
     *            設定する値
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(final Class<?> clazz, final Object instance, final String name, final Byte value) {
        final Field field = getClassField(clazz, instance, name);
        final Class<?> clazz1 = field.getType();

        if (clazz1.getSimpleName().equals("byte") || clazz1.getSimpleName().equals("Byte")) {
            try {
                field.set(instance, value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 指定されたインスタンスフィールドへ値を設定する。(short)
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @param value
     *            設定する値
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(final Class<?> clazz, final Object instance, final String name, final Short value) {
        final Field field = getClassField(clazz, instance, name);
        final Class<?> clazz1 = field.getType();

        if (clazz1.getSimpleName().equals("short") || clazz1.getSimpleName().equals("Short")) {
            try {
                field.set(instance, value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 指定されたインスタンスフィールドへ値を設定する。(int)
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @param value
     *            設定する値
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(final Class<?> clazz, final Object instance, final String name, final Integer value) {
        final Field field = getClassField(clazz, instance, name);
        final Class<?> clazz1 = field.getType();

        if (clazz1.getSimpleName().equals("int") || clazz1.getSimpleName().equals("Integer")) {
            try {
                field.set(instance, value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 指定されたインスタンスフィールドへ値を設定する。(long)
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @param value
     *            設定する値
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(final Class<?> clazz, final Object instance, final String name, final Long value) {
        final Field field = getClassField(clazz, instance, name);
        final Class<?> clazz1 = field.getType();

        if (clazz1.getSimpleName().equals("long") || clazz1.getSimpleName().equals("Long")) {
            try {
                field.set(instance, value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 指定されたインスタンスフィールドへ値を設定する。(float)
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @param value
     *            設定する値
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(final Class<?> clazz, final Object instance, final String name, final Float value) {
        final Field field = getClassField(clazz, instance, name);
        final Class<?> clazz1 = field.getType();

        if (clazz1.getSimpleName().equals("float") || clazz1.getSimpleName().equals("Float")) {
            try {
                field.set(instance, value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 指定されたインスタンスフィールドへ値を設定する。(double)
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @param value
     *            設定する値
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(final Class<?> clazz, final Object instance, final String name, final Double value) {
        final Field field = getClassField(clazz, instance, name);
        final Class<?> clazz1 = field.getType();

        if (clazz1.getSimpleName().equals("double") || clazz1.getSimpleName().equals("Double")) {
            try {
                field.set(instance, value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 指定されたインスタンスフィールドへ値を設定する。(char)
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @param value
     *            設定する値
     * @throws IllegalArgumentException
     *             インスタンスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(final Class<?> clazz, final Object instance, final String name, final char value) {
        final Field field = getClassField(clazz, instance, name);
        final Class<?> clazz1 = field.getType();

        if (clazz1.getSimpleName().equals("char")) {
            try {
                field.set(instance, value);
            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 指定クラスのフィールド取得をする
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象インスタンス
     * @param name
     *            フィールド名
     * @return フィールド
     */
    private static Field getClassField(final Class<?> clazz, final Object instance, final String name) {
        // 引数が不正の場合は例外
        if (instance == null || name == null) {
            throw new IllegalArgumentException();
        }

        Field field = null; // 取得したフィールド
        // 対象クラスがない場合
        if (clazz == null) {
            try {
                // 指定インスタンスの公開フィールドを取得する
                field = getInstancePublicField(instance, name);
            } catch (final Throwable e) {
                // 指定インスタンスの非公開フィールドを取得する
                field = getInstanceDeclaredField(instance, name);
            }
        } else {
            try {
                // 指定クラスの公開フィールドを取得する
                field = getClassPublicField(clazz, name);
            } catch (final Throwable e) {
                // 指定クラスの非公開フィールドを取得する
                field = getClassDeclaredField(clazz, name);
            }
        }
        return field;
    }

    /**
     * 指定クラスの指定公開フィールドを取得する。
     * 
     * @param clazz
     *            クラス
     * @param name
     *            フィールド名
     * @return 指定クラスの指定公開フィールド
     * @throws IllegalArgumentException
     *             クラスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             フィールド取得失敗エラー時
     */
    public static Field getClassPublicField(final Class<?> clazz, final String name) {
        // 引数が不正の場合は例外
        if (clazz == null || name == null) {
            throw new IllegalArgumentException();
        }
        try {
            // 指定フィールドを返す
            return clazz.getField(name);
        } catch (final Throwable e) {
            // 失敗のため例外を返す
            throw new IllegalStateException(e);
        }
    }

    /**
     * 指定クラスの指定非公開フィールドを取得する。
     * 
     * @param clazz
     *            クラス
     * @param name
     *            フィールド名
     * @return 指定クラスの指定非公開フィールド
     * @throws IllegalArgumentException
     *             クラスまたはフィールド名が null の場合
     * @throws IllegalStateException
     *             フィールド取得失敗エラー時
     */
    public static Field getClassDeclaredField(final Class<?> clazz, final String name) {
        // 引数が不正の場合は例外
        if (clazz == null || name == null) {
            throw new IllegalArgumentException();
        }
        try {
            // 指定フィールドを取得する
            final Field retField = clazz.getDeclaredField(name);
            // アクセス可能を設定する
            retField.setAccessible(true);
            // 取得したフィールドを返す
            return retField;
        } catch (final Throwable e) {
            // 失敗のため例外を返す
            throw new IllegalStateException(e);
        }
    }

    /**
     * フィールドをアクセシブルにして返却する
     * 
     * @param target
     *            クラス
     * @param name
     *            フィールド名
     * @return フィールド(インスタンス変数)
     */
    public static Field getField(final Class<?> target, final String name) {
        for (Class<?> clazz = target; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                final Field f = clazz.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (final NoSuchFieldException ex) {
            } catch (final SecurityException ex) {
            }
        }
        return null; // みつからなかった
    }

    /**
     * 指定されたObject型データを指定されたバンドルへ追加する。
     * 
     * 指定された値の型を判定し、適切な型であれば指定されたバンドルへ値が追加される。 Bundleでサポートされていない型の場合は値は追加されない。
     * 
     * @param bundle
     *            追加先バンドル
     * @param key
     *            追加する値名称
     * @param value
     *            追加する値
     * @return 値の追加に成功した場合は true
     * @throws IllegalArgumentException
     *             バンドルまたは値名称が null の場合
     */
    public static boolean putObjectBundle(final Bundle bundle, final String key, final Object value) {
        // バンドルまたは値名称が null の場合
        if (bundle == null || key == null) {
            throw new IllegalArgumentException();
        }
        // Serializable型の場合
        if (value instanceof Serializable) {
            // Serializable型として追加する
            bundle.putSerializable(key, (Serializable) value);
            // String型の場合
        } else if (value instanceof String) {
            bundle.putString(key, (String) value);
            // Integer型の場合
        } else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
            // Bundle型の場合
        } else if (value instanceof Bundle) {
            // Bundle型として追加する
            bundle.putBundle(key, (Bundle) value);
            // Parcelable型の場合
        } else if (value instanceof Parcelable) {
            // Parcelable型として追加する
            bundle.putParcelable(key, (Parcelable) value);
            // Parcelable[]型の場合
        } else if (value instanceof Parcelable[]) {
            // Parcelable[]型として追加する
            bundle.putParcelableArray(key, (Parcelable[]) value);
            // その他
        } else {
            return false;
        }
        return true;
    }

    /**
     * @param object
     *            Object
     * @param startClass
     *            開始クラス
     * @param endClass
     *            終了クラス
     */
    public static void setObjectNull(final Object object, final Class<?> startClass, final Class<?> endClass) {
        try {
            // 自クラスを含んだ全スーパークラス情報を取得する
            final List<Class<?>> classes = ClassUtils.getSuperClasses(startClass, endClass, true, true);
            // 自クラスを含んだ全スーパークラス情報分繰り返す
            for (final Class<?> nowClass : classes) {
                // 全フィールドを取得する
                final List<Field> fields = ClassUtils.getClassInstanceFields(nowClass);
                // 全フィールド分処理をする
                for (final Field field : fields) {
                    // フィールドが final の場合
                    if (Modifier.isFinal(field.getModifiers())) {
                        // 次のフィールドへ
                        continue;
                    }
                    ClassUtils.setInstanceFieldValue(ClassUtils.getClassObject(nowClass.getName()), object, field.getName(), (Object) null);
                }
            }
        } catch (final Throwable e) {
        }
    }

    /**
     * @param object
     * @param startClass
     * @param endClass
     */
    public static void setAsyncObjectNull(final Object object, final Class<?> startClass, final Class<?> endClass) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (final InterruptedException e1) {
                }

                try {
                    // 自クラスを含んだ全スーパークラス情報を取得する
                    final List<Class<?>> classes = ClassUtils.getSuperClasses(startClass, endClass, true, true);
                    // 自クラスを含んだ全スーパークラス情報分繰り返す
                    for (final Class<?> nowClass : classes) {
                        // 全フィールドを取得する
                        final List<Field> fields = ClassUtils.getClassInstanceFields(nowClass);
                        // 全フィールド分処理をする
                        for (final Field field : fields) {
                            // フィールドが final の場合
                            if (Modifier.isFinal(field.getModifiers())) {
                                // 次のフィールドへ
                                continue;
                            }
                            ClassUtils.setInstanceFieldValue(ClassUtils.getClassObject(nowClass.getName()), object, field.getName(), (Object) null);
                        }
                    }
                } catch (final Throwable e) {
                }
            };
        })).start();
    }

    /**
     * 対応するMethod型を取得する。
     * 
     * @param clazz
     *            クラス
     * @param methodName
     *            メソッド名
     * @param args
     *            引数
     * @return Method型
     */
    public static Method getMethod(final Class<?> clazz, final String methodName, final Object... args) {
        // メソッドリストを取得し、newInstanceメソッドを探す
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                // -----------------------------------------------------
                // 引数の数と引数の型を判断し、完全一致していることを確認する。
                // 一致している場合はコールする。
                // -----------------------------------------------------
                final Class<?>[] paramTypes = method.getParameterTypes();
                final int length = paramTypes.length;

                if (length == args.length) {
                    boolean paramFlg = true;
                    for (int i = 0; i < length; i++) {
                        if (!args[i].getClass().getName().equals(paramTypes[i].getName())) {
                            paramFlg = false;
                            break;
                        }
                    }
                    if (paramFlg) {
                        return method;
                    }
                }
            }
        }
        return null;
    }
}
