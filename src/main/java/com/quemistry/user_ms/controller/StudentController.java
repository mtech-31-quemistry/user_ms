package com.quemistry.user_ms.controller;

import ch.qos.logback.core.util.StringUtil;
import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.model.AcceptInvitationDto;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_EMAIL;
import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_ID;

@Slf4j
@RestController
@RequestMapping("/v1/student")
public class StudentController extends BaseController {

    private final StudentService studentService;
    private final String controllerName = "StudentController";

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/profile")
    public ResponseEntity<ResponseDto> saveOrUpdateStudentProfile(
            @RequestHeader(value = HEADER_KEY_USER_ID) String userId,
            @Valid @RequestBody StudentDto input) {

        String functionName = "saveOrUpdateStudentProfile";

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

    @PostMapping("/send-invitation")
    public ResponseEntity<ResponseDto> sendStudentInvitation(
            @NotBlank @RequestHeader(value = HEADER_KEY_USER_ID) String tutorId,
            @Valid @RequestBody StudentInvitationDto input) {

        String functionName = "sendStudentInvitation";

        try {

            if (StringUtil.isNullOrEmpty(tutorId))
                throw new IllegalArgumentException("tutor id is empty");

            ResponseDto responseDto = prepareResponse(
                    controllerName,
                    functionName,
                    "Successfully invited the student.",
                    this.studentService.sendInvitation(input, tutorId));

            return ResponseEntity.ok(responseDto);

        } catch (Exception ex) {
            return prepareException(controllerName, functionName, ex);
        }
    }

    @PostMapping("/accept-invitation")
    public ResponseEntity<ResponseDto> acceptStudentInvitation(
            @NotBlank @RequestHeader(value = HEADER_KEY_USER_EMAIL) String studentEmail,
            @NotBlank @RequestHeader(value = HEADER_KEY_USER_ID) String studentAccountId,
            @Valid @RequestBody AcceptInvitationDto acceptInvitationDto) {

        String functionName = "acceptStudentInvitation";

        try {

            if (StringUtil.isNullOrEmpty(studentEmail))
                throw new IllegalArgumentException("student email is empty");

            ResponseDto responseDto = prepareResponse(
                    controllerName,
                    functionName,
                    "Successfully invited the student.",
                    this.studentService.acceptInvitation(studentEmail, studentAccountId, acceptInvitationDto.invitationCode()));

            return ResponseEntity.ok(responseDto);

        } catch (Exception ex) {
            return prepareException(controllerName, functionName, ex);
        }
    }
}
