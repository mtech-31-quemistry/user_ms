package com.quemistry.user_ms.helper;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
public class EmailHelper {

    public static String readEmailTemplate(String templatePath, String type) throws IOException {
        return readEmailTemplate(templatePath, type, null);
    }

    public static String readEmailTemplate(String templatePath, String type, HashMap<String, String> templateItems) {

        templatePath = String.format("%s/%s", templatePath, type);

        log.info("Reading email template from {}", templatePath);

        InputStream inputStream = EmailHelper.class.getResourceAsStream(templatePath);
        assert inputStream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String contents = reader.lines()
                .collect(Collectors.joining(System.lineSeparator()));

        log.info("Able to read template from the path");

        if (templateItems != null) {
            contents = replaceEmailContent(contents, templateItems);
        }

        return contents;
    }

    private static String replaceEmailContent(String templateContent, HashMap<String, String> templateItems) {
        for (String key : templateItems.keySet()) {
            templateContent = templateContent.replace(key, templateItems.get(key));
        }

        return templateContent;
    }
}
