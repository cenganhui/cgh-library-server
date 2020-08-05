package com.cgh.library.controller;

import com.cgh.library.api.BaseResponse;
import com.cgh.library.persistence.entity.Book;
import com.cgh.library.persistence.entity.User;
import com.cgh.library.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author cenganhui
 */
@Slf4j
@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@Api(tags = "我的图书：管理员接口")
public class AdminController {

    private final AdminService adminService;

    @ApiOperation("根据用户名查询用户")
    @GetMapping("users")
    public BaseResponse<Page<User>> getAllUser(@RequestParam(value = "username", required = false) String username,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("size") Integer size) {
        adminService.checkPermission();
        return BaseResponse.success(adminService.getAllUser(page, size, username));
    }

    @ApiOperation("创建用户")
    @PostMapping("users")
    public BaseResponse<User> createUser(@RequestBody User user) {
//        adminService.checkPermission();
        return BaseResponse.success(adminService.createUser(user));
    }

    @ApiOperation("修改用户")
    @PutMapping("users")
    public BaseResponse<User> updateUser(@Valid @RequestBody User user) {
        adminService.checkPermission();
        return BaseResponse.success(adminService.updateUser(user));
    }

    @ApiOperation("删除用户")
    @DeleteMapping("users/{id}")
    public BaseResponse<String> deleteUser(@PathVariable Long id) {
        adminService.checkPermission();
        adminService.deleteUser(id);
        return BaseResponse.success("用户删除成功");
    }

    @ApiOperation("根据id查询用户")
    @GetMapping("users/{id}")
    public BaseResponse<User> getUserById(@PathVariable Long id) {
        adminService.checkPermission();
        return BaseResponse.success(adminService.getUserById(id));
    }

    @ApiOperation("根据书名查询图书")
    @GetMapping("books")
    public BaseResponse<Page<Book>> getAllBook(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("size") Integer size) {
        adminService.checkPermission();
        return BaseResponse.success(adminService.getAllBook(page, size, name));
    }

    @ApiOperation("删除图书")
    @DeleteMapping("books/{id}")
    public BaseResponse<String> deleteBook(@PathVariable Long id) {
        adminService.checkPermission();
        adminService.deleteBook(id);
        return BaseResponse.success("图书删除成功");
    }

    @ApiOperation("根据id查询图书")
    @GetMapping("books/{id}")
    public BaseResponse<Book> getBookById(@PathVariable Long id) {
        adminService.checkPermission();
        return BaseResponse.success(adminService.getBookById(id));
    }

}
