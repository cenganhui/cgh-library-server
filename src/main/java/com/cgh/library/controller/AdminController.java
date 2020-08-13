package com.cgh.library.controller;

import com.cgh.library.api.AdminReq;
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
        log.info("根据用户名查询用户");
        adminService.checkPermission();
        return BaseResponse.success(adminService.getAllUser(page, size, username));
    }

    @ApiOperation("创建用户")
    @PostMapping("users")
    public BaseResponse<User> createUser(@Valid @RequestBody User user) {
        log.info("创建用户");
        adminService.checkPermission();
        return BaseResponse.success(adminService.createUser(user));
    }

    @ApiOperation("修改用户")
    @PutMapping("users")
    public BaseResponse<User> updateUser(@RequestBody User user) {
        log.info("修改用户");
        adminService.checkPermission();
        return BaseResponse.success(adminService.updateUser(user));
    }

    @ApiOperation("修改用户管理员权限")
    @PutMapping("permission")
    public BaseResponse<String> updateAdmin(@Valid @RequestBody AdminReq req) {
        log.info("修改用户管理员权限");
        if (Boolean.TRUE.equals(adminService.updateAdmin(req.getId(), req.getAdmin()))) {
            return BaseResponse.success("修改成功");
        }
        return BaseResponse.success("修改失败");
    }

    @ApiOperation("删除用户")
    @DeleteMapping("users/{id}")
    public BaseResponse<String> deleteUser(@PathVariable Long id) {
        log.info("删除用户");
        adminService.checkPermission();
        adminService.deleteUser(id);
        return BaseResponse.success("用户删除成功");
    }

    @ApiOperation("根据id查询用户")
    @GetMapping("users/{id}")
    public BaseResponse<User> getUserById(@PathVariable Long id) {
        log.info("根据id查询用户");
        adminService.checkPermission();
        return BaseResponse.success(adminService.getUserById(id));
    }

    @ApiOperation("根据书名查询图书")
    @GetMapping("books")
    public BaseResponse<Page<Book>> getAllBook(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("size") Integer size) {
        log.info("根据书名查询图书");
        adminService.checkPermission();
        return BaseResponse.success(adminService.getAllBook(page, size, name));
    }

    @ApiOperation("删除图书")
    @DeleteMapping("books/{id}")
    public BaseResponse<String> deleteBook(@PathVariable Long id) {
        log.info("删除图书");
        adminService.checkPermission();
        adminService.deleteBook(id);
        return BaseResponse.success("图书删除成功");
    }

    @ApiOperation("根据id查询图书")
    @GetMapping("books/{id}")
    public BaseResponse<Book> getBookById(@PathVariable Long id) {
        log.info("根据id查询图书");
        adminService.checkPermission();
        return BaseResponse.success(adminService.getBookById(id));
    }

}
