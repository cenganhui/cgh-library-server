package com.cgh.library.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author cenganhui
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final OnlineService onlineService;

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
            String filePath = String.format("%s%s.pdf", Constants.SAVE_FILE_PATH, UUID.randomUUID());
            // 保存文件到磁盘
            File dest = new File(filePath);
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = Constants.HOST;
            Book book = new Book();
            book.setName(name);
            book.setUrl(url);
            book.setFilePath(filePath);
            book.setUserId(onlineService.getCurrentUserId());
//        book.setCurrentPage(0);
//        book.setTotalPage(0);
            return bookRepository.save(book);
        } else {
            throw new LibraryException(StatusCode.BOOK_FORMAT_ERROR);
        }
    }

    @Override
    public Book update(Book book) {
        Book dbBook = bookRepository.findBookById(book.getId());
        if (dbBook != null) {
            BeanUtil.copyPropertiesIgnoreNull(book, dbBook);
            return bookRepository.save(dbBook);
        } else {
            throw new LibraryException(StatusCode.NOT_FOUND_BOOK);
        }
    }

    /**
     * 图书所属用户id是否与当前用户id匹配
     *
     * @param id 图书id
     * @return 结果
     */
    private Boolean matchUserId(Long id) {
        Book book = bookRepository.findBookById(id);
        if (!book.getUserId().equals(onlineService.getCurrentUserId())) {
            throw new LibraryException(StatusCode.BOOK_UNAUTHORIZED_ACCESS);
        }
        return true;
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