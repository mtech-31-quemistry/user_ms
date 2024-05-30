package com.quemistry.user_ms.controller.base;

import com.quemistry.user_ms.constant.UserConstant;
import com.quemistry.user_ms.model.base.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class BaseController {

    public ResponseEntity<?> prepareException(String controllerName,
                                              String functionName,
                                              Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String errorTrace = sw.toString();

        log.error("Encountered error at controllerName:{}, function:{}, error: {}",
                controllerName,
                functionName,
                errorTrace);

        if (ex instanceof IllegalArgumentException)
            return ResponseEntity.badRequest().body(ex.getMessage());

        if (ex instanceof RuntimeException)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());

        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    public ResponseEntity<?> prepareResponse(String controllerName,
                                                       String functionName,
                                                       ResponseDto<?> responseDto) {
        if (responseDto == null)
            throw new RuntimeException("responseDto is null");

        log.info("controllerName:{}, functionName:{}: Status Message:{}",
                controllerName,
                functionName,
                responseDto.getStatusMessage());

        if (responseDto.getStatusCode().equals(UserConstant.STATUS_CODE_VALIDATION_ERROR))
            return ResponseEntity.badRequest().body(responseDto);

        if (!responseDto.getErrors().isEmpty())
            return ResponseEntity.badRequest().body(responseDto);

        return ResponseEntity.ok(responseDto);
    }
}
