package com.cgh.library.service;

/**
 * @author Akuma
 * @date 2020/9/6 11:15
 */
public interface MyMailService {

    /**
     * 发送 html 邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendHtmlMail(String to, String subject, String content);

}
