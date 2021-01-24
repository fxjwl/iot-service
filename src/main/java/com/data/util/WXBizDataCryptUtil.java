package com.data.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class WXBizDataCryptUtil {

    public static String decryptUserInfo(String sessionKey, String iv, String encrypData) throws Exception {

        byte[] encData = Base64.decodeBase64(encrypData);
        byte[] ivData = Base64.decodeBase64(iv);
        byte[] keyData = Base64.decodeBase64(sessionKey);

        int base = 16;
        if (keyData.length % base != 0) {
            int groups = keyData.length / base
                    + (keyData.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyData, 0, temp, 0, keyData.length);
            keyData = temp;
        }

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivData);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串
        return new String(cipher.doFinal(encData), "UTF-8");
    }

    public static String decrypt(String key, String iv, String data) throws Exception {
//        initialize();

        //被加密的数据
        byte[] dataByte = Base64.decodeBase64(data);
        //加密秘钥
        byte[] keyByte = Base64.decodeBase64(key);
        //偏移量
        byte[] ivByte = Base64.decodeBase64(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //PKCS5Padding PKCS7Padding

        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));

        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

        byte[] resultByte = cipher.doFinal(dataByte);
        if (null != resultByte && resultByte.length > 0) {
            String result = new String(resultByte, "UTF-8");
            return result;
        }
        return null;
    }

}
