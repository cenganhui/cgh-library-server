package com.cgh.library.service;

import com.cgh.library.persistence.entity.Book;
import com.cgh.library.persistence.entity.User;
import org.springframework.data.domain.Page;

/**
 * @author cenganhui
 */
public interface AdminService {

    /**
     * 检查管理员权限
     */
    void checkPermission();

    /**
     * 根据用户名查询所有用户
     *
     * @param page     页码
     * @param size     大小
     * @param username 用户名
     * @return 用户列表
     */
    Page<User> getAllUser(Integer page, Integer size, String username);

    /**
     * 创建新用户
     *
     * @param user 用户
     * @return 创建后用户
     */
    User createUser(User user);

    /**
     * 更新用户
     *
     * @param user 用户
     * @return 更新后用户
     */
    User updateUser(User user);

    /**
     * 根据id删除用户
     *
     * @param id 用户id
     */
    void deleteUser(Long id);

    /**
     * 根据id获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    User getUserById(Long id);

    /**
     * 根据名称查询图书
     *
     * @param page 页码
     * @param size 大小
     * @param name 名称
     * @return 图书列表
     */
    Page<Book> getAllBook(Integer page, Integer size, String name);

    /**
     * 根据id删除图书
     *
     * @param id 图书id
     */
    void deleteBook(Long id);

    /**
     * 根据id查询图书信息
     *
     * @param id 图书id
     * @return 图书
     */
    Book getBookById(Long id);

}
