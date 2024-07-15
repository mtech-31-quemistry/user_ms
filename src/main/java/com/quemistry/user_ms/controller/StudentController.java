package com.quemistry.user_ms.controller;

import ch.qos.logback.core.util.StringUtil;
import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/student")
public class StudentController extends BaseController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> saveOrUpdateStudentProfile(
            @RequestHeader(value = "User-ID") String userId,
            @Valid @RequestBody StudentDto input) {

        String functionName = "saveOrUpdateStudentProfile";
        String controllerName = "StudentController";
        
        try {

            if (StringUtil.isNullOrEmpty(userId))
                throw new IllegalArgumentException("user id is empty");

            input.setUserId(userId);

            ResponseDto responseDto = prepareResponse(
                    controllerName,
                    functionName,
                    "Your profile has been updated.",
                    this.studentService.updateStudentProfile(input));

            return ResponseEntity.ok(responseDto);

        } catch (Exception ex) {
            return prepareException(controllerName, functionName, ex);
        }
    }
}
