package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.StudentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public boolean updateStudentProfile(StudentDto student) {
        log.info("update student profile started..");
        return true;
    }
}
