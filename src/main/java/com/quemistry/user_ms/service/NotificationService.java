package com.quemistry.user_ms.service;

import java.util.HashMap;

public interface NotificationService {

    boolean sendEmailNotification(String recipientEmail, String template, HashMap<String, String> templateItems);
}
