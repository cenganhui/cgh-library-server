package com.cgh.library.service.impl;

import com.cgh.library.api.PasswordReq;
import com.cgh.library.api.StatusCode;
import com.cgh.library.exception.LibraryException;
import com.cgh.library.persistence.entity.User;
import com.cgh.library.persistence.repository.UserRepository;
import com.cgh.library.service.UserService;
import com.cgh.library.util.BeanUtil;
import com.cgh.library.util.EncryptUtil;
import com.louislivi.fastdep.shirojwt.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author cenganhui
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Override
    public User updateUser(User user) {
        User dbUser = userRepository.findUserById(user.getId());
        BeanUtil.copyPropertiesIgnoreNull(user, dbUser, "username", "password", "admin");
        return userRepository.save(dbUser);
    }

    @Override
    public void password(PasswordReq req) {
        req.setOldPassword(EncryptUtil.encryptPassword(req.getOldPassword()));
        req.setNewPassword(EncryptUtil.encryptPassword(req.getNewPassword()));
        User dbUser = userRepository.findUserByUsernameAndPassword(jwtUtil.getUserId(), req.getOldPassword());
        if (dbUser == null) {
            throw new LibraryException(StatusCode.REQUEST_PARAM_ILLEGAL.message("原密码不匹配"));
        }
        if (req.getNewPassword().equals(dbUser.getPassword())) {
            throw new LibraryException(StatusCode.REQUEST_PARAM_ILLEGAL.message("新旧密码相同"));
        }
        dbUser.setPassword(req.getNewPassword());
    }

    @Override
    public User getInfo() {
        return userRepository.findUserByUsername(jwtUtil.getUserId());
    }

}
