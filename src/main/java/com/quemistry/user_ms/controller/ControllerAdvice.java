package com.quemistry.user_ms.controller;

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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericExceptions(Exception ex, WebRequest request) {

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        responseDto.setStatusMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<Object> handleResponseStatusExceptions(ResponseStatusException ex, WebRequest request) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(String.valueOf(ex.getStatusCode().value()));
        responseDto.setStatusMessage(ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(responseDto);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        ResponseDto baseResponseDto = new ResponseDto();
//
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String errorMessage = error.getDefaultMessage();
//            baseResponseDto.setStatusMessage(errorMessage);
//        });
//
//        baseResponseDto.setStatusCode(String.valueOf(ex.getStatusCode().value()));
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDto);
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("bad request, {}", ex.getMessage());
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

        return ResponseEntity.badRequest().body(responseDto);
    }
}
