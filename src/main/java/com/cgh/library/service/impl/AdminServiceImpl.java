package com.cgh.library.service.impl;

import com.cgh.library.api.StatusCode;
import com.cgh.library.dto.OnlineUserDTO;
import com.cgh.library.exception.LibraryException;
import com.cgh.library.persistence.entity.Book;
import com.cgh.library.persistence.entity.User;
import com.cgh.library.persistence.repository.BookRepository;
import com.cgh.library.persistence.repository.UserRepository;
import com.cgh.library.service.AdminService;
import com.cgh.library.service.OnlineService;
import com.cgh.library.util.BeanUtil;
import com.cgh.library.util.EncryptUtil;
import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author cenganhui
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    private final OnlineService onlineService;

    @Override
    public void checkPermission() {
        OnlineUserDTO onlineUserDTO = onlineService.getOnlineUserDTO();
        if (!Boolean.TRUE.equals(onlineUserDTO.getAdmin())) {
            throw new LibraryException(StatusCode.NO_ADMIN_PERMISSION);
        }
    }

    @Override
    public Page<User> getAllUser(Integer page, Integer size, String username) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (!StringUtils.isNullOrEmpty(username)) {
            return userRepository.findAllByUsernameContaining(username, pageRequest);
        }
        return userRepository.findAll(pageRequest);
    }

    @Override
    public User createUser(User user) {
        user.setPassword(EncryptUtil.encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        user.setPassword(EncryptUtil.encryptPassword(user.getPassword()));
        User dbUser = userRepository.findUserById(user.getId());
        BeanUtil.copyPropertiesIgnoreNull(user, dbUser, "username");
        return userRepository.save(dbUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public Page<Book> getAllBook(Integer page, Integer size, String name) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (!StringUtils.isNullOrEmpty(name)) {
            return bookRepository.findAllByNameContaining(name, pageRequest);
        }
        return bookRepository.findAll(pageRequest);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findBookById(id);
    }

}
