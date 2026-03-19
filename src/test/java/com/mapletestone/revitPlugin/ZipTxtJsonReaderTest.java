package com.mapletestone.revitPlugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mapletestone.revitPlugin.utils.ZipTxtJsonReader;
import org.junit.jupiter.api.Test;

/**
 * 读取 zip 中所有 txt/json 文件里的 JSON 内容，并汇总成一个 JSON 数组输出。
 */
public class ZipTxtJsonReaderTest {

    private static final String ZIP_PATH = "C:\\Users\\OUYANG\\Desktop\\Envelope_20260318_201505.zip";
    private static final String ZIP_PASSWORD = "sap2idea";

    @Test
    public void readZipTxtJsonAndPrint() {
        System.out.println(JSON.toJSONString(
                ZipTxtJsonReader.collectJsonArray(ZIP_PATH, ZIP_PASSWORD),
                SerializerFeature.PrettyFormat
        ));
    }
}
