package com.hzq.core.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
    public static final String PUBLIC_KEY_CONTEXT = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB" +
            "CgKCAQEAmvnt+a8k+mw7TJB/cK+ao6xZWnQCDfPq8w4pEigLCs+sGpje/xB1KKA0tYsvIBQQdiDN" +
            "uUY3EBvuqZd3smOnFFXWH6V/IhEaG3rbEGKRqdp6UCodMTrInjgqMCzWXXZHmT2aa65X0xTmNqsU" +
            "T8z1DFYgXaaSgJOfMDGILsxyKAjErR/BSirDx0EyyswKF8XZhuUBG5761qvw969PEalg2BnM+WKd" +
            "gRoj4uUCBKI0iSkcQLypRE1HgsgjtR+1XULzKGmuofqa1Y27APrUVPjX3rqN+2SPEhjNrxQFHMH8" +
            "3QDnFPsXvsT/w+9kspwq1kgVhuerHQkTLSB8jZE6JDAIswIDAQAB";

    public static final String PRIVATE_KEY_CONTEXT = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKY" +
            "wggSiAgEAAoIBAQCa+e35ryT6bDtMkH9wr5qjrFladAIN8+rzDikSKAsKz6wamN7/EHUooDS1iy8" +
            "gFBB2IM25RjcQG+6pl3eyY6cUVdYfpX8iERobetsQYpGp2npQKh0xOsieOCowLNZddkeZPZprrlf" +
            "TFOY2qxRPzPUMViBdppKAk58wMYguzHIoCMStH8FKKsPHQTLKzAoXxdmG5QEbnvrWq/D3r08RqWD" +
            "YGcz5Yp2BGiPi5QIEojSJKRxAvKlETUeCyCO1H7VdQvMoaa6h+prVjbsA+tRU+Nfeuo37ZI8SGM2" +
            "vFAUcwfzdAOcU+xe+xP/D72SynCrWSBWG56sdCRMtIHyNkTokMAizAgMBAAECggEADAi2Svons+5" +
            "tmF9sFfL83hLHCXgSpMS5DNCfrdJAMUG5CoOuZT4A6vutvB8ntKT6NkIL14Q1SGHy4WZqnAaS9ch" +
            "IjUKLobiUP2gF1Lritr7JWqo6hBUdePPBiuYgazeBDcwzzoLGPRTVX49fT2cJ7EBP2RXTMHrGFLb" +
            "z7TZg8uzEUYf1guQDI3I2kKIiusk4qAbM4O0yFqt7K5xheTAM35v0Si9qQ2iKq5Poye54hf0tmWo" +
            "gR+XqxN/KR+XfGaSnRIZcy6oCXLE5Af95DFwmLrprGq3D4JR8VfxlfL9YNr9oLZoDC5ehesZjzNO" +
            "wa3TGFN+3Uplvv2SLjqZllxDbiQKBgQC1+yLeXyWLM9HPa/VsJWQ5id1DSXoRf3B9Lq1Okng/ljr" +
            "SORaKGcRdZVUTkrO+a47XbGPWBZlMAoABSlSMtYRV9AVXvCjnSiywrX2pgxXz/Glcvlul1BJGr8J" +
            "1sn/Bnbwjn/VC2JP9VjvnSBwjhRHUKvX/VuoT5WPsqP9F8IOJnQKBgQDaAun4QTdSqTMFqSc8qWR" +
            "Rs7anwDtlmY4pOqsfJ28z4xsZOo1ZPDvdUp+OxhP3OW+hnJKw1du9YqyvfKTgivuWdHLCv9lYqLo" +
            "+1UAlPpWlNfGFfmMUSR/ilAhK/l0ldesf3b9Sh5W3M+hhoxFyW6phrCPDs/kmk6Gm0mmY+fOyjwK" +
            "BgHUtQxN6onsSn9t2gW3kWbeu7NkBu+b+bxk77/3y1Bjli/SHK9s6ojUdJX3X7YMEUNKniVVq/31" +
            "w8qliCjUcQBz0ZmhK0O+01DfjTlHTBVS4R8OfEvYhKq93RrhwelDaVyV9PviO6iOq3iGbTfsVGRj" +
            "LnOCQP2MWRpRBcTXAm4IFAoGAGr7QxUv65YEsaT1fOuntDLjnZ50FrGkyL4yrAZ9s3Qi3gljUjmX" +
            "9UiGYwYM7X9EECOeUd21FD+7TEJRSBdoHE2Ylzg6LM30/nAW9YNRUt3fusVnX/d0atElE2y5pbvc" +
            "QaVBi8FUB4P/VOmUu9sCYHnNnz9J8dmcWJckae1Huu7ECgYAUxRjr7rtb+r9ob1RbidEo4y1IUyi" +
            "fUGHd1PoSXZfwxOM9y3Sgp92WKrLHigeoHlyRb6gFWLk+BFwILh/1QDgSFiPkd3kJrvvjbgJX6No" +
            "mO79winiJB4BNYQxMps7Ik1Er+e+RO5tM0fNlnPfkFrGi2L8qeMKVNDSZnF1rfYLgkA==";

    private static final int KEY_SIZE = 2048;
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
        return Base64.getEncoder().encodeToString(key);
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
            return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8); // 直接转换为字符串
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + text + "]时遇到异常", e);
        }
    }

    public static void test() throws Exception {
        // 用于生成公钥和私钥
        Map<String, Object> keyPair = getKeyPair(KEY_SIZE);
        String publicKeyStr = getPublicKeyStr(keyPair);
        System.out.println(publicKeyStr);
        System.out.println();
        String privateKeyStr = getPrivateKeyStr(keyPair);
        System.out.println(privateKeyStr);
    }
}
