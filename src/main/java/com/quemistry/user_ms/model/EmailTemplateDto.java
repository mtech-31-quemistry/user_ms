package com.quemistry.user_ms.model;

import com.quemistry.user_ms.helper.EmailHelper;

import java.io.IOException;
import java.util.HashMap;

public record EmailTemplateDto(String templateCode, HashMap<String, String> templateItems) {

    public String getSubject() throws IOException {
        return EmailHelper.readEmailTemplate(this.templateCode, "subject.txt");
    }

    public String getContent() throws IOException {
        return EmailHelper.readEmailTemplate(this.templateCode, "body.html", this.templateItems);
    }
}
