package com.cgh.library.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cgh.library.Constants;
import com.cgh.library.api.StatusCode;
import com.cgh.library.dto.OnlineUserDTO;
import com.cgh.library.exception.LibraryException;
import com.cgh.library.service.OnlineService;
import com.louislivi.fastdep.shirojwt.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author cenganhui
 */
@Service
@RequiredArgsConstructor
public class OnlineServiceImpl implements OnlineService {

    private final StringRedisTemplate redisTemplate;

    private final JwtUtil jwtUtil;

    @Override
    public Long getCurrentUserId() {
        return getOnlineUserDTO().getId();
    }

    @Override
    public OnlineUserDTO getOnlineUserDTO() {
        // 从 Redis 中获取当前用户信息
        OnlineUserDTO onlineUserDTO = JSONObject.parseObject(redisTemplate.opsForValue().get(Constants.REDIS_USER_PREFIX + jwtUtil.getUserId()), OnlineUserDTO.class);
        if (onlineUserDTO == null) {
            throw new LibraryException(StatusCode.NO_LOGIN);
        }
        return onlineUserDTO;
    }

}
