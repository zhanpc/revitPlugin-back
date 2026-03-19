package com.mapletestone.revitPlugin.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class ZipTxtJsonReader {

    private static final Charset TXT_CHARSET = StandardCharsets.UTF_8;

    private ZipTxtJsonReader() {
    }

    public static JSONArray collectJsonArray(String zipPath, String password) {
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            if (!zipFile.isValidZipFile()) {
                throw new IllegalArgumentException("不是合法的 zip 文件: " + zipPath);
            }

            if (zipFile.isEncrypted()) {
                if (StringUtils.isBlank(password)) {
                    throw new IllegalArgumentException("当前 zip 已加密，但密码为空: " + zipPath);
                }
                zipFile.setPassword(password);
            }

            List<FileHeader> fileHeaders = listTargetFileHeaders(zipFile);
            if (fileHeaders.isEmpty()) {
                throw new IllegalArgumentException("zip 中没有找到 txt 或 json 文件: " + zipPath);
            }

            JSONArray result = new JSONArray();
            for (FileHeader fileHeader : fileHeaders) {
                mergeJsonContent(zipFile, zipPath, fileHeader, result);
            }
            return result;
        } catch (ZipException | IOException ex) {
            throw new IllegalArgumentException("解析 zip 文件失败: " + zipPath, ex);
        }
    }

    public static String collectJsonString(String zipPath, String password) {
        return JSON.toJSONString(collectJsonArray(zipPath, password));
    }

    private static List<FileHeader> listTargetFileHeaders(ZipFile zipFile) throws ZipException {
        List<FileHeader> fileHeaders = new ArrayList<>();
        for (Object headerObject : zipFile.getFileHeaders()) {
            FileHeader fileHeader = (FileHeader) headerObject;
            if (fileHeader.isDirectory()) {
                continue;
            }
            String fileName = fileHeader.getFileName();
            if (StringUtils.isNotBlank(fileName) && isTargetJsonFile(fileName)) {
                fileHeaders.add(fileHeader);
            }
        }
        fileHeaders.sort(Comparator.comparing(FileHeader::getFileName));
        return fileHeaders;
    }

    private static boolean isTargetJsonFile(String fileName) {
        String lowerFileName = fileName.toLowerCase(Locale.ROOT);
        return lowerFileName.endsWith(".txt") || lowerFileName.endsWith(".json");
    }

    private static void mergeJsonContent(ZipFile zipFile, String zipPath, FileHeader fileHeader, JSONArray result)
            throws ZipException, IOException {
        try (ZipInputStream inputStream = zipFile.getInputStream(fileHeader)) {
            String text = IOUtils.toString(inputStream, TXT_CHARSET);
            String content = removeBom(text).trim();
            if (content.isEmpty()) {
                return;
            }

            Object parsed = JSON.parse(content);
            if (parsed instanceof JSONArray) {
                result.addAll((JSONArray) parsed);
                return;
            }
            result.add(parsed);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "解析压缩包内 JSON 失败，zip: " + zipPath + "，entry: " + fileHeader.getFileName(),
                    ex
            );
        }
    }

    private static String removeBom(String text) {
        if (text != null && text.startsWith("\uFEFF")) {
            return text.substring(1);
        }
        return text;
    }
}
