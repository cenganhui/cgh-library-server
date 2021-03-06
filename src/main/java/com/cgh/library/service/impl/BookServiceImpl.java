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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        String name = file.getOriginalFilename();
        if (StringUtil.isNullOrEmpty(name)) {
            throw new LibraryException(StatusCode.REQUEST_PARAM_ILLEGAL);
        }
        if (checkPdf(file)) {
            String fileName = UUID.randomUUID().toString();
            String filePath = String.format("%s%s.pdf", Constants.SAVE_FILE_PATH, fileName);
            // 保存文件到磁盘
            File dest = new File(filePath);
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = Constants.HOST + fileName + ".pdf";
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
        // 将 book 保存至 Redis，过期时间为 8 小时
        redisTemplate.opsForValue().set(Constants.REDIS_BOOK_PREFIX + book.getId(), JSON.toJSONString(book), 8, TimeUnit.HOURS);
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

    @Override
    public void download(Long id, HttpServletResponse response) {
        Book book = bookRepository.findBookById(id);
        // 判断是否用此书或者此书是否属于当前用户
        if (book == null || !book.getUserId().equals(onlineService.getCurrentUserId())) {
            throw new LibraryException(StatusCode.NOT_FOUND_BOOK);
        }
        try {
            // 获取文件输入流
            InputStream inputStream = new BufferedInputStream(new FileInputStream(book.getFilePath()));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            // 解决文件名乱码
            String fileName = URLEncoder.encode(book.getName(), StandardCharsets.UTF_8.toString());
            String value = String.format("attachment;filename=\"%s\";filename*=utf-8''%s", fileName, fileName);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, value);
            response.getOutputStream().write(data);
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new LibraryException(StatusCode.DOWNLOAD_ERROR);
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

    /**
     * 魔数检验上传的文件是否是 pdf 格式
     *
     * @param file 文件
     * @return 是否合格
     */
    private Boolean checkPdf(MultipartFile file) {
        // 获取文件头
        String fileHead;
        try {
            fileHead = getFileHeader(file);
        } catch (IOException e) {
            throw new LibraryException(StatusCode.FILE_CHECK_ERROR);
        }
        if (fileHead != null && fileHead.length() > 0) {
            fileHead = fileHead.toUpperCase();
            if (fileHead.startsWith(Constants.PDF)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 获取文件头
     *
     * @param file MultipartFile
     * @return String
     * @throws IOException IOException
     */
    private String getFileHeader(MultipartFile file) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = file.getInputStream();
        inputStream.read(b, 0, 28);
        return bytesToHex(b);
    }

    /**
     * 将字节数组转换成 16 进制字符串
     */
    public static String bytesToHex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
