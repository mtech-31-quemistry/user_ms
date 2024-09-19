package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.StudentProfileRequest;
import jakarta.validation.constraints.NotBlank;


public interface StudentService {

    StudentDto updateStudentProfile(StudentProfileRequest studentProfileRequest);

    boolean sendInvitation(StudentInvitationDto input, String tutorAccountId) throws Exception;

    boolean acceptInvitation(String studentEmail, String accountId, String code) throws Exception;

    StudentDto getStudentProfile(@NotBlank String studentEmail);

    StudentDto searchStudentProfile(String studentEmail, String teacherEmail);
}
