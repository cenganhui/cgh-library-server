package com.cgh.library.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cgh.library.Constants;
import com.cgh.library.api.StatusCode;
import com.cgh.library.dto.PageTieBaDTO;
import com.cgh.library.dto.QRCodeDTO;
import com.cgh.library.dto.TieBaDTO;
import com.cgh.library.exception.LibraryException;
import com.cgh.library.persistence.entity.TieBaUser;
import com.cgh.library.persistence.entity.User;
import com.cgh.library.persistence.repository.TieBaUserRepository;
import com.cgh.library.persistence.repository.UserRepository;
import com.cgh.library.service.MyMailService;
import com.cgh.library.service.OnlineService;
import com.cgh.library.service.TieBaService;
import com.github.libsgh.tieba.api.TieBaApi;
import com.github.libsgh.tieba.model.MyTB;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cenganhui
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TieBaServiceImpl implements TieBaService {

    private final TieBaUserRepository tieBaUserRepository;

    private final UserRepository userRepository;

    private final OnlineService onlineService;

    private final MyMailService myMailService;

    private final TieBaApi api = TieBaApi.getInstance();

    @Override
    public QRCodeDTO getQRCode() {
        Map<String, Object> map = api.getQRCodeUrl();
        QRCodeDTO qrCodeDTO = new QRCodeDTO();
        qrCodeDTO.setCodeUrl(map.get("codeUrl").toString());
        qrCodeDTO.setGid(map.get("gid").toString());
        qrCodeDTO.setSign(map.get("sign").toString());
        qrCodeDTO.setTime(map.get("time").toString());
        return qrCodeDTO;
    }

    @Override
    public JSONObject getQRCodeLoginStatus(String sign, String gid) {
        JSONObject jsonObject = api.qrCodeLoginStatus(sign, gid);
        String v = jsonObject.getJSONObject("channel_v").get("v").toString();
        getCookieFromQRCode(v);
        return jsonObject;
    }

    @Override
    public Page<TieBaUser> getAllTieBaUser(Integer page, Integer size, String nickName) {
        if (nickName == null) {
            nickName = "";
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        return tieBaUserRepository.findAllByNickNameContaining(nickName, pageRequest);
    }

    @Override
    public PageTieBaDTO getAllTieBaInfo(Integer page, Integer size, String name) {
        // 获取当前用户的贴吧用户
        TieBaUser tieBaUser = tieBaUserRepository.findTieBaUserByUserId(onlineService.getCurrentUserId());
        // 检验登录状态
        Boolean login = api.islogin(tieBaUser.getBduss(), tieBaUser.getStoken());
        if (Boolean.FALSE.equals(login)) {
            throw new LibraryException(StatusCode.TIEBA_NO_LOGIN);
        }
        // 组装贴吧信息列表
        PageTieBaDTO pageTieBaDTO = new PageTieBaDTO();
        // 获取关注的所有贴吧
        List<MyTB> myTBList = api.getMyLikedTB(tieBaUser.getBduss(), tieBaUser.getStoken());
        if (StringUtil.isNullOrEmpty(name)) {
            name = "";
        }
        // 查询名称
        String queryName = name;
        // 根据查询名称进行过滤
        myTBList = myTBList
                .stream()
                .filter(tb -> tb.getTbName().contains(queryName))
                .collect(Collectors.toList());
        // 设置总个数
        pageTieBaDTO.setTotal(myTBList.size());
        // 根据分页进行过滤
        myTBList = myTBList
                .stream()
                .skip((page * size))
                .limit(size)
                .collect(Collectors.toList());
        // list MyTB -> TieBaDTO
        List<TieBaDTO> list = new ArrayList<>();
        for (MyTB myTB : myTBList) {
            TieBaDTO tieBaDTO = new TieBaDTO();
            tieBaDTO.setId(myTB.getFid());
            tieBaDTO.setName(myTB.getTbName());
            tieBaDTO.setUrl(myTB.getUrl());
            tieBaDTO.setCurExp(myTB.getEx());
            tieBaDTO.setLevelId(myTB.getLv());
            tieBaDTO.setLevelName(myTB.getLvName());
            tieBaDTO.setSignTime(myTB.getSignTime());
            tieBaDTO.setSignCount(myTB.getCountSignNum());
            tieBaDTO.setSignExp(myTB.getExp());
            tieBaDTO.setErrorMsg(myTB.getError_msg());
            list.add(tieBaDTO);
        }
        pageTieBaDTO.setTieBaDTOList(list);
        return pageTieBaDTO;
    }

    /**
     * 扫描二维码登录获取 bduss、stoken
     *
     * @param v v
     */
    private void getCookieFromQRCode(String v) {
        Map<String, Object> map = api.getCookieFromQRCode(v);
        String bduss = map.get("bduss").toString();
        String stoken = map.get("stoken").toString();
        saveTieBaUser(bduss, stoken);
    }

    /**
     * 保存贴吧用户
     *
     * @param bduss  bduss
     * @param stoken stoken
     * @return 贴吧用户
     */
    private TieBaUser saveTieBaUser(String bduss, String stoken) {
        // 获取贴吧用户信息
        Map<String, Object> userInfo = api.getUserInfo(bduss, stoken);
        String username = userInfo.get("user_name_url").toString();
        Long openUid = Long.parseLong(userInfo.get("open_uid").toString());
        // 查看是否存在贴吧用户，若不存在则创建
        TieBaUser dbTieBaUser = tieBaUserRepository.findTieBaUserByOpenUid(openUid);
        if (dbTieBaUser == null) {
            dbTieBaUser = new TieBaUser();
        }
        // 获取当前用户
        Long currentUserId = onlineService.getCurrentUserId();
        User dbUser = userRepository.findUserById(currentUserId);
        dbTieBaUser.setUsername(username);
        dbTieBaUser.setNickName(userInfo.get("user_name_show").toString());
        dbTieBaUser.setBduss(bduss);
        dbTieBaUser.setStoken(stoken);
        dbTieBaUser.setOpenUid(openUid);
        dbTieBaUser.setAvatar(api.getHeadImg(username));
        dbTieBaUser.setUserId(currentUserId);
        dbTieBaUser.setEmail(dbUser.getEmail());
        // 执行签到
        TieBaUser tieBaUser = doSign(dbTieBaUser);
        // 关联用户-贴吧用户并保存
        TieBaUser save = tieBaUserRepository.save(tieBaUser);
        dbUser.setTiebaId(save.getId());
        userRepository.save(dbUser);
        return save;
    }

    @Override
    @Async("asyncServiceExecutor")
    public TieBaUser doSign(TieBaUser tieBaUser) {
        // 检验贴吧登录状态
        Boolean login = api.islogin(tieBaUser.getBduss(), tieBaUser.getStoken());
        // 未登录
        if (Boolean.FALSE.equals(login)) {
            tieBaUser.setLogin(false);
            tieBaUser.setCost("0s");
            tieBaUser.setSignedTb(0);
            tieBaUser.setErrorTb(tieBaUser.getTotalTb());
            tieBaUser.setSignTime(LocalDateTime.now());
            tieBaUser.setSignStatus(false);
            return tieBaUser;
        }
        // 已登录
        Long startTime = System.currentTimeMillis();
        // 执行签到并获取签到结果 map
        Map<String, Object> map = api.oneBtnToSign(tieBaUser.getBduss(), tieBaUser.getStoken());
        tieBaUser.setTotalTb(Integer.parseInt(map.get("用户贴吧数").toString()));
        Long endTime = System.currentTimeMillis();
        // 设置相关信息
        tieBaUser.setLogin(true);
        tieBaUser.setCost((endTime - startTime) / 1000 + "s");
        tieBaUser.setErrorTb(Integer.parseInt(map.get("签到失败").toString()));
        tieBaUser.setSignedTb(tieBaUser.getTotalTb() - tieBaUser.getErrorTb());
        tieBaUser.setSignTime(LocalDateTime.now());
        tieBaUser.setSignStatus(true);
        if (!StringUtil.isNullOrEmpty(tieBaUser.getEmail())) {
            // 发送邮件
            myMailService.sendHtmlMail(tieBaUser.getEmail(), Constants.SEND_EMAIL_SUBJECT, concatContent(tieBaUser));
        }
        return tieBaUser;
    }

    private String concatContent(TieBaUser tieBaUser) {
        String startH3 = "<h3>";
        String endH3 = "</h3>";
        return startH3 +
                "贴吧总数：" +
                tieBaUser.getTotalTb() +
                endH3 +
                startH3 +
                "已签到数：" +
                tieBaUser.getSignedTb() +
                endH3 +
                startH3 +
                "签到失败：" +
                tieBaUser.getErrorTb() +
                endH3 +
                startH3 +
                "签到时间：" +
                tieBaUser.getSignTime() +
                endH3 +
                startH3 +
                "耗时：" +
                tieBaUser.getCost() +
                endH3;
    }

}
