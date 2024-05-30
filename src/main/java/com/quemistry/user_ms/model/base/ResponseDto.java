package com.quemistry.user_ms.model.base;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseDto<T> {
    private String statusCode;
    private String statusMessage;
    private List<ErrorDto> errors = new ArrayList<>();
    private T payload;
}

