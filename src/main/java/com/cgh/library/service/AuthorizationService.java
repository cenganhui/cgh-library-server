package com.cgh.library.service;

import com.cgh.library.api.LoginRes;
import com.cgh.library.dto.AuthUserDTO;

/**
 * @author cenganhui
 */
public interface AuthorizationService {

    /**
     * 登录
     *
     * @param authUserDTO 登录用户
     * @param ip          ip地址
     * @return 登录响应
     */
    LoginRes login(AuthUserDTO authUserDTO, String ip);

    /**
     * 登出
     */
    void logout();

}
