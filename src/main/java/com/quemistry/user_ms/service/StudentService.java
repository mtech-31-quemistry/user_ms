package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;


public interface StudentService {

    StudentResponseDto updateStudentProfile(StudentDto student);

}
