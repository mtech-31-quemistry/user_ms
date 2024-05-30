package com.quemistry.user_ms.controller;

import ch.qos.logback.core.util.StringUtil;
import com.quemistry.user_ms.constant.UserConstant;
import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/student")
public class StudentController extends BaseController {

    private final StudentService studentService;

    private final String controllerName = "StudentController";

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdateStudentProfile(
            @RequestHeader(value = "User-ID") String userId,
            @RequestBody StudentDto input) {

        String functionName = "saveOrUpdateStudentProfile";

        try {

            if (StringUtil.isNullOrEmpty(userId))
                throw new IllegalArgumentException("user id is empty");

            if (input == null)
                throw new IllegalArgumentException("input is null");

            input.setUserId(userId);

            return prepareResponse(controllerName,
                    functionName,
                    this.studentService.updateStudentProfile(input));

        } catch (Exception ex) {
            return prepareException(controllerName, functionName, ex);
        }
    }
}
