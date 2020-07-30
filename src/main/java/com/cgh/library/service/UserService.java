package com.cgh.library.service;

import com.cgh.library.api.PasswordReq;
import com.cgh.library.persistence.entity.User;

/**
 * @author cenganhui
 */
public interface UserService {

    /**
     * 修改用户信息
     *
     * @param user 用户
     * @return 用户
     */
    User updateUser(User user);

    /**
     * 修改密码
     *
     * @param req 修改密码请求
     */
    void password(PasswordReq req);

    /**
     * 获取用户信息
     *
     * @return 用户
     */
    User getInfo();

}
