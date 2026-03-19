package com.mapletestone.revitPlugin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.mapletestone.revitPlugin.constant.HttpConst;
import com.mapletestone.revitPlugin.dao.entity.Record;
import com.mapletestone.revitPlugin.dao.entity.User;
import com.mapletestone.revitPlugin.pojo.ResponseVO;
import com.mapletestone.revitPlugin.service.impl.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/record")
public class RecordController {
    @Resource
    private RecordService recordService;

    @PostMapping("/storageRecord")
    public ResponseVO<String> storageRecord( Record record, HttpSession session, MultipartFile multipartFile) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(ObjectUtils.isEmpty(loginUser)){
            return ResponseVO.failure(400, "请先登录");
        }
        boolean result = recordService.saveRecord(record.getRecord(), loginUser.getUserName(),multipartFile);
        return ResponseVO.getStatusJoMsg(result, "记录成功", "记录失败");
    }

    @GetMapping("/getRecordList")
    public ResponseVO<List<Record>> getRecordList(String like, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(ObjectUtils.isEmpty(loginUser)){
            return ResponseVO.failure(400, "请先登录");
        }
        List<Record> recordList = recordService.getRecordList(like);
        return ResponseVO.success(recordList);
    }

    @GetMapping("/getUserRecordList")
    public ResponseVO<IPage<Record>> getUserRecordList(String like, HttpSession session, String userName, Integer page, Integer size,String startDate,String endDate) {
//        User loginUser = (User) session.getAttribute("loginUser");
//        if(ObjectUtils.isEmpty(loginUser)){
//            return ResponseVO.failure(400, "请先登录");
//        }
        IPage<Record> recordList = recordService.getUserRecordList(like,userName,page,size,startDate,endDate);
        return ResponseVO.success(recordList);
    }

    @DeleteMapping("/deleteRecord")
    public ResponseVO<String> deleteRecord(String id) {
        boolean result = recordService.deleteRecord(id);
        return  ResponseVO.getStatusJoMsg(result, HttpConst.DELETE_SUCCESS, HttpConst.DELETE_FAILED);
    }
}
