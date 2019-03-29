package com.charles.utils;

/**
 * 字节工具,提供了一系列的常见类型的数据与byte的互相转化
 *
 * @author CharlesLee
 */
public class ByteUtils {


    /**
     * 将 long 转换为byte
     */
    public static byte[] longToByte(long value) {
        byte[] result = new byte[8];
        long temp = value;
        for (int i = 0; i < result.length; i++) {
            result[result.length - 1 - i] = (byte) (temp & 0xff);
            temp >>= 8;
        }
        return result;
    }


    /**
     * 将 int 转换为byte
     */
    public static byte[] intToByte(int value) {
        byte[] byt = new byte[4];
        byt[3] = (byte) (0xff & value);
        byt[2] = (byte) ((0xff00 & value) >> 8);
        byt[1] = (byte) ((0xff0000 & value) >> 16);
        byt[0] = (byte) ((0xff000000 & value) >> 24);
        return byt;
    }

    /**
     * 将 byte数组对象转换回 long值
     */
    public static long byteToLong(byte[] bytes) {
        if (bytes == null || bytes.length != 8) {
            throw new IllegalArgumentException("需要转换的byte数组长度不为8位,无法解析");
        }

        long s0 = bytes[7] & 0xff;
        long s1 = bytes[6] & 0xff;
        long s2 = bytes[5] & 0xff;
        long s3 = bytes[4] & 0xff;
        long s4 = bytes[3] & 0xff;
        long s5 = bytes[2] & 0xff;
        long s6 = bytes[1] & 0xff;
        long s7 = bytes[0] & 0xff;

        // s0不变,最高位不需要动
        s1 <<= 8;
        s2 <<= 8 * 2;
        s3 <<= 8 * 3;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        return s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
    }

    /**
     * 将byte数组转换成为int
     */
    public static int byteToInt(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("需要转换的byte数组长度不为4位,无法解析");
        }
        int s0 = bytes[3] & 0xff;
        int s1 = bytes[2] & 0xff;
        int s2 = bytes[1] & 0xff;
        int s3 = bytes[0] & 0xff;

        // s0不变,最高位不需要动
        s1 <<= 8;
        s2 <<= 8 * 2;
        s3 <<= 8 * 3;
        return s0 | s1 | s2 | s3;
    }
}
