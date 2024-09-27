package com.hzq.core.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hua
 * @className com.hzq.gateway.util RSAUtils
 * @date 2024/9/26 22:06
 * @description RSA 加密解密工具
 */
public class RSAUtils {
    private static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * @author hua
     * @date 2024/9/26 23:05
     * @param keySize 设置密钥对位数，要求是 512 的倍数
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @apiNote 生成 RSA 密钥对
     **/
    public static Map<String, Object> getKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 获取公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new HashMap<>(2) {{
            put(PUBLIC_KEY, publicKey);
            put(PRIVATE_KEY, privateKey);
        }};
    }

    /**
     * @author hua
     * @date 2024/9/26 23:08
     * @param keyMap 密钥对 Map
     * @return java.lang.String
     * @apiNote 获取公钥字符串
     **/
    public static String getPublicKeyStr(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * @author hua
     * @date 2024/9/26 23:08
     * @param keyMap 密钥对 Map
     * @return java.lang.String
     * @apiNote 获取私钥字符串
     **/
    public static String getPrivateKeyStr(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * @author hua
     * @date 2024/9/26 23:10
     * @param publicKeyString 公钥字符串
     * @return java.security.PublicKey
     * @apiNote 获取公钥对象
     **/
    public static PublicKey getPublicKey(String publicKeyString) throws Exception {
        byte[] publicKeyByte = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * @author hua
     * @date 2024/9/26 23:10
     * @param privateKeyString 公钥字符串
     * @return java.security.PublicKey
     * @apiNote 获取私钥对象
     **/
    public static PrivateKey getPrivateKey(String privateKeyString) throws Exception {
        byte[] privateKeyByte = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * @author hua
     * @date 2024/9/26 23:13
     * @param key 需要编码的字节数组
     * @return java.lang.String
     * @apiNote BASE64编码
     **/
    public static String encryptBASE64(byte[] key) {
        return Arrays.toString(Base64.getEncoder().encode(key));
    }

    /**
     * @author hua
     * @date 2024/9/26 23:14
     * @param key 需要编码的字符串
     * @return byte[]
     * @apiNote BASE64解码
     **/
    public static byte[] decryptBASE64(String key) {
        return Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @author hua
     * @date 2024/9/26 23:14
     * @param text 待加密的明文字符串
     * @param publicKeyStr 公钥
     * @return java.lang.String
     * @apiNote 获取加密后的密文
     **/
    public static String encrypt(String text, String publicKeyStr) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyStr));
            byte[] bytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return encryptBASE64(bytes);
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + text + "]时遇到异常", e);
        }
    }

    /**
     * @author hua
     * @date 2024/9/26 23:15
     * @param text 待解密的密文字符串
     * @param privateKeyStr 私钥
     * @return java.lang.String
     * @apiNote 获取解密后的明文
     **/
    public static String decrypt(String text, String privateKeyStr) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyStr));
            byte[] bytes = decryptBASE64(text);
            return Arrays.toString(cipher.doFinal(bytes));
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + text + "]时遇到异常", e);
        }
    }
}
