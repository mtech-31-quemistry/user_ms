package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.repository.*;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private StudentClassRepository studentClassRepository;

    @Mock
    private ClassInvitationRepository classInvitationRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private CryptoService cryptoService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenStudent_whenCreateStudentProfile_thenReturnSuccess() {
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
    void givenStudent_whenUpdateStudentProfile_thenReturnSuccess() {
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
                "second");


        when(userRepository.findUserEntityByAccountId(any())).thenReturn(Optional.of(userEntity));
        when(studentRepository.findStudentEntityByUserEntityId(any())).thenReturn(studentEntity);

        StudentResponseDto responseDto = this.studentService.updateStudentProfile(inputStudentProfile);

        assertTrue(responseDto.isSuccess());
    }

//    @Test
//    void givenStudent_whenSendInvitation_thenReturnSuccess() throws Exception {
//        String tutorId = "test";
//
//        var inputStudentProfile = new StudentInvitationDto(
//                "first@first.com",
//                "full name",
//                "c001");
//
//
//        var userEntity = new User(
//                1L,
//                "user-id",
//                "test@test.com",
//                "first",
//                "second"
//        );
//
//        var tutorEntity = new Tutor(2L, "P1", "centre", userEntity, Collections.emptyList());
//
//        var classEntity = new Class(1L, "test", "test2", "test3", "test4", null, Collections.emptyList());
//
//
//        when(tutorRepository.findTutorByUserEntityAccountId(anyString())).thenReturn(Optional.of(tutorEntity));
//        when(classRepository.findByCode(any())).thenReturn(Optional.of(classEntity));
//        when(notificationService.sendEmailNotification(anyString(), anyString(), any())).thenReturn(true);
//        when(cryptoService.encrypt(anyString())).thenReturn("test");
//
//        boolean isSucceed = this.studentService.sendInvitation(inputStudentProfile, tutorId);
//
//        assertTrue(isSucceed);
//    }
}
