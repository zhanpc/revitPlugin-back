package com.mapletestone.revitPlugin.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ReJeeParseUtils {


    public static String USR_DEFIED = "defined";

    // 7374617274 03 0039 252791D75D4A0F68 01 0029 0001 01 1C69DD60 0210 0310 0405 0501 06FFC0 07000B 0812 033000015B000A9A007F100400F6051D 8719 0104
    public static Map<String, Object> parseGatewayPacket(String hex){

        ByteBuffer buffer =  ByteBuffer.wrap(hexStringToBytes(hex));
        Map<String, Object> result =  new HashMap<>( 100 );
        byte[]  ss =  new byte[5];
        buffer.get(ss);
        result.put("start", new String(ss));
        result.put("version", buffer.get());
        result.put("packetLen", buffer.getShort());

        byte[]  nos =  new byte[8];
        buffer.get(nos);
        result.put("gatewayNo", bytesToHexString(nos));
        byte  dtype =  buffer.get();
        if (dtype == 0) {
            return result;
        }
        result.put("tlvLen", buffer.getShort());

        int index = 0;

        // gateway packet crc
        while (buffer.remaining() > 4) {
            Byte type  =  buffer.get();
            Integer t =  type.intValue();
            switch (t) {
                case 0x00: index++;  result.put("rfch", (int) buffer.get()) ; break;
                case 0x01: index+=4; result.put("chan", buffer.getInt()) ;break;
                case 0x02: index++;  result.put("modu", buffer.get()) ; break;
                case 0x03: index++;  result.put("stat", buffer.get()) ; break;
                case 0x04: index++;  result.put("datr", buffer.get()); break;
                case 0x05: index++;  result.put("codr", buffer.get()) ;break;
                case 0x06: index++;  result.put("rssi", buffer.getShort()); break;
                case 0x07: index++;  result.put("lsnr", buffer.getShort()); break;
                case 0x08:
                    index++;
                    byte len = buffer.get();
                    byte[] sensorData = new byte[len];
                    buffer.get(sensorData);
                    String sensorHex =  bytesToHexString(sensorData);
//                    result.put("size", buffer.getShort());
                    result.put("source", sensorHex);
                    result.put("sensors", parseSensorData(sensorHex));
                    break;
                default:
                    log.warn("unknown node data type: {}", type);
            }
        }

        return result;
    }

    /**
     * eg: 0300003E14000162005F7103048DB4C6
     * 03
     * 00003E14
     * 00
     * 0162
     * 00 5F71
     * 03 048D
     * B4C6
     * */
    public static Map<String, Object> parseSensorData(String hex){

        Map<String, Object> result =  new HashMap<>( 100 );

//        byte data[]= ByteUtils.hexStringToBytes(hex);
        byte data[]= DatatypeConverter.parseHexBinary(hex);
        ByteBuffer buffer =  ByteBuffer.wrap(data);
        result.put("version", buffer.get());

        byte no[] = new byte[4];
        buffer.get(no);

        String deviceNo =  bytesToHexString(no);
        int ctl =  buffer.get();
        int  seq  = buffer.getShort();
        result.put("seq", seq);
        result.put("deviceNo", deviceNo);

        Map<String, Object> values=   new HashMap<>(8);
        do {
            int type = buffer.get();

            switch (type){
                case 0x00:
                    int battery = (buffer.get()&0x1F);
                    int deviceType = (buffer.get());
                    result.put("battery", battery);
                    result.put("deviceType", deviceType);
                    break;
                case 0x03:
                    Short adc = buffer.getShort();
                    values.put("adc", adc);
                    break;
                case 0x04:
                    Short temperature = buffer.getShort();
                    values.put("temperature", temperature/10.0);
                    break;
                case 0x05: int humidity =buffer.get();
                    values.put("humidity", humidity);
                    break;
                case 0x06: int oxygen = buffer.get();
                    values.put("oxygen", oxygen);
                    break;
                case 0x07:
                    int  pressure = buffer.getInt();
                    values.put("pressure", pressure);
                    break;
                case 0x08:
                    values.put(USR_DEFIED,  buffer.getFloat());
                    break;
                case 0x09: int  switchStatus =buffer.get();
                    values.put("switchStatus", switchStatus);
                    break;
                case 0x21:
                case 0x24:
                case 0x25:
                    int dataLen =  buffer.get();
                    byte[] s =  new byte[dataLen];
                    buffer.get(s);
                    values.put("dtu", bytesToHexString(s));
                    break;
                default:
                    int len =  buffer.get();
                    byte[] src =  new byte[len];
                    buffer.get(src);
                    log.error("unknown data type: {} deviceNo:{} source:{}", type, deviceNo, bytesToHexString(src));
            }

        }while (buffer.remaining() > 2);
        if (!values.isEmpty()) {
            result.put("sensorData", values);
        }
        return result;
    }

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    /**
     * 将字节数字转换为16进制字符串
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) {
            // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }

    public static byte[] hexStringToBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }


    public static void main(String[] args) {
        String hex = "737461727402002a086855806806129801001a06000e070063081203300021e700000c007f100400c00523cdc10084";
        System.out.println(JSONObject.toJSONString(parseGatewayPacket(hex)));
    }

}
