package com.cgh.library.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cenganhui
 */
@RestController
@RequestMapping
@Api(tags = "Hello接口")
public class ApplicationController {

    @ApiOperation("测试")
    @GetMapping("home")
    public String index() {
        return "Hello~";
    }

}
