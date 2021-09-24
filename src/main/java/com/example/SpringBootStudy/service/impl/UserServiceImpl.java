package com.example.SpringBootStudy.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.SpringBootStudy.db.rmdb.entity.User;
import com.example.SpringBootStudy.db.rmdb.mapper.UserMapper;
import com.example.SpringBootStudy.model.bo.UserBO;
import com.example.SpringBootStudy.model.vo.UserVO;
import com.example.SpringBootStudy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public Long insertUser(UserBO bo) {
        log.trace("insertUser: data: {}", bo);
        User user = new User()
                .setName(bo.getName())
                .setMobileNumber(bo.getMobileNumber())
                .setType(bo.getType())
                .setGender(bo.getGender())
                .setCreateTime(new Date());
        userMapper.insert(user);
        log.trace("insertUser: success: {}", user);
        return user.getId();
    }

    @Override
    public UserVO selectById(Long id) {
        log.trace("selectById: id: {}", id);
        User user = userMapper.selectById(id);
        log.trace("selectById: result: {}", user);
        if(user != null) {
            return toUserVO(user);
        }
        return null;
    }

    @Override
    public List<UserVO> selectByName(String name, Page<User> page) {
        log.trace("selectByName: name: {}, page: current: {} / size: {} / total: {}", name, page.getCurrent(), page.getSize(), page.getTotal());
        MDC.put("X-name", name);
        page = userMapper.selectByName(name, page);
        log.trace("selectByName: records: {}", page.getRecords());
        return toUserVOs(page.getRecords());
    }

    @Override
    public Long updateById(Long id, UserBO bo) {
        User user = userMapper.selectById(id)
                .setName(bo.getName())
                .setMobileNumber(bo.getMobileNumber())
                .setType(bo.getType())
                .setGender(bo.getGender())
                .setModifiedTime(new Date());
        userMapper.updateById(user);
        return user.getId();
    }

    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    private List<UserVO> toUserVOs(List<User> users) {
        return users.stream().map(user -> toUserVO(user)).collect(Collectors.toList());
    }

    private UserVO toUserVO(User user) {
        return new UserVO()
                .setId(user.getId())
                .setName(user.getName())
                .setMobileNumber(user.getMobileNumber())
                .setType(user.getType())
                .setGender(user.getGender())
                .setCreateTime(user.getCreateTime())
                .setModifiedTime(user.getModifiedTime());
    }

}
