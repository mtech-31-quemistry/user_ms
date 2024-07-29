package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.response.ClassResponseDto;

import java.util.List;

public interface ClassService {

    ClassResponseDto saveClass(ClassDto input);

    ClassResponseDto updateClass(ClassDto input);

    List<ClassDto> getAllClasses();
}
