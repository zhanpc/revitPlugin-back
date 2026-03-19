package com.mapletestone.revitPlugin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mapletestone.revitPlugin.constant.HttpConst;
import com.mapletestone.revitPlugin.dao.entity.User;
import com.mapletestone.revitPlugin.pojo.ResponseVO;
import com.mapletestone.revitPlugin.service.impl.UserService;
import com.mapletestone.revitPlugin.utils.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: iot-project
 * @BelongsPackage: com.mapletestone.iot.iotproject.controller
 * @Author: zhanpc
 * @CreateTime: 2024-01-15  16:49
 * @Description: TODO
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("/login")
    public ResponseVO<String> login(@RequestBody User user,HttpSession session) {
        User searchUser = userService.getUserByUser(user);
        if(ObjectUtils.isEmpty(searchUser)){
            return ResponseVO.failure(400,"验证失败");
        }else{
            session.setAttribute("loginUser", searchUser);
            return ResponseVO.success("验证成功");
        }
    }

    @PostMapping("/generateUser")
    public ResponseVO<String> generateUser(@RequestBody(required = false) User user) {
        boolean result = true;
        Date now = new Date();
        if(ObjectUtils.isEmpty(user)){
            User user1 = new User();
            user1.setUserName(StrUtils.generateRandomString(6));
            user1.setPassword(StrUtils.generateRandomString(8));
            user1.setCreatedTime(now);
            user1.setLastModifiedTime(now);
            result = userService.saveActionBase(user1);
        }else{
            User searchUser = userService.getUserByUser(user);
            if(ObjectUtils.isNotEmpty(searchUser)){
                return ResponseVO.getStatusJoMsg(false, "用户已存在", "用户已存在");
            }
            user.setCreatedTime(now);
            user.setLastModifiedTime(now);
            result = userService.saveActionBase(user);
        }
        return ResponseVO.getStatusJoMsg(result, "生成成功", "生成失败");
    }

    @GetMapping("/getUsers")
    public ResponseVO<IPage<User>> getUsers(Integer page,Integer size) {
        IPage<User> userList = userService.getUsers(page,size);
        return ResponseVO.success(userList);
    }

    @DeleteMapping("/deleteUser")
    public ResponseVO<String> deleteUser(String id) {
        boolean result = userService.deleteUser(id);
        return  ResponseVO.getStatusJoMsg(result, HttpConst.DELETE_SUCCESS, HttpConst.DELETE_FAILED);
    }
}
