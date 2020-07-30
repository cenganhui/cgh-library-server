package com.cgh.library.service;

import com.cgh.library.dto.OnlineUserDTO;

/**
 * @author cenganhui
 */
public interface OnlineService {

    /**
     * 获取当前用户id
     *
     * @return 用户id
     */
    Long getCurrentUserId();

    /**
     * 获取当前在线用户DTO
     *
     * @return 在线用户DTO
     */
    OnlineUserDTO getOnlineUserDTO();

    /**
     * 获取当前用户的用户名
     *
     * @return 用户名
     */
    String getCurrentUsername();

}
