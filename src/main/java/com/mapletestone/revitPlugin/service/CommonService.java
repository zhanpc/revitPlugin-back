package com.mapletestone.revitPlugin.service;


import com.mapletestone.revitPlugin.constant.FilePathConst;
import com.mapletestone.revitPlugin.pojo.FileDocumentVO;
import com.mapletestone.revitPlugin.pojo.RspVo;
import com.mapletestone.revitPlugin.utils.DateUtils;
import com.mapletestone.revitPlugin.utils.FileUtils;
import com.mapletestone.revitPlugin.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

/**
 * @description:
 * @Author hmx
 * @CreateTime 2021-06-25 12:56
 */

@Slf4j
@Service
public class CommonService {

    @Value("${web.file.root}")
    private String fileRootPath;


    /**
     * MultipartFile 上传文件
     * @param file
     * @param filePath
     * @return
     */
    public FileDocumentVO uploadFile(MultipartFile file, String filePath, String suffix) {
        FileDocumentVO fileDocumentVO=new FileDocumentVO();
        //新文件名
        String newFileName= UUIDUtils.getUUID() +"."+suffix;
        //源文件名称
        String oldFileName = file.getOriginalFilename();

        //创建输出文件夹路径
        String date= DateUtils.dateToString(new Date(),"yyyyMMdd");
        String directory =  fileRootPath + FilePathConst.PATH_UPLOAD_DOCUMENT + filePath+"/"+date;
        File newFile = new File(directory);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        String realPath =  directory+"/"+oldFileName;
        String path =  FilePathConst.PATH_UPLOAD_DOCUMENT + filePath+"/"+date+"/"+oldFileName;
        try {
            //输出
            FileUtils.writeFromStream(file.getInputStream(),realPath);
            fileDocumentVO.setFilePath(path);
            fileDocumentVO.setOriginalName(oldFileName);
            fileDocumentVO.setPresentName(newFileName);
            fileDocumentVO.setSize(file.getSize()+"");
            fileDocumentVO.setFileFormat(suffix);
            return fileDocumentVO;
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }

    /** 上传文件 */
    public String upload(MultipartFile file, String filePath, String fileName) {
        //创建输出文件夹路径
        String date= DateUtils.dateToString(new Date(),"yyyyMMdd");
        String directory =  fileRootPath + FilePathConst.PATH_UPLOAD_DOCUMENT + filePath+"/"+date;
        File newFile = new File(directory);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        String realPath =  directory+"/"+fileName;
        String path =  FilePathConst.PATH_UPLOAD_DOCUMENT + filePath+"/"+date+"/"+fileName;
        try {
            //输出
            FileUtils.writeFromStream(file.getInputStream(),realPath);
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }
        return path;
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param filePath
     * @param fileName
     */
    public void download(HttpServletRequest request, HttpServletResponse response,String filePath,String fileName) {
        File file = new File(fileRootPath+filePath);
        if (file.exists()) {
            RspVo.response(request,response,file,fileName);
        } else {
            throw new IllegalArgumentException("文件不存在~");
        }
    }

}
