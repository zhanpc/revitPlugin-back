package com.mapletestone.revitPlugin.pojo;

import lombok.Data;

/**
 * @description: 文件类
 * @Author hmx
 * @CreateTime 2021-06-25 14:09
 */

@Data
public class FileDocumentVO {

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 原文件名称
     */
    private String originalName;

    /**
     * 新文件名称
     */
    private String presentName;

    /**
     * 大小
     */
    private String size;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     *  1-是压缩包 0-否
     */
    private Integer isPackage;

}
