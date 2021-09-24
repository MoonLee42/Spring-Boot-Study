package com.example.SpringBootStudy.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserType implements IEnum<String> {

    USER("1", "使用者"),
    VIP("2", "付費使用者"),
    ADMIN("3", "管理員");

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
