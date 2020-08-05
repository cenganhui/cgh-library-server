package com.cgh.library.service.impl;

import com.alibaba.fastjson.JSON;
import com.cgh.library.Constants;
import com.cgh.library.api.LoginRes;
import com.cgh.library.api.StatusCode;
import com.cgh.library.dto.AuthUserDTO;
import com.cgh.library.dto.OnlineUserDTO;
import com.cgh.library.exception.LibraryException;
import com.cgh.library.persistence.entity.User;
import com.cgh.library.persistence.repository.UserRepository;
import com.cgh.library.service.AuthorizationService;
import com.cgh.library.util.EncryptUtil;
import com.louislivi.fastdep.shirojwt.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author cenganhui
 */
@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final StringRedisTemplate redisTemplate;

    @Override
    public LoginRes login(AuthUserDTO authUserDTO, String ip) {
        authUserDTO.setPassword(DigestUtils.md5DigestAsHex(authUserDTO.getPassword().getBytes()));
        User user = userRepository.findUserByUsernameAndPassword(authUserDTO.getUsername(), authUserDTO.getPassword());
        if (user == null) {
            // 用户名或密码错误
            throw new LibraryException(StatusCode.LOGIN_FAILED);
        }
        String token = jwtUtil.sign(authUserDTO.getUsername());
        LoginRes loginRes = new LoginRes();
        loginRes.setToken(token);
        loginRes.setUser(user);
        // 设置在线用户信息
        OnlineUserDTO onlineUserDTO = new OnlineUserDTO();
        onlineUserDTO.setUserName(user.getUsername());
        onlineUserDTO.setNickName(user.getNickName());
        onlineUserDTO.setId(user.getId());
        onlineUserDTO.setAdmin(user.getAdmin());
        onlineUserDTO.setIp(ip);
        onlineUserDTO.setKey(Constants.REDIS_USER_PREFIX + user.getUsername());
        onlineUserDTO.setLoginTime(new Date());
        // 将 onlineUserDTO 保存至 Redis
        redisTemplate.opsForValue().set(onlineUserDTO.getKey(), JSON.toJSONString(onlineUserDTO), Duration.ofSeconds(jwtUtil.fastDepShiroJwtProperties.getExpireTime()));
        return loginRes;
    }

    @Override
    public void logout() {
        redisTemplate.delete(Constants.REDIS_USER_PREFIX + jwtUtil.getUserId());
    }

    @Override
    public User createUser(User user) {
        user.setPassword(EncryptUtil.encryptPassword(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

}
