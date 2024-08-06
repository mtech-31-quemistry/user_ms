package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.model.EmailTemplateDto;
import com.quemistry.user_ms.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

import static com.quemistry.user_ms.constant.EmailConstant.STUDENT_INVITATION_TEMPLATE;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSenderImpl mailSender;

    public NotificationServiceImpl(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @return flag if notification is sent successfully or failed
     */
    @Override
    public boolean sendEmailNotification(
            String recipientEmail,
            String template,
            HashMap<String, String> templateItems) {

        log.info("Send notification started");

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            var emailTemplateDto = new EmailTemplateDto(STUDENT_INVITATION_TEMPLATE, templateItems);

            helper.setText(emailTemplateDto.getContent(), true);
            helper.setTo(recipientEmail);
            helper.setFrom(new InternetAddress("no-reply@quemistry.com", "Quemistry"));
            helper.setSubject(emailTemplateDto.getSubject());

            mailSender.send(message);

            log.info("Send notification ended");
            return true;

        } catch (MessagingException | IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
