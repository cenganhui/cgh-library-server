package com.cgh.library.util;

import com.alibaba.fastjson.JSONObject;
import com.cgh.library.Constants;
import com.cgh.library.api.StatusCode;
import com.cgh.library.exception.LibraryException;
import com.cgh.library.persistence.entity.Book;
import com.cgh.library.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 定时任务，每隔 4h 将 Redis 的图书信息更新到数据库中
 *
 * @author cenganhui
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Constants.DATE_FORMAT);

    private final BookRepository bookRepository;

    private final StringRedisTemplate redisTemplate;

    /**
     * 每隔 4h 执行一次
     */
    @Scheduled(fixedRate = 4 * 60 * 60 * 1000)
    public void persistToDatabase() {
        log.info("定时任务：持久化 Redis 图书数据到数据库，开始时间：{}", DATE_FORMAT.format(new Date()));
        // 获取所有图书 key 集合
        Set<String> keys = redisTemplate.keys(Constants.REDIS_BOOK_PREFIX + "*");
        // 若集合不为空，则遍历集合
        if (keys != null) {
            List<Book> bookList = new ArrayList<>();
            for (String key : keys) {
                Book book = JSONObject.parseObject(redisTemplate.opsForValue().get(key), Book.class);
                if (book == null) {
                    throw new LibraryException(StatusCode.NOT_FOUND_BOOK);
                }
                // 查询数据库是否有此书，有则加入更新列表
                Book dbBook = bookRepository.findBookById(book.getId());
                if (dbBook != null) {
                    BeanUtil.copyPropertiesIgnoreNull(book, dbBook);
                    dbBook.setUpdatedBy("System auto update");
                    dbBook.setUpdateTime(LocalDateTime.now());
                    bookList.add(dbBook);
                }
            }
            bookRepository.saveAll(bookList);
        }
        log.info("定时任务：持久化 Redis 图书数据到数据库，结束时间：{}", DATE_FORMAT.format(new Date()));
    }

}
