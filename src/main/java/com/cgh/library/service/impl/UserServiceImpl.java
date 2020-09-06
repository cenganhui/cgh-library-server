package com.cgh.library.service.impl;

import com.cgh.library.api.PasswordReq;
import com.cgh.library.api.StatusCode;
import com.cgh.library.exception.LibraryException;
import com.cgh.library.persistence.entity.TieBaUser;
import com.cgh.library.persistence.entity.User;
import com.cgh.library.persistence.repository.TieBaUserRepository;
import com.cgh.library.persistence.repository.UserRepository;
import com.cgh.library.service.OnlineService;
import com.cgh.library.service.UserService;
import com.cgh.library.util.BeanUtil;
import com.cgh.library.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author cenganhui
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final OnlineService onlineService;

    private final TieBaUserRepository tieBaUserRepository;

    @Override
    public User updateUser(User user) {
        user.setUpdatedBy(onlineService.getCurrentUsername());
        user.setUpdateTime(LocalDateTime.now());
        User dbUser = userRepository.findUserById(user.getId());
        BeanUtil.copyPropertiesIgnoreNull(user, dbUser, "username", "password", "admin");
        // 查看当前用户是否绑定贴吧，若有，则修改邮箱
        if (dbUser.getTiebaId() != null) {
            TieBaUser tieBaUser = tieBaUserRepository.findTieBaUserById(dbUser.getTiebaId());
            tieBaUser.setEmail(dbUser.getEmail());
            tieBaUserRepository.save(tieBaUser);
        }
        return userRepository.save(dbUser);
    }

    @Override
    public void password(PasswordReq req) {
        req.setOldPassword(EncryptUtil.encryptPassword(req.getOldPassword()));
        req.setNewPassword(EncryptUtil.encryptPassword(req.getNewPassword()));
        User dbUser = userRepository.findUserByUsernameAndPassword(onlineService.getCurrentUsername(), req.getOldPassword());
        if (dbUser == null) {
            throw new LibraryException(StatusCode.REQUEST_PARAM_ILLEGAL.message("原密码不匹配"));
        }
        if (req.getNewPassword().equals(dbUser.getPassword())) {
            throw new LibraryException(StatusCode.REQUEST_PARAM_ILLEGAL.message("新旧密码相同"));
        }
        dbUser.setPassword(req.getNewPassword());
        dbUser.setUpdatedBy(onlineService.getCurrentUsername());
        dbUser.setUpdateTime(LocalDateTime.now());
        userRepository.save(dbUser);
    }

    @Override
    public User getInfo() {
        return userRepository.findUserByUsername(onlineService.getCurrentUsername());
    }

}
