package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;


public interface StudentService {

    ResponseDto<StudentResponseDto> updateStudentProfile(StudentDto student);

}
