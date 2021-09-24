package com.example.SpringBootStudy.db.rmdb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.SpringBootStudy.db.rmdb.entity.User;

public interface UserMapper extends BaseMapper<User> {
    Page<User> selectByName(String userName, Page<User> page);
}
