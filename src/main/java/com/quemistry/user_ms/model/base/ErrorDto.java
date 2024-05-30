package com.quemistry.user_ms.model.base;

import lombok.Data;

@Data
public class ErrorDto {
    private String code;
    private String displayMessage;
    private String serviceName;
}
