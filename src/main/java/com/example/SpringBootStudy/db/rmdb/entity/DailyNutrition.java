package com.example.SpringBootStudy.db.rmdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
@TableName
public class DailyNutrition {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer energy;
    private Integer protein;
    private Integer fat;
    private Integer carbohydrate;
    private ZonedDateTime createTime;
    private ZonedDateTime modifiedTime;
}
