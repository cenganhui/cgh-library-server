package com.cgh.library.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgh.library.Constants;
import com.cgh.library.api.StatusCode;
import com.cgh.library.exception.LibraryException;
import com.cgh.library.persistence.entity.Book;
import com.cgh.library.persistence.repository.BookRepository;
import com.cgh.library.service.BookService;
import com.cgh.library.service.OnlineService;
import com.cgh.library.util.BeanUtil;
import com.mysql.cj.util.StringUtils;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author cenganhui
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final OnlineService onlineService;

    private final StringRedisTemplate redisTemplate;

    @Override
    public Page<Book> getAllBook(Integer page, Integer size, String name) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (!StringUtils.isNullOrEmpty(name)) {
            return bookRepository.findAllByNameContainingAndUserId(name, onlineService.getCurrentUserId(), pageRequest);
        }
        return bookRepository.findAllByUserId(onlineService.getCurrentUserId(), pageRequest);
    }

    @Override
    public void deleteBook(Long id) {
        matchUserId(id);
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBookById(Long id) {
        matchUserId(id);
        return bookRepository.findBookById(id);
    }

    @Override
    public Book upload(MultipartFile file) {
        // TODO 待重构
        String name = file.getOriginalFilename();
        if (StringUtil.isNullOrEmpty(name)) {
            throw new LibraryException(StatusCode.REQUEST_PARAM_ILLEGAL);
        }
        if (checkFormat(name)) {
            String fileName = UUID.randomUUID().toString();
            String filePath = String.format("%s%s.pdf", Constants.SAVE_FILE_PATH, fileName);
            // 保存文件到磁盘
            File dest = new File(filePath);
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = Constants.HOST + fileName;
            Book book = new Book();
            book.setName(name);
            book.setUrl(url);
            book.setFilePath(filePath);
            book.setUserId(onlineService.getCurrentUserId());
            book.setCreateBy(onlineService.getCurrentUsername());
            book.setCreateTime(LocalDateTime.now());
            book.setCurrentPage(0);
            book.setTotalPage(0);
            return bookRepository.save(book);
        } else {
            throw new LibraryException(StatusCode.BOOK_FORMAT_ERROR);
        }
    }

    @Override
    public Book update(Book book) {
        // 将 book 保存至 Redis，过期时间为 2 小时
        redisTemplate.opsForValue().set(Constants.REDIS_BOOK_PREFIX + book.getId(), JSON.toJSONString(book), 2, TimeUnit.HOURS);
        return book;
    }

    @Override
    public Book persistById(Long id) {
        // 从 Redis 中获取图书信息
        Book book = JSONObject.parseObject(redisTemplate.opsForValue().get(Constants.REDIS_BOOK_PREFIX + id), Book.class);
        if (book == null) {
            throw new LibraryException(StatusCode.NOT_FOUND_BOOK);
        }
        // 查询数据库是否有此书，有则复制更新
        Book dbBook = bookRepository.findBookById(id);
        if (dbBook != null) {
            BeanUtil.copyPropertiesIgnoreNull(book, dbBook);
            dbBook.setUpdatedBy(onlineService.getCurrentUsername());
            dbBook.setUpdateTime(LocalDateTime.now());
            return bookRepository.save(dbBook);
        } else {
            throw new LibraryException(StatusCode.NOT_FOUND_BOOK);
        }
    }

    /**
     * 图书所属用户id是否与当前用户id匹配
     *
     * @param id 图书id
     */
    private void matchUserId(Long id) {
        Book book = bookRepository.findBookById(id);
        if (!book.getUserId().equals(onlineService.getCurrentUserId())) {
            throw new LibraryException(StatusCode.BOOK_UNAUTHORIZED_ACCESS);
        }
    }

    /**
     * 检验上传的文件是否是 pdf 格式
     *
     * @param name 文件名称
     * @return 是否合格
     */
    private Boolean checkFormat(String name) {
        String[] str = name.split("\\.");
        if (str.length == 0) {
            return false;
        }
        String suffix = str[str.length - 1];
        return Constants.BOOK_FORMAT.equals(suffix);
    }

}
