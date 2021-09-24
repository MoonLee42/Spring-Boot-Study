package com.example.SpringBootStudy.model.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class UpdateUserBO {
    @NotNull
    private Long id;
    @NotBlank
    @Size(max = 64)
    private String name;
}
