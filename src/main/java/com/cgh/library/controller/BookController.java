package com.cgh.library.controller;

import com.cgh.library.api.BaseResponse;
import com.cgh.library.persistence.entity.Book;
import com.cgh.library.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author cenganhui
 */
@Slf4j
@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Api(tags = "我的图书：图书接口")
public class BookController {

    private final BookService bookService;

    @ApiOperation("根据书名查询图书")
    @GetMapping
    public BaseResponse<Page<Book>> getAllBook(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("size") Integer size) {
        log.info("根据书名查询图书");
        return BaseResponse.success(bookService.getAllBook(page, size, name));
    }

    @ApiOperation("删除图书")
    @DeleteMapping("{id}")
    public BaseResponse<String> delete(@PathVariable Long id) {
        log.info("删除图书");
        bookService.deleteBook(id);
        return BaseResponse.success("图书删除成功");
    }

    @ApiOperation("根据id查询图书")
    @GetMapping("{id}")
    public BaseResponse<Book> getBookById(@PathVariable Long id) {
        log.info("根据id查询图书");
        return BaseResponse.success(bookService.getBookById(id));
    }

    @ApiOperation("上传图书pdf")
    @PostMapping("upload")
    public BaseResponse<Book> upload(@RequestParam("file") MultipartFile file) {
        log.info("上传图书pdf");
        return BaseResponse.success(bookService.upload(file));
    }

    @ApiOperation("更新图书")
    @PostMapping("update")
    public BaseResponse<Book> update(@RequestBody Book book) {
        log.info("更新图书");
        return BaseResponse.success(bookService.update(book));
    }

}
