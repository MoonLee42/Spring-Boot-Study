package com.example.SpringBootStudy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.SpringBootStudy.db.rmdb.entity.User;
import com.example.SpringBootStudy.model.bo.UpdateUserBO;
import com.example.SpringBootStudy.model.bo.UserBO;
import com.example.SpringBootStudy.model.vo.UserVO;
import com.example.SpringBootStudy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public String selectUser(@RequestParam(name = "id", required = false) Long id) {
        UserVO userVO = null;
        if (Objects.nonNull(id)) {
            userVO = userService.selectById(id);
        }
        if (Objects.nonNull(userVO)) {
            return userVO.toString();
        } else {
            return "not found";
        }
    }

    @GetMapping("/users")
    public String selectUsers(@RequestParam(name = "name", required = false) String name, Page<User> page) {
        StringBuilder data = new StringBuilder();
        List<UserVO> userVOs = userService.selectByName(name, page);
        if (!userVOs.isEmpty()) {
            userVOs.forEach(userVO -> {
                if (data.length() > 0)
                    data.append(',');
                data.append(userVO);
            });
        }
        return String.format("Page(current=%d,size=%d,total=%d,hasPrevious=%b,hasNext=%b),Data[%s]"
                , page.getCurrent(), page.getSize(), page.getTotal(), page.hasPrevious(), page.hasNext(), data.toString());
    }

    @PostMapping("/user")
    public String insertUser(
            @Validated({UserBO.NameValidGroup.class, UserBO.MobileValidGroup.class}) @RequestBody UserBO bo/*, BindingResult error*/) {
//        if (error.hasErrors()) {
//            String errorTemplate = "{\"failure\":\"%s\",\"field\":\"%s\",\"message\":\"%s\"}";
//            StringBuilder errorFields = new StringBuilder();
//            errorFields.append('[');
//            error.getFieldErrors().stream().forEach(fieldError -> {
//                if (errorFields.length() > 1)
//                    errorFields.append(',');
//                errorFields.append(String.format(errorTemplate, fieldError.getCode(), fieldError.getField(), fieldError.getDefaultMessage()));
//            });
//            return errorFields.append(']').toString();
//        }
        long userId = userService.insertUser(bo);
        return String.format("{\"id\":%d}", userId);
    }

    @PatchMapping("/user")
    public String updateUser(@RequestParam(name = "id") Long id, @RequestBody UserBO bo) {
        if(Objects.nonNull(userService.selectById(id))) {
            userService.updateById(id, bo);
            return "Updated id:" + id;
        }
        return "not found";
    }


    @DeleteMapping( "/user")
    public String deleteUser(@RequestParam(name = "id") Long id){
        if(Objects.nonNull(userService.selectById(id))) {
            userService.deleteById(id);
            return "Deleted id:" + id;
        }
        return "not found";
    }

}
