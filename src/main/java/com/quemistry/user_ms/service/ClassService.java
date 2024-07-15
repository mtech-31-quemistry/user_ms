package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.response.ClassResponseDto;

public interface ClassService {

    ClassResponseDto saveClass(ClassDto input);

    ClassResponseDto updateClass(ClassDto input);
}
