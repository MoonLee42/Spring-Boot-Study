package com.example.SpringBootStudy.model.bo;

import com.example.SpringBootStudy.enums.Gender;
import com.example.SpringBootStudy.enums.UserType;
import com.example.SpringBootStudy.validation.MobileCheck;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.GroupSequence;
import javax.validation.constraints.*;
import java.util.Date;

@Data
@Accessors(chain = true)
public class UserBO {
    @NotBlank(message = "name should not be blank.", groups = NameValidLevel1.class)
    @Size(min = 2, max = 4, groups = NameValidLevel2.class)
    @Pattern(regexp = "[A-Z][a-zA-Z0-9]*", message = "invalid name", groups = NameValidLevel3.class)
    private String name;
    @NotBlank(message = "mobile number should not be blank.", groups = MobileValidLevel1.class)
    @MobileCheck(groups = MobileValidLevel2.class)
    private String mobileNumber;
    private UserType type;
    private Gender gender;

    @GroupSequence({NameValidLevel1.class, NameValidLevel2.class, NameValidLevel3.class})
    public interface NameValidGroup {}
    interface NameValidLevel1 {}
    interface NameValidLevel2 {}
    interface NameValidLevel3 {}
    @GroupSequence({MobileValidLevel1.class, MobileValidLevel2.class})
    public interface MobileValidGroup {}
    interface MobileValidLevel1 {}
    interface MobileValidLevel2 {}

}
