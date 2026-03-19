package com.mapletestone.revitPlugin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapletestone.revitPlugin.pojo.FileDocumentVO;
import com.mapletestone.revitPlugin.service.BaseIService;
import com.mapletestone.revitPlugin.dao.entity.Record;
import com.mapletestone.revitPlugin.dao.mapper.RecordMapper;

import com.mapletestone.revitPlugin.service.CommonService;
import com.mapletestone.revitPlugin.utils.FileUtils;
import com.mapletestone.revitPlugin.utils.ZipTxtJsonReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
* 表服务实现类
*/
@Service
public class RecordService extends BaseIService<RecordMapper, Record> {
    @Resource
    private CommonService commonService;

    @Value("${web.file.root}")
    private String fileRootPath;

    public boolean saveRecord(String record, String loginUser, MultipartFile file) {
        Date date = new Date();
        Record recordEntity = new Record();
        recordEntity.setRecord(record);
        recordEntity.setCreatedUser(loginUser);
        recordEntity.setLastModifiedUser(loginUser);
        recordEntity.setCreatedTime( date);
        recordEntity.setLastModifiedTime( date);
        if(file!=null){
            //上传文件
            String suffixNotDot = FileUtils.getFileSuffixNotDot(file.getOriginalFilename());
            FileDocumentVO fileDocumentVO = commonService.uploadFile(file, "zip", suffixNotDot);
            String filePath = fileDocumentVO.getFilePath();
            recordEntity.setFilePath(filePath);
            String realZipPath = buildRealFilePath(filePath);
            System.out.println("realZipPath:"+realZipPath);
            recordEntity.setRecord(ZipTxtJsonReader.collectJsonString(realZipPath, loginUser));
        }
        return save(recordEntity);
    }

    private String buildRealFilePath(String filePath) {
        return fileRootPath + filePath.replace("/", File.separator);
    }

    public List<Record> getRecordList(String like) {
        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(like)){
            wrapper.and(w -> w.like(Record::getRecord, like)
                    .or()
                    .like(Record::getFilePath, like));
        }

        return list(wrapper);
    }

    public IPage<Record> getUserRecordList(String like, String userName, Integer page, Integer size, String startDate, String endDate) {
        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(like)){
            wrapper.and(w -> w.like(Record::getRecord, like)
                    .or()
                    .like(Record::getFilePath, like));
        }
        if(StringUtils.isNotEmpty(userName)){
            wrapper.like(Record::getCreatedUser,userName);
        }
        if (StringUtils.isNotEmpty(startDate)&&StringUtils.isNotEmpty(endDate)){
            wrapper.between(Record::getCreatedTime,startDate+ " 00:00:00",endDate+ " 23:59:59");
        }
        wrapper.orderByDesc(Record::getCreatedTime);

        return baseMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public boolean deleteRecord(String id) {
        return removeById(id);
    }
}
