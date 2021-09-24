package com.example.SpringBootStudy.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender implements IEnum<String> {

    FEMALE("F", "女"),
    MALE("M", "男"),
    UNKNOWN("U", "不願透漏");

    private final String code;
    private final String description;

    @Override
    @JsonValue
    public String getValue() {
        return this.code;
    }

    @Override
    public String toString() {
        return this.description;
    }

}
