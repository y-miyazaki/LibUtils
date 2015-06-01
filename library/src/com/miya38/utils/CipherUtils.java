package com.miya38.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * 暗号化・復号ユーティリティークラス
 * 
 * @author y-miyazaki
 * 
 */
public final class CipherUtils {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_AES = "AES";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_RSA = "RSA";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_ARCFOUR = "ARCFOUR";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_BLOWFISH = "Blowfish";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_DES = "DES";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_DESEDE = "DESede";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_HMACMD5 = "HmacMD5";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_SHA256 = "SHA-256";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_HMACSHA1 = "HmacSHA1";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_HMACSHA256 = "HmacSHA256";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_HMACSHA384 = "HmacSHA384";
    /**
     * アルゴリズム定義
     */
    public static final String ALGORITHM_HMACSHA512 = "HmacSHA512";

    /** RSA + /ECB/PKCS1Padding */
    private static final String ALGORITHM_RSA_PADDING_ECB_PKCS1PADDING = ALGORITHM_RSA + "/ECB/PKCS1Padding";

    /** RSA + /ECB/PKCS5Padding */
    private static final String ALGORITHM_AES_PADDING_ECB_PKCS5PADDING = ALGORITHM_AES + "/ECB/PKCS5Padding";

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private CipherUtils() {
    }

    /**
     * キー生成(AES) 128bitのランダムのキーを自動生成する。
     * 
     * @return キークラス
     */
    public static Key generateKey() {
        try {
            final KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_AES);
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            generator.init(128, random);
            return generator.generateKey();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }
    }

    /**
     * キー生成(AES) 128bitのランダムのキーを自動生成する。
     * 
     * @return キー
     */
    public static byte[] generateByteKey() {
        try {
            final KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_AES);
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            generator.init(128, random);
            return generator.generateKey().getEncoded();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }
    }

    /**
     * キー生成(AES(base64))
     * 
     * @return キー(base64にした秘密鍵)
     */
    public static String generateBase64Key() {
        try {
            final KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_AES);
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            generator.init(128, random);
            return Base64.encodeToString(generator.generateKey().getEncoded(), Base64.DEFAULT);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }
    }

    /**
     * ハッシュ(SHA256)
     * 
     * @param target
     *            ハッシュ対象の文字列
     * @return ハッシュ
     */
    public static byte[] getHash(final String target) {
        if (target == null) {
            return null;
        }
        try {
            final byte[] originalBytes = target.getBytes("UTF-8");
            final MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_SHA256);
            messageDigest.update(originalBytes);
            return messageDigest.digest();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * ハッシュ(HMACSHA256)<br>
     * 秘密鍵がKey型版
     * 
     * @param target
     *            ハッシュ対象の文字列
     * @param secretKey
     *            秘密鍵
     * @return ハッシュ
     */
    public static byte[] getHash(final String target, final Key secretKey) {
        if (target == null) {
            return null;
        }
        try {
            final byte[] originalBytes = target.getBytes("UTF-8");
            final Mac mac = Mac.getInstance(ALGORITHM_HMACSHA256);
            mac.init(secretKey);
            return mac.doFinal(originalBytes);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * ハッシュ(HMACSHA256) 秘密鍵が文字列型版
     * 
     * @param target
     *            ハッシュ対象の文字列
     * @param secretKey
     *            秘密鍵
     * @return ハッシュ
     */
    public static byte[] getHash(final String target, final String secretKey) {
        if (target == null) {
            return null;
        }
        try {
            final byte[] originalBytes = target.getBytes("UTF-8");
            final byte[] decodeSecretKey = Base64.decode(secretKey, Base64.DEFAULT);
            final SecretKeySpec sksSpec = new SecretKeySpec(decodeSecretKey, ALGORITHM_AES);
            final Mac mac = Mac.getInstance(ALGORITHM_HMACSHA256);
            mac.init(sksSpec);
            return mac.doFinal(originalBytes);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * ハッシュ(HMACSHA256) 秘密鍵がbyte型版
     * 
     * @param target
     *            ハッシュ対象の文字列
     * @param secretKey
     *            秘密鍵
     * @return ハッシュ
     */
    public static byte[] getHash(final String target, final byte[] secretKey) {
        if (target == null) {
            return null;
        }
        try {
            final byte[] originalBytes = target.getBytes("UTF-8");
            final SecretKeySpec sksSpec = new SecretKeySpec(secretKey, ALGORITHM_AES);
            final Mac mac = Mac.getInstance(ALGORITHM_HMACSHA256);
            mac.init(sksSpec);
            return mac.doFinal(originalBytes);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * 暗号化(AES)(Base64)<br>
     * 秘密鍵がKey型版
     * 
     * @param target
     *            暗号化対象の文字列
     * @param secretKey
     *            秘密鍵
     * @return String 暗号化文字列
     */
    public static String enCryptBase64(final String target, final Key secretKey) {
        if (target == null) {
            return null;
        }
        try {
            final byte[] originalBytes = target.getBytes("UTF-8");

            final Cipher cipher = Cipher.getInstance(ALGORITHM_AES_PADDING_ECB_PKCS5PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new String(Base64.encode(cipher.doFinal(originalBytes), Base64.DEFAULT));
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * 復号(AES)<br>
     * 秘密鍵がKey型版
     * 
     * @param targetEnCrypt
     *            復号対象文字列
     * @param secretKey
     *            秘密鍵
     * @return String 復号文字列
     */
    public static String deCryptBase64(final String targetEnCrypt, final Key secretKey) {
        if (targetEnCrypt == null) {
            return null;
        }
        try {
            final byte[] encryptBytes = Base64.decode(targetEnCrypt, Base64.DEFAULT);

            final Cipher cipher = Cipher.getInstance(ALGORITHM_AES_PADDING_ECB_PKCS5PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(encryptBytes));
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        }
    }

    /**
     * 暗号化(AES)<br>
     * 秘密鍵が文字列型版
     * 
     * @param target
     *            暗号化対象の文字列
     * @param secretKey
     *            秘密鍵
     * @return String 暗号化文字列
     */
    public static String enCryptBase64(final String target, final String secretKey) {
        if (target == null) {
            return null;
        }
        try {
            // Base64で対象文字列と秘密鍵を復号する
            final byte[] originalBytes = target.getBytes("UTF-8");
            final byte[] decodeSecretKey = Base64.decode(secretKey, Base64.DEFAULT);

            final SecretKeySpec sksSpec = new SecretKeySpec(decodeSecretKey, ALGORITHM_AES);
            final Cipher cipher = Cipher.getInstance(ALGORITHM_AES_PADDING_ECB_PKCS5PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, sksSpec);
            return new String(Base64.encode(cipher.doFinal(originalBytes), Base64.DEFAULT));
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * 復号(AES)<br>
     * 秘密鍵が文字列型版
     * 
     * @param targetEnCrypt
     *            復号対象文字列
     * @param secretKey
     *            秘密鍵
     * @return String 復号文字列
     */
    public static String deCryptBase64(final String targetEnCrypt, final String secretKey) {
        if (targetEnCrypt == null) {
            return null;
        }
        try {
            // Base64で対象文字列と秘密鍵を復号する
            final byte[] encryptBytes = Base64.decode(targetEnCrypt, Base64.DEFAULT);
            final byte[] decodeSecretKey = Base64.decode(secretKey, Base64.DEFAULT);

            final SecretKeySpec sksSpec = new SecretKeySpec(decodeSecretKey, ALGORITHM_AES);
            final Cipher cipher = Cipher.getInstance(ALGORITHM_AES_PADDING_ECB_PKCS5PADDING);
            cipher.init(Cipher.DECRYPT_MODE, sksSpec);
            return new String(cipher.doFinal(encryptBytes), "UTF-8");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * 暗号化(AES)<br>
     * 秘密鍵が文字列型版
     * 
     * @param target
     *            暗号化対象の文字列
     * @param secretKey
     *            秘密鍵
     * @return String 暗号化文字列
     */
    public static byte[] enCrypt(final byte[] target, final byte[] secretKey) {
        if (target == null) {
            return null;
        }
        try {
            final SecretKeySpec sksSpec = new SecretKeySpec(secretKey, ALGORITHM_AES);

            final Cipher cipher = Cipher.getInstance(ALGORITHM_AES_PADDING_ECB_PKCS5PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, sksSpec);
            return cipher.doFinal(target);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        }
    }

    /**
     * 復号(AES)<br>
     * 秘密鍵が文字列型版
     * 
     * @param targetEnCrypt
     *            復号対象文字列
     * @param secretKey
     *            秘密鍵
     * @return String 復号文字列
     */
    public static String deCrypt(final byte[] targetEnCrypt, final byte[] secretKey) {
        if (targetEnCrypt == null) {
            return null;
        }
        try {
            final SecretKeySpec sksSpec = new SecretKeySpec(secretKey, ALGORITHM_AES);
            final Cipher cipher = Cipher.getInstance(ALGORITHM_AES_PADDING_ECB_PKCS5PADDING);
            cipher.init(Cipher.DECRYPT_MODE, sksSpec);
            return new String(cipher.doFinal(targetEnCrypt), "UTF-8");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * 秘密鍵による暗号化(RSA)
     * 
     * @param data
     *            暗号化するデータのバイト配列
     * @param privateKey
     *            秘密鍵
     * @return byte[] 暗号化されたバイト配列
     */
    public static byte[] enCryptByPrivateKey(final byte[] data, final String privateKey) {
        try {
            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey.getBytes("UTF-8"));
            final KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
            final PrivateKey key = kf.generatePrivate(spec);

            final Cipher rsa = Cipher.getInstance(ALGORITHM_RSA_PADDING_ECB_PKCS1PADDING);
            rsa.init(Cipher.ENCRYPT_MODE, key);
            return rsa.doFinal(data);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeySpecException e) {
            throw new RuntimeException("InvalidKeySpecException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * 秘密鍵による復号(RSA)
     * 
     * @param data
     *            復号するデータのバイト配列
     * @param privateKey
     *            秘密鍵
     * @return byte[] 復号されたバイト配列
     */
    public static byte[] deCryptByPrivateKey(final byte[] data, final String privateKey) {
        try {
            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey.getBytes("UTF-8"));
            final KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
            final PrivateKey key = kf.generatePrivate(spec);

            final Cipher rsa = Cipher.getInstance(ALGORITHM_RSA_PADDING_ECB_PKCS1PADDING);
            rsa.init(Cipher.DECRYPT_MODE, key);
            return rsa.doFinal(data);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeySpecException e) {
            throw new RuntimeException("InvalidKeySpecException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * 公開鍵による暗号化(RSA)
     * 
     * @param data
     *            暗号化するデータのバイト配列
     * @param publicKey
     *            公開鍵
     * @return byte[] 暗号化されたバイト配列
     */
    public static byte[] enCryptByPublicKey(final byte[] data, final String publicKey) {
        try {
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getBytes("UTF-8"));
            final KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
            final PublicKey key = kf.generatePublic(spec);

            final Cipher rsa = Cipher.getInstance(ALGORITHM_RSA_PADDING_ECB_PKCS1PADDING);
            rsa.init(Cipher.ENCRYPT_MODE, key);
            return rsa.doFinal(data);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeySpecException e) {
            throw new RuntimeException("InvalidKeySpecException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * 公開鍵による復号(RSA)
     * 
     * @param data
     *            復号するデータのバイト配列
     * @param publicKey
     *            公開鍵
     * @return byte[] 復号されたバイト配列
     */
    public static byte[] deCryptByPublicKey(final byte[] data, final String publicKey) {
        try {
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getBytes("UTF-8"));
            final KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
            final PublicKey key = kf.generatePublic(spec);

            final Cipher rsa = Cipher.getInstance(ALGORITHM_RSA_PADDING_ECB_PKCS1PADDING);
            rsa.init(Cipher.DECRYPT_MODE, key);
            return rsa.doFinal(data);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeySpecException e) {
            throw new RuntimeException("InvalidKeySpecException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
    }

    /**
     * 秘密鍵による暗号化(RSA)
     * 
     * @param data
     *            暗号化するデータのバイト配列
     * @param privateKey
     *            秘密鍵
     * @return byte[] 暗号化されたバイト配列
     */
    public static byte[] enCryptByPrivateKey(final byte[] data, final byte[] privateKey) {
        try {
            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
            final KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
            final PrivateKey key = kf.generatePrivate(spec);

            final Cipher rsa = Cipher.getInstance(ALGORITHM_RSA_PADDING_ECB_PKCS1PADDING);
            rsa.init(Cipher.ENCRYPT_MODE, key);
            return rsa.doFinal(data);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeySpecException e) {
            throw new RuntimeException("InvalidKeySpecException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        }
    }

    /**
     * 秘密鍵による復号(RSA)
     * 
     * @param data
     *            復号するデータのバイト配列
     * @param privateKey
     *            秘密鍵
     * @return byte[] 復号されたバイト配列
     */
    public static byte[] deCryptByPrivateKey(final byte[] data, final byte[] privateKey) {
        try {
            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
            final KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
            final PrivateKey key = kf.generatePrivate(spec);

            final Cipher rsa = Cipher.getInstance(ALGORITHM_RSA_PADDING_ECB_PKCS1PADDING);
            rsa.init(Cipher.DECRYPT_MODE, key);
            return rsa.doFinal(data);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeySpecException e) {
            throw new RuntimeException("InvalidKeySpecException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        }
    }

    /**
     * 公開鍵による暗号化(RSA)
     * 
     * @param data
     *            暗号化するデータのバイト配列
     * @param publicKeyData
     *            公開鍵
     * @return byte[] 暗号化されたバイト配列
     */
    public static byte[] enCryptByPublicKey(final byte[] data, final byte[] publicKeyData) {
        try {
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyData);
            final KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
            final PublicKey key = kf.generatePublic(spec);

            final Cipher rsa = Cipher.getInstance(ALGORITHM_RSA_PADDING_ECB_PKCS1PADDING);
            rsa.init(Cipher.ENCRYPT_MODE, key);
            return rsa.doFinal(data);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeySpecException e) {
            throw new RuntimeException("InvalidKeySpecException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        }
    }

    /**
     * 公開鍵による復号(RSA)
     * 
     * @param data
     *            復号するデータのバイト配列
     * @param publicKey
     *            公開鍵
     * @return byte[] 復号されたバイト配列
     */
    public static byte[] deCryptByPublicKey(final byte[] data, final byte[] publicKey) {
        try {
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
            final KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
            final PublicKey key = kf.generatePublic(spec);

            final Cipher rsa = Cipher.getInstance(ALGORITHM_RSA_PADDING_ECB_PKCS1PADDING);
            rsa.init(Cipher.DECRYPT_MODE, key);
            return rsa.doFinal(data);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (final InvalidKeySpecException e) {
            throw new RuntimeException("InvalidKeySpecException", e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException("NoSuchPaddingException", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException", e);
        } catch (final IllegalBlockSizeException e) {
            throw new RuntimeException("IllegalBlockSizeException", e);
        } catch (final BadPaddingException e) {
            throw new RuntimeException("BadPaddingException", e);
        }
    }

}
