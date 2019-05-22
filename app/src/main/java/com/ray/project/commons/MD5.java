package com.ray.project.commons;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 *
 * author:ray
 * date:2017/5/4 15:15
 */

public class MD5 {

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    public static String hashKeyForDisk(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }



    /** 小写 */
    public static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /** 大写 */
    public static final char HEX_DIGITS_UPPER[] = { '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };


    /**
     * 加密内容 - 32位大小MD5 - 小写
     *
     * @param str 加密内容
     */
    public static final String md5(String str) {
        try {
            return md5(str.getBytes());
        } catch (Exception e) {
            //JCLogUtils.eTag(TAG, e, "MD5");
        }
        return null;
    }


    /**
     * 加密内容 - 32位大小MD5 - 小写
     */
    public static final String md5(byte[] bytes) {
        try {
            // 获取MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(bytes);
            // 获取密文
            byte[] md = mdInst.digest();
            return toHexString(md, HEX_DIGITS);
        } catch (Exception e) {
            //JCLogUtils.eTag(TAG, e, "MD5");
        }
        return null;
    }


    /**
     * 加密内容 - 32位大小MD5 - 大写
     *
     * @param str 加密内容
     */
    public static final String md5Upper(String str) {
        try {
            return md5Upper(str.getBytes());
        } catch (Exception e) {
            //JCLogUtils.eTag(TAG, e, "MD5Upper");
        }
        return null;
    }


    /**
     * 加密内容 - 32位大小MD5 - 大写
     */
    public static final String md5Upper(byte[] bytes) {
        try {
            // 获取MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(bytes);
            // 获取密文
            byte[] md = mdInst.digest();
            return toHexString(md, HEX_DIGITS_UPPER);
        } catch (Exception e) {
            //JCLogUtils.eTag(TAG, e, "MD5Upper");
        }
        return null;
    }


    /**
     * 进行转换
     */
    public static String toHexString(byte[] bData) {
        return toHexString(bData, HEX_DIGITS);
    }


    /**
     * 进行转换
     */
    public static String toHexString(byte[] bData, char[] hexDigits) {
        if (bData == null || hexDigits == null) {
            return null;
        }
        StringBuilder sBuilder = new StringBuilder(bData.length * 2);
        for (int i = 0, len = bData.length; i < len; i++) {
            sBuilder.append(hexDigits[(bData[i] & 0xf0) >>> 4]);
            sBuilder.append(hexDigits[bData[i] & 0x0f]);
        }
        return sBuilder.toString();
    }


    /**
     * 获取文件MD5值 - 小写
     *
     * @param fPath 文件地址
     */
    public static String getFileMD5(String fPath) {
        try {
            InputStream fis = new FileInputStream(fPath);
            byte[] buffer = new byte[1024];
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
            return toHexString(md5.digest(), HEX_DIGITS);
        } catch (Exception e) {
            //JCLogUtils.eTag(TAG, e, "getFileMD5");
        }
        return null;
    }

}
