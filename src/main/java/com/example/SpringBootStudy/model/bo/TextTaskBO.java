package com.example.SpringBootStudy.model.bo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class TextTaskBO implements Serializable {

    private String applicationName;
    @NotBlank(message = "Text should not be blank.")
    private String text;
    private Date createTime;
    private Date expiredTime;

    public TextTaskBO init() {
        createTime = new Date();
        return this;
    }

}
