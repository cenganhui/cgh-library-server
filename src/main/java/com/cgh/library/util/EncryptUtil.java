package com.cgh.library.util;

import org.springframework.util.DigestUtils;

/**
 * @author cenganhui
 */
public class EncryptUtil {

    /**
     * 密码加密
     *
     * @param sourcePassword 原密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String sourcePassword) {
        return DigestUtils.md5DigestAsHex(sourcePassword.getBytes());
    }

}
