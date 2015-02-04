package com.miya38.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * データベース用アノテーション
 *
 * @author y-miyazaki
 *
 */
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface DatabaseAnnotation {
    /**
     * 暗号化有無アノテーション
     *
     * @return true:暗号化あり/false:暗号化なし
     */
    public boolean encryption() default false;
}
