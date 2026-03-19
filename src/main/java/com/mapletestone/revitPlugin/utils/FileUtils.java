package com.mapletestone.revitPlugin.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author
 * @date 7/15/2020 9:25 AM
 */
public class FileUtils {


    /**
     * 读取字节流
     */
    public static  <T extends OutputStream> void readFile(T stream, File file, InputStream inputStream) throws Exception {
        InputStream in;
        if (inputStream == null) {
            in = new FileInputStream(file);
        } else {
            in = inputStream;
        }

        int bytesRead = 0;
        byte[] buffer = new byte[2048];
        while ((bytesRead = in.read(buffer)) != -1) {
            stream.write(buffer, 0, bytesRead);
        }
        stream.flush();
        in.close();
    }

    /**
     * 输出文件
     * @param inputStream
     * @param realPath
     * @throws Exception
     */
    public static void writeFromStream(InputStream inputStream, String realPath) throws Exception {
        FileOutputStream out = new FileOutputStream(realPath);
        byte[] buffer = new byte[1024];
        int pos;
        while ((pos = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, pos);
            out.flush();
        }
        inputStream.close();
        out.close();
    }

    public static void outPutFile(MultipartFile file, String path) throws IOException {
        InputStream in = file.getInputStream();
        FileOutputStream out = new FileOutputStream(path);
        byte[] buffer = new byte[1024];
        int pos;
        while ((pos = in.read(buffer)) != -1) {
            out.write(buffer, 0, pos);
            out.flush();
        }
        in.close();
        out.close();
    }

    /**
     * 获取文件后缀
     *
     * @param str 文件名
     * @return 文件后缀（带点）
     */
    public static String getFileSuffix(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        int startIndex = str.lastIndexOf(".");
        if (startIndex == -1) {
            return "";
        }
        return str.substring(startIndex);
    }

    /**
     * 获取文件后缀
     * @param str 文件名
     * @return 文件后缀（不带点）
     */
    public static String getFileSuffixNotDot(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        int startIndex = str.lastIndexOf(".");
        if (startIndex == -1) {
            return null;
        }
        return str.substring(startIndex + 1);
    }


    public static void makeDir(String s) {
        File path = new File(s);
        if (!path.exists()){
            path.mkdirs();
        }
    }


    /**
     * 文件重命名
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @param newName  文件新名称
     * @return 文件完整路径和名称
     */
    public boolean fileRename(String filePath, String fileName, String newName) {
        String newFileName = filePath + newName;
        return new File( filePath + fileName).renameTo(new File(newFileName));
    }


    /**
     * 清空文件夹
     */
    private static void deleteFolder(String folderPath) throws Exception {
        String newPath = folderPath;
        deleteAllFile(newPath); // 删除完里面所有内容
        File myFilePath = new File(newPath);
        myFilePath.delete(); // 删除空文件夹

    }

    /**
     * 删除文件
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File( filePath + fileName);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     * 删除路径及文件夹
     */
    public static boolean deleteAllFile(String path) throws Exception {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (String s : tempList != null ? tempList : new String[0]) {
            if (path.endsWith(File.separator)) {
                temp = new File( path + s);
            } else {
                temp = new File(path + File.separator + s);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                deleteAllFile(path + "/" + s);// 先删除文件夹里面的文件
                deleteFolder(path + "/" + s);// 再删除空文件夹
                flag = true;
            }
        }

        return flag;
    }

    /**
     * * * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     */
    public void copyFile(String oldPath, String newPath) throws Exception {
        File oldFile = new File(oldPath);
        if (oldFile.exists()) { //文件存在时
            InputStream inStream = new FileInputStream(oldPath); //读入原文件
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newPath));
            // 读取字节流
            FileUtils.readFile(out, null, inStream);
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     */
    public void copyFolder(String oldPath, String newPath) throws Exception {
        new File(newPath).mkdirs(); //如果文件夹不存在 则建立新文件夹
        File a = new File(oldPath);
        String[] file = a.list();
        File temp = null;
        for (String s : file != null ? file : new String[0]) {
            if (oldPath.endsWith(File.separator)) {
                temp = new File(oldPath + s);
            } else {
                temp = new File(oldPath + File.separator + s);
            }
            if (temp.isFile()) {
                FileInputStream input = new FileInputStream(temp);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newPath + "/" + temp.getName()));
                // 读取字节流
                FileUtils.readFile(out, null, input);
                out.close();
            }
            if (temp.isDirectory()) {//如果是子文件夹
                copyFolder(oldPath + "/" + s, newPath + "/" + s);
            }
        }
    }

    /**
     * 根据路径创建文件目录
     */
    public boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        return dir.mkdirs();
    }

    public File inputStreamtofile(InputStream ins, String filePath, String fileName) throws Exception {
        File file = new File(filePath + "/" + fileName);
        if (!file.exists()) {
            this.createDir(filePath);
            // 高级流 有缓存区默认大小8k，可自定义缓存区大小，有flush方法
            // flush调用write方法，缓存区的数据写入文件，缓存区满将数据写入文件 效率高效
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath + "/" + fileName));
            FileUtils.readFile(out, file, null);
        }

        return file;
    }

}
