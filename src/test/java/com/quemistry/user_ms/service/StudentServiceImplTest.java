package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.repository.StudentRepository;
import com.quemistry.user_ms.repository.UserRepository;

import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenStudent_whenCreateStudentProfile_thenReturnSuccess() {
        var inputStudentProfile = new StudentDto(
                "first",
                "second",
                "test@test.com",
                "user-id",
                "Sec2");


        when(userRepository.findUserEntityByAccountId(any())).thenReturn(Optional.empty());

        ResponseDto<StudentResponseDto> responseDto = this.studentService.updateStudentProfile(inputStudentProfile);

        assertTrue(responseDto.getPayload().isSuccess());
    }

    @Test
    public void givenStudent_whenUpdateStudentProfile_thenReturnSuccess() {
        var inputStudentProfile = new StudentDto(
                "first",
                "second",
                "test@test.com",
                "user-id",
                "Sec2");

        var studentEntity = new Student(2L, "P1", null);

        var userEntity = new User(
                1L,
                "user-id",
                "test@test.com",
                "first",
                "second", studentEntity);


        when(userRepository.findUserEntityByAccountId(any())).thenReturn(Optional.of(userEntity));
        when(studentRepository.findStudentEntityByUserEntityId(any())).thenReturn(studentEntity);

        ResponseDto<StudentResponseDto> responseDto = this.studentService.updateStudentProfile(inputStudentProfile);

        assertTrue(responseDto.getPayload().isSuccess());
    }
}
