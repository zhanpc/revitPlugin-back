package com.mapletestone.revitPlugin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapletestone.revitPlugin.service.BaseIService;
import com.mapletestone.revitPlugin.dao.entity.User;
import com.mapletestone.revitPlugin.dao.mapper.UserMapper;

import org.springframework.stereotype.Service;

/**
* 表服务实现类
*/
@Service
public class UserService extends BaseIService<UserMapper, User> {

    public User getUserByUser(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, user.getUserName());
        wrapper.eq(User::getPassword, user.getPassword());
        return this.getOne(wrapper);
    }

    public IPage<User> getUsers(Integer page, Integer size) {
        return baseMapper.selectPage(new Page<>(page, size), null);
    }

    public boolean deleteUser(String id) {
        return this.removeById(id);
    }
}
