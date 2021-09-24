package com.example.SpringBootStudy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.SpringBootStudy.db.rmdb.entity.User;
import com.example.SpringBootStudy.model.bo.UpdateUserBO;
import com.example.SpringBootStudy.model.bo.UserBO;
import com.example.SpringBootStudy.model.vo.UserVO;

import java.util.List;

public interface UserService {
    Long insertUser(UserBO bo);
    UserVO selectById(Long id);
    List<UserVO> selectByName(String name, Page<User> page);

    Long updateById(Long id,UserBO bo);

    void deleteById(Long id);
}
