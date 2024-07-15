package com.quemistry.user_ms.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private String statusCode;
    private String statusMessage;
    private String serviceName;
    private List<ErrorDto> errors = new ArrayList<>();
    private Object payload;
}

