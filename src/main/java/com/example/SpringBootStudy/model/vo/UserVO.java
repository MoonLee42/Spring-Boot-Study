package com.example.SpringBootStudy.model.vo;

import com.example.SpringBootStudy.enums.Gender;
import com.example.SpringBootStudy.enums.UserType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserVO {
    private Long id;
    private String name;
    private String mobileNumber;
    private UserType type;
    private Gender gender;
    private Date createTime;
    private Date modifiedTime;
}
