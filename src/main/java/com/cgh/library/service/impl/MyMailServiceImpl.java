package com.cgh.library.service.impl;

import com.cgh.library.service.MyMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Akuma
 * @date 2020/9/6 11:16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MyMailServiceImpl implements MyMailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private final String from;

    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        // 获取 MimeMessage 对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            // 设置发送人
            messageHelper.setFrom(from);
            // 设置收件人
            messageHelper.setTo(to);
            // 设置邮件主题
            message.setSubject(subject);
            // 设置内容和 html 格式
            messageHelper.setText(content, true);
            // 发送
            mailSender.send(message);
            log.info("已发送邮件至 {}", to);
        } catch (MessagingException e) {
            log.info("发送邮件至 {} 异常", to);
        }
    }

}
