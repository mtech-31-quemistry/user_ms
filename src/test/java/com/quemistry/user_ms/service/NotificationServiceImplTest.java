package com.quemistry.user_ms.service;

import com.quemistry.user_ms.constant.EmailConstant;
import com.quemistry.user_ms.service.impl.NotificationServiceImpl;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashMap;

import static org.mockito.Mockito.when;

public class NotificationServiceImplTest {

    @Mock
    final JavaMailSenderImpl sender = new JavaMailSenderImpl();

    @InjectMocks
    NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(sender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void givenWhenSendEmail_AbleToReceiveNotification() {
        String recipientEmail = "tst@tst.com";
        var emailItems = new HashMap<String, String>();


        boolean canSend = this.notificationService.sendEmailNotification(
                recipientEmail,
                EmailConstant.STUDENT_INVITATION_TEMPLATE,
                emailItems);

        Assertions.assertTrue(canSend);
    }
}
