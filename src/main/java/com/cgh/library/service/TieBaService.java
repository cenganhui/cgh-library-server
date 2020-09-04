package com.cgh.library.service;

import com.alibaba.fastjson.JSONObject;
import com.cgh.library.dto.PageTieBaDTO;
import com.cgh.library.dto.QRCodeDTO;
import com.cgh.library.persistence.entity.TieBaUser;
import org.springframework.data.domain.Page;

/**
 * @author cenganhui
 */
public interface TieBaService {

    /**
     * 获取登录二维码
     *
     * @return 二维码
     */
    QRCodeDTO getQRCode();

    /**
     * 扫码登录状态查询并绑定签到
     *
     * @param sign sign
     * @param gid  gid
     * @return 登录信息
     */
    JSONObject getQRCodeLoginStatus(String sign, String gid);

    /**
     * 根据昵称查询贴吧用户
     *
     * @param page     页码
     * @param size     大小
     * @param nickName 昵称
     * @return 贴吧用户列表
     */
    Page<TieBaUser> getAllTieBaUser(Integer page, Integer size, String nickName);

    /**
     * 获取当前用户贴吧信息
     *
     * @param page 页码
     * @param size 大小
     * @param name 贴吧名称
     * @return 贴吧列表
     */
    PageTieBaDTO getAllTieBaInfo(Integer page, Integer size, String name);

    /**
     * 执行签到
     *
     * @param tieBaUser 贴吧用户
     * @return 签到后贴吧用户
     */
    TieBaUser doSign(TieBaUser tieBaUser);

}
