package com.quemistry.user_ms.controller.base;

import com.quemistry.user_ms.constant.UserConstant;
import com.quemistry.user_ms.model.base.ErrorDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

@Slf4j
public class BaseController {

    public ResponseEntity<ResponseDto> prepareException(String controllerName,
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

        var errors = new ArrayList<ErrorDto>();
        errors.add(new ErrorDto(UserConstant.STATUS_CODE_GENERIC_ERROR, "Error processing the request"));

        var responseDto = new ResponseDto(
                UserConstant.STATUS_CODE_GENERIC_ERROR,
                "Service unavailable, please try again later.",
                functionName,
                errors,
                null
        );

        if (ex instanceof IllegalArgumentException)
            return ResponseEntity.badRequest().body(responseDto);

        if (ex instanceof RuntimeException)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDto);

        return ResponseEntity.internalServerError().body(responseDto);
    }

    public ResponseDto prepareResponse(String controllerName,
                                       String functionName,
                                       String statusMessage,
                                       Object response) {
        if (response == null)
            throw new RuntimeException("responseDto is null");

        var responseDto = new ResponseDto(
                UserConstant.STATUS_CODE_SUCCESS,
                statusMessage,
                functionName,
                new ArrayList<ErrorDto>(),
                response
        );
        responseDto.setPayload(response);

        log.info("controllerName:{}, functionName:{}: Status Message:{}",
                controllerName,
                functionName,
                statusMessage);

        return responseDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        var errors = new ArrayList<ErrorDto>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ErrorDto(fieldName, errorMessage));
        });

        ResponseDto responseDto = new ResponseDto();
        responseDto.getErrors().addAll(errors);
        responseDto.setStatusCode(UserConstant.STATUS_CODE_VALIDATION_ERROR);
        responseDto.setStatusMessage("Fail to validate the request");

        return responseDto;
    }
}
