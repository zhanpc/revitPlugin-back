package com.mapletestone.revitPlugin.utils;

/**
 * @BelongsProject: iot-project
 * @BelongsPackage: com.mapletestone.iot.iotproject.utils
 * @Author: zhanpc
 * @CreateTime: 2024-03-13  10:19
 * @Description: TODO
 */
public class NumberConverter {
    // 二进制转换为十进制
    public static int binaryToDecimal(String binary) {
        return Integer.parseInt(binary, 2);
    }

    // 十进制转换为二进制
    public static String decimalToBinary(int decimal) {
        return Integer.toBinaryString(decimal);
    }

    // 十进制转换为十六进制
    public static String decimalToHex(int decimal) {
        return Integer.toHexString(decimal);
    }

    // 十六进制转换为十进制
    public static int hexToDecimal(String hex) {
        return Integer.parseInt(hex, 16);
    }

    // 二进制转换为十六进制
    public static String binaryToHex(String binary) {
        int decimal = binaryToDecimal(binary);
        return decimalToHex(decimal);
    }

    // 十六进制转换为二进制
    public static String hexToBinary(String hex) {
        int decimal = hexToDecimal(hex);
        return decimalToBinary(decimal);
    }

}
