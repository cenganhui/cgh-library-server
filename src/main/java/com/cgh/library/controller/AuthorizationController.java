package com.cgh.library.controller;

import com.cgh.library.api.BaseResponse;
import com.cgh.library.api.LoginRes;
import com.cgh.library.dto.AuthUserDTO;
import com.cgh.library.persistence.entity.User;
import com.cgh.library.service.AuthorizationService;
import com.cgh.library.util.HttpServletUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cenganhui
 */
@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Api(tags = "我的图书：授权接口")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @ApiOperation("用户登录")
    @PostMapping("login")
    public BaseResponse<LoginRes> login(@RequestBody AuthUserDTO authUserDTO, HttpServletRequest request) {
        return BaseResponse.success(authorizationService.login(authUserDTO, HttpServletUtil.getIp(request)));
    }

    @ApiOperation("用户注册")
    @PostMapping("register")
    public BaseResponse<User> register(@RequestBody User user) {
        return BaseResponse.success(authorizationService.createUser(user));
    }

    @ApiOperation("退出登录")
    @DeleteMapping("logout")
    public BaseResponse<String> logout() {
        authorizationService.logout();
        return BaseResponse.success("logout success");
    }

}
