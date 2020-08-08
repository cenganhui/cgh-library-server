package com.cgh.library.service;

import com.cgh.library.persistence.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cenganhui
 */
public interface BookService {

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

    /**
     * 上传图书
     *
     * @param file 图书pdf
     * @return 图书
     */
    Book upload(MultipartFile file);

    /**
     * 更新图书
     *
     * @param book 图书
     * @return 更新后的图书
     */
    Book update(Book book);

    /**
     * 根据id持久化图书信息
     *
     * @param id 图书id
     * @return 图书
     */
    Book persistById(Long id);

}
