package com.cgh.library.controller;

import com.cgh.library.api.BaseResponse;
import com.cgh.library.api.PasswordReq;
import com.cgh.library.persistence.entity.User;
import com.cgh.library.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author cenganhui
 */
@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Api(tags = "我的图书：用户接口")
public class UserController {

    private final UserService userService;

    @ApiOperation("修改用户")
    @PutMapping
    public BaseResponse<User> update(@Valid @RequestBody User user) {
        return BaseResponse.success(userService.updateUser(user));
    }

    @ApiOperation("修改密码")
    @PutMapping("password")
    public BaseResponse<String> password(@Valid @RequestBody PasswordReq req) {
        userService.password(req);
        return BaseResponse.success("密码修改成功");
    }

    @ApiOperation("获取个人信息")
    @GetMapping
    public BaseResponse<User> getInfo() {
        return BaseResponse.success(userService.getInfo());
    }

}
