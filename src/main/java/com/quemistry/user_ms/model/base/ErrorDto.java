package com.quemistry.user_ms.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDto {
    private String code;
    private String displayMessage;
    private String serviceName;
}
