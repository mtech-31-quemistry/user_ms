package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.SaveClassRequest;
import com.quemistry.user_ms.model.response.ClassResponseDto;

import java.util.List;

public interface ClassService {

    ClassResponseDto saveClass(SaveClassRequest request);

    ClassResponseDto updateClass(ClassDto input);

    List<ClassDto> getAllClasses();

    ClassDto getClassWithInvitations(Long classId, String tutorId);
}
