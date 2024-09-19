package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.constant.UserConstant;
import com.quemistry.user_ms.model.base.ErrorDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class ControllerAdviceTest {

    private ControllerAdvice controllerAdvice;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controllerAdvice = new ControllerAdvice();
    }

    @Test
    void testHandleResponseStatusExceptions() {
        // Create a ResponseStatusException for testing
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");

        // Call the method to be tested
        ResponseEntity<Object> responseEntity = controllerAdvice.handleResponseStatusExceptions(ex, webRequest);

        // Assertions to verify the response
        ResponseDto responseDto = (ResponseDto) responseEntity.getBody();
        assert responseDto != null;
        Assertions.assertEquals(String.valueOf(ex.getStatusCode().value()), responseDto.getStatusCode());
        Assertions.assertEquals(ex.getReason(), responseDto.getStatusMessage());
        Assertions.assertEquals(ex.getStatusCode(), responseEntity.getStatusCode());
    }

//    @Test
//    void testHandleValidationExceptions() {
//        // Create a MethodArgumentNotValidException for testing
//        BindingResult bindingResult = Mockito.mock(BindingResult.class);
//        // Mock behavior of ex.getBindingResult().getAllErrors() to return validation errors
//        Mockito.when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(
//                new FieldError("objectName", "field", "Field is required")
//        ));
//        MethodArgumentNotValidException ex = new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
//
//        // Call the method to be tested
//        ResponseEntity<Object> responseEntity = controllerAdvice.handleValidationExceptions(ex);
//
//        // Assertions to verify the response
//        ResponseDto responseDto = (ResponseDto) responseEntity.getBody();
//        assert responseDto != null;
//        Assertions.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), responseDto.getStatusCode());
//        Assertions.assertEquals("Field is required", responseDto.getStatusMessage());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//    }
//
//    @Test
//    void testHandleValidationExceptions2() {
//        // Create a mock BindingResult
//        BindingResult bindingResult = Mockito.mock(BindingResult.class);
//
//        // Mock behavior to return multiple validation errors
//        List<FieldError> fieldErrors = Arrays.asList(
//                new FieldError("objectName", "field1", "Field1 is required"),
//                new FieldError("objectName", "field2", "Field2 must not be empty")
//        );
//        Mockito.when(bindingResult.getAllErrors()).thenReturn(fieldErrors);
//
//        // Create a MethodArgumentNotValidException with the mock BindingResult
//        MethodArgumentNotValidException ex = new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
//
//        // Call the method to be tested
//        ResponseEntity<Object> responseEntity = controllerAdvice.handleValidationExceptions(ex);
//
//        // Verify that the response body is not null
//        assertNotNull(responseEntity.getBody(), "Response body should not be null");
//
//        // Cast the response body to ResponseDto and verify it's not null
//        ResponseDto responseDto = (ResponseDto) responseEntity.getBody();
//        assertNotNull(responseDto, "ResponseDto should not be null");
//
//        // Verify that the responseDto contains the correct number of errors
//        Assertions.assertEquals(2, responseDto.getErrors().size(), "There should be 2 errors");
//
//        // Check the content of the first error
//        ErrorDto firstError = responseDto.getErrors().get(0);
//        Assertions.assertEquals("field1", firstError.getCode(), "First field name should be 'field1'");
//        Assertions.assertEquals("Field1 is required", firstError.getDisplayMessage(), "First error message should be 'Field1 is required'");
//
//        // Check the content of the second error
//        ErrorDto secondError = responseDto.getErrors().get(1);
//        Assertions.assertEquals("field2", secondError.getCode(), "Second field name should be 'field2'");
//        Assertions.assertEquals("Field2 must not be empty", secondError.getDisplayMessage(), "Second error message should be 'Field2 must not be empty'");
//
//        // Verify the status code and status message in the responseDto
//        Assertions.assertEquals(UserConstant.STATUS_CODE_VALIDATION_ERROR, responseDto.getStatusCode(), "Status code should be validation error code");
//        Assertions.assertEquals("Fail to validate the request", responseDto.getStatusMessage(), "Status message should be 'Fail to validate the request'");
//
//        // Check the HTTP status in the response entity
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "HTTP status should be 400 Bad Request");
//    }


    @Test
    void testHandleValidationExceptions() throws NoSuchMethodException {
        // Create a mock BindingResult
        BindingResult bindingResult = Mockito.mock(BindingResult.class);

        // Mock behavior to return multiple validation errors
        List<ObjectError> fieldErrors = Arrays.asList(
                new FieldError("objectName", "field1", "Field1 is required"),
                new FieldError("objectName", "field2", "Field2 must not be empty")
        );
        Mockito.when(bindingResult.getAllErrors()).thenReturn(fieldErrors);

        // Use a real method to create a MethodParameter
        Method method = this.getClass().getMethod("dummyMethodForTesting", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);  // 0 indicates the first parameter

        // Create a MethodArgumentNotValidException with the mock MethodParameter and BindingResult
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        // Call the method to be tested
        ResponseEntity<Object> responseEntity = controllerAdvice.handleValidationExceptions(ex);

        // Verify that the response body is not null
        assertNotNull(responseEntity.getBody(), "Response body should not be null");

        // Cast the response body to ResponseDto and verify it's not null
        ResponseDto responseDto = (ResponseDto) responseEntity.getBody();
        assertNotNull(responseDto, "ResponseDto should not be null");

        // Verify that the responseDto contains the correct number of errors
        Assertions.assertEquals(2, responseDto.getErrors().size(), "There should be 2 errors");

        // Check the content of the first error
        ErrorDto firstError = responseDto.getErrors().get(0);
        Assertions.assertEquals("field1", firstError.getCode(), "First field name should be 'field1'");
        Assertions.assertEquals("Field1 is required", firstError.getDisplayMessage(), "First error message should be 'Field1 is required'");

        // Check the content of the second error
        ErrorDto secondError = responseDto.getErrors().get(1);
        Assertions.assertEquals("field2", secondError.getCode(), "Second field name should be 'field2'");
        Assertions.assertEquals("Field2 must not be empty", secondError.getDisplayMessage(), "Second error message should be 'Field2 must not be empty'");

        // Verify the status code and status message in the responseDto
        Assertions.assertEquals(UserConstant.STATUS_CODE_VALIDATION_ERROR, responseDto.getStatusCode(), "Status code should be validation error code");
        Assertions.assertEquals("Fail to validate the request", responseDto.getStatusMessage(), "Status message should be 'Fail to validate the request'");

        // Check the HTTP status in the response entity
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "HTTP status should be 400 Bad Request");
    }


    // Dummy method to be used for creating MethodParameter
    public void dummyMethodForTesting(String input) {
    }
}
