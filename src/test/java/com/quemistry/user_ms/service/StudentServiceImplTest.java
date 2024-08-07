package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.StudentRepository;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.UserRepository;
import com.quemistry.user_ms.repository.entity.Class;
import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.Tutor;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private NotificationService notificationService;

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

        StudentResponseDto responseDto = this.studentService.updateStudentProfile(inputStudentProfile);

        assertTrue(responseDto.isSuccess());
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
                "second",
                studentEntity,
                null);


        when(userRepository.findUserEntityByAccountId(any())).thenReturn(Optional.of(userEntity));
        when(studentRepository.findStudentEntityByUserEntityId(any())).thenReturn(studentEntity);

        StudentResponseDto responseDto = this.studentService.updateStudentProfile(inputStudentProfile);

        assertTrue(responseDto.isSuccess());
    }

    @Test
    public void givenStudent_whenSendInvitation_thenReturnSuccess() {
        String tutorId = "test";

        var inputStudentProfile = new StudentInvitationDto(
                "first@first.com",
                "full name",
                "c001");


        var userEntity = new User(
                1L,
                "user-id",
                "test@test.com",
                "first",
                "second",
                null,
                null
        );

        var tutorEntity = new Tutor(2L, "P1", "centre", userEntity);

        var classEntity = new Class(1L, "test", "test2", "test3", "test4");


        when(tutorRepository.findTutorByUserEntityAccountId(anyString())).thenReturn(Optional.of(tutorEntity));
        when(classRepository.findClassByCode(any())).thenReturn(Optional.of(classEntity));
        when(notificationService.sendEmailNotification(anyString(), anyString(), any())).thenReturn(true);

        boolean isSucceed = this.studentService.sendInvitation(inputStudentProfile, tutorId);

        assertTrue(isSucceed);
    }
}
