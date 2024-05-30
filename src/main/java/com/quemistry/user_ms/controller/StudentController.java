package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.constant.UserConstant;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdateStudentProfile(StudentDto input) {
        var response = new ResponseDto<StudentResponseDto>();
        var studentResponseDto = new StudentResponseDto();

        try {

            boolean isSaved = this.studentService.updateStudentProfile(input);
            studentResponseDto.setSuccess(isSaved);

            response.setStatusCode(UserConstant.STATUS_CODE_SUCCESS);
            response.setStatusMessage("Successfully updated your profile");
            response.setPayload(studentResponseDto);
        } catch (Exception ex) {

        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
