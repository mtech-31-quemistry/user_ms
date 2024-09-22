package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.StudentProfileRequest;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface StudentService {

    StudentDto updateStudentProfile(StudentProfileRequest studentProfileRequest);

    boolean sendInvitation(StudentInvitationDto input, String tutorAccountId) throws Exception;

    boolean acceptInvitation(String studentEmail, String accountId, String code) throws Exception;

    StudentDto getStudentProfile(@NotBlank String studentEmail);

    List<StudentDto> searchStudentProfile(List<String> studentEmails, List<String> studentAccountIds, String tutorEmail);
}
