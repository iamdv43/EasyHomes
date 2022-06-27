package com.group24.easyHomes.service;

import com.group24.easyHomes.mappers.EmailConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;


@Service
@AllArgsConstructor
public class SendMailService{

    private final static Logger LOGGER = LoggerFactory.getLogger(SendMailService.class);

    private final JavaMailSender javaMailSender;
    private final EmailConfig emailConfig;

    @Autowired
    private final Environment env;

    @Async
    public void send(String emailID, String content, String subject) {
        try{
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(emailConfig.getHost());
            mailSender.setPort(emailConfig.getPort());
            mailSender.setUsername(emailConfig.getSenderEmail());
            mailSender.setPassword(emailConfig.getSenderpassword());

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setTo(emailID);
//            mimeMessageHelper.setSubject(Objects.requireNonNull(env.getProperty("email.subject")));
            mimeMessageHelper.setSubject(subject);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            LOGGER.error(env.getProperty("send.error"),e);
            throw new IllegalStateException(env.getProperty("send.error"));
        }
    }
}
