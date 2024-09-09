package com.quemistry.user_ms.service;

import com.quemistry.user_ms.constant.ClassInvitationStatus;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.repository.*;
import com.quemistry.user_ms.repository.entity.Class;
import com.quemistry.user_ms.repository.entity.ClassInvitation;
import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.StudentClass;
import com.quemistry.user_ms.repository.entity.Tutor;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

    private StudentDto studentDto;
    private StudentInvitationDto invitationDto;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        studentDto = new StudentDto();
        studentDto.setUserId("12345");
        studentDto.setFirstName("John");
        studentDto.setLastName("Doe");
        studentDto.setEmail("john.doe@example.com");
        studentDto.setEducationLevel("COLLEGE");
    }

    @Test
    void givenStudent_whenCreateStudentProfile_thenReturnSuccess() {
        when(userRepository.findUserEntityByAccountId(any())).thenReturn(Optional.empty());

        StudentResponseDto responseDto = this.studentService.updateStudentProfile(studentDto);

        assertTrue(responseDto.isSuccess());
    }

    @Test
    void givenStudent_whenUpdateStudentProfile_thenReturnSuccess() {
        var studentEntity = new Student(2L, "P1", null);

        var userEntity = new User(
                1L,
                "user-id",
                "test@test.com",
                "first",
                "second");


        when(userRepository.findUserEntityByAccountId(any())).thenReturn(Optional.of(userEntity));
        when(studentRepository.findStudentEntityByUserEntityId(any())).thenReturn(studentEntity);

        StudentResponseDto responseDto = this.studentService.updateStudentProfile(studentDto);

        assertTrue(responseDto.isSuccess());
    }

    @Test
    void testUpdateStudentProfile_UserDoesNotExist() {
        // Mock the repository call to return an empty Optional for user
        when(userRepository.findUserEntityByAccountId(studentDto.getUserId())).thenReturn(Optional.empty());

        // Call the method
        StudentResponseDto response = studentService.updateStudentProfile(studentDto);

        // Verify that the new User and Student were created
        verify(userRepository).save(any(User.class));
        verify(studentRepository).save(any(Student.class));

        // Check response
        assertTrue(response.isSuccess());
    }

    @Test
    void givenStudent_whenSendInvitation_thenReturnSuccess() throws Exception {
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
                "second"
        );

        var tutorEntity = new Tutor(2L, "P1", "centre", userEntity, Collections.emptyList());
        var classEntity = new Class(1L, "test", "test2", "test3", "test4", null, Collections.emptyList(), Collections.emptyList());

        when(tutorRepository.findTutorByUserEntityAccountId(anyString())).thenReturn(Optional.of(tutorEntity));
        when(classRepository.findByCode(any())).thenReturn(Optional.of(classEntity));
        when(notificationService.sendEmailNotification(anyString(), anyString(), any())).thenReturn(true);
        when(cryptoService.encrypt(anyString())).thenReturn("test");

        boolean isSucceed = this.studentService.sendInvitation(inputStudentProfile, tutorId);

        assertTrue(isSucceed);
    }

    @Test
    void testSendInvitation_TutorNotFound() {
        // Mock the repository call to return an empty Optional for tutor
        when(tutorRepository.findTutorByUserEntityAccountId("tutor123")).thenReturn(Optional.empty());

        // Call the method and expect a ResponseStatusException
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.sendInvitation(invitationDto, "tutor123");
        });

        assertEquals("tutor id=tutor123 not found", exception.getReason());
    }

    @Test
    void testSendInvitation_ClassNotFound() {
        // Given
        String tutorAccountId = "tutor123";
        String classCode = "class456";
        StudentInvitationDto input = new StudentInvitationDto("John Doe", "student@example.com", classCode);

        Tutor tutor = new Tutor();
        when(tutorRepository.findTutorByUserEntityAccountId(tutorAccountId)).thenReturn(Optional.of(tutor));
        when(classRepository.findByCode(classCode)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.sendInvitation(input, tutorAccountId);
        });

        assertEquals("404 NOT_FOUND \"class code=class456 not found\"", exception.getMessage());
        verify(classInvitationRepository, never()).save(any(ClassInvitation.class));
    }

    @Test
    void acceptInvitation_validInvitation_success() throws Exception {
        String studentEmail = "student@example.com";
        String accountId = "accountId";
        String code = "encodedCode";
        String decryptedInvitationCode = "invitationCode";

        ClassInvitation invitation = new ClassInvitation();
        invitation.setUserEmail(studentEmail);
        invitation.setStatus(ClassInvitationStatus.INVITED);
        invitation.setClassEntity(new Class());

        when(cryptoService.decrypt(anyString())).thenReturn(decryptedInvitationCode);
        when(classInvitationRepository.findByCode(anyString())).thenReturn(Optional.of(invitation));
        when(userRepository.findUserEntityByAccountId(anyString())).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

        boolean result = studentService.acceptInvitation(studentEmail, accountId, code);

        assertTrue(result);
        verify(classInvitationRepository).save(invitation);
        assertEquals(ClassInvitationStatus.ACCEPTED, invitation.getStatus());
    }

    @Test
    void acceptInvitation_mismatchedEmail_throwsForbidden() throws Exception {
        String studentEmail = "student@example.com";
        String accountId = "accountId";
        String code = "encodedCode";
        String decryptedInvitationCode = "invitationCode";

        ClassInvitation invitation = new ClassInvitation();
        invitation.setUserEmail("other@example.com");

        when(cryptoService.decrypt(anyString())).thenReturn(decryptedInvitationCode);
        when(classInvitationRepository.findByCode(anyString())).thenReturn(Optional.of(invitation));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                studentService.acceptInvitation(studentEmail, accountId, code)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verify(classInvitationRepository, never()).save(any());
    }

    @Test
    void testAcceptInvitation_ClassInvitationNotFound() throws Exception {
        String studentEmail = "student@example.com";
        String accountId = "12345";
        String encryptedCode = "encryptedCode";
        String invitationCode = "validCode";

        when(cryptoService.decrypt(anyString())).thenReturn(invitationCode);
        when(classInvitationRepository.findByCode(invitationCode)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            studentService.acceptInvitation(studentEmail, accountId, encryptedCode);
        });

        verify(classInvitationRepository, never()).save(any(ClassInvitation.class));
    }

    @Test
    void testAcceptInvitation_UserNotFound() throws Exception {
        String studentEmail = "student@example.com";
        String accountId = "12345";
        String encryptedCode = "encryptedCode";
        String invitationCode = "validCode";

        ClassInvitation classInvitation = new ClassInvitation();
        classInvitation.setUserEmail(studentEmail);
        classInvitation.setStatus(ClassInvitationStatus.INVITED);

        when(cryptoService.decrypt(anyString())).thenReturn(invitationCode);
        when(classInvitationRepository.findByCode(invitationCode)).thenReturn(Optional.of(classInvitation));
        when(userRepository.findUserEntityByAccountId(accountId)).thenReturn(Optional.empty());

        // Act
        boolean result = studentService.acceptInvitation(studentEmail, accountId, encryptedCode);

        // Assert
        assertTrue(result);  // Assuming the new user will be created in this case
        verify(studentRepository).save(any(Student.class));
        verify(studentClassRepository).save(any(StudentClass.class));
    }

}
