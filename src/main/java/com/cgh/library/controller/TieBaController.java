package com.cgh.library.controller;

import com.alibaba.fastjson.JSONObject;
import com.cgh.library.api.BaseResponse;
import com.cgh.library.api.QRCodeLoginStatusReq;
import com.cgh.library.dto.PageTieBaDTO;
import com.cgh.library.dto.QRCodeDTO;
import com.cgh.library.persistence.entity.TieBaUser;
import com.cgh.library.service.TieBaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author cenganhui
 */
@Slf4j
@RestController
@RequestMapping("tieba")
@RequiredArgsConstructor
@Api(tags = "我的图书：贴吧接口")
public class TieBaController {

    private final TieBaService tieBaService;

    @ApiOperation("获取登录二维码")
    @GetMapping("qrcode")
    public BaseResponse<QRCodeDTO> getQRCode() {
        log.info("获取登录二维码");
        return BaseResponse.success(tieBaService.getQRCode());
    }

    @ApiOperation("扫码登录状态查询并绑定签到")
    @PostMapping("qrcode/status")
    public BaseResponse<JSONObject> getQRCodeLoginStatus(@RequestBody QRCodeLoginStatusReq req) {
        log.info("扫码登录状态查询并绑定签到");
        return BaseResponse.success(tieBaService.getQRCodeLoginStatus(req.getSign(), req.getGid()));
    }

    @ApiOperation("根据昵称查询贴吧用户")
    @GetMapping("users")
    public BaseResponse<Page<TieBaUser>> getAllTieBaUser(@RequestParam(value = "nickName", required = false) String nickName,
                                                         @RequestParam("page") Integer page,
                                                         @RequestParam("size") Integer size) {
        log.info("根据昵称查询贴吧用户");
        return BaseResponse.success(tieBaService.getAllTieBaUser(page, size, nickName));
    }

    @ApiOperation("获取当前用户贴吧信息")
    @GetMapping("info")
    public BaseResponse<PageTieBaDTO> getAllTieBaInfo(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam("page") Integer page,
                                                      @RequestParam("size") Integer size) {
        log.info("获取当前用户贴吧信息");
        return BaseResponse.success(tieBaService.getAllTieBaInfo(page, size, name));
    }

}
