package com.example.SpringBootStudy.db.rmdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.SpringBootStudy.enums.Gender;
import com.example.SpringBootStudy.enums.UserType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private UserType type;
    private Gender gender;
    private String mobileNumber;
    private Date createTime;
    private Date modifiedTime;
}
