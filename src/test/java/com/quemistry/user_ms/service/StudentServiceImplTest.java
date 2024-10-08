package com.quemistry.user_ms.service;

import com.quemistry.user_ms.constant.ClassInvitationStatus;
import com.quemistry.user_ms.model.StudentDto;
import com.quemistry.user_ms.model.StudentInvitationDto;
import com.quemistry.user_ms.model.StudentProfileRequest;
import com.quemistry.user_ms.repository.ClassInvitationRepository;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.StudentClassRepository;
import com.quemistry.user_ms.repository.StudentRepository;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.UserRepository;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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

    private StudentProfileRequest studentProfileRequest;
    private StudentInvitationDto invitationDto;
    private Student student;
    private Class clazz;
    private List<String> studentEmails;
    private List<String> studentAccountIds;
    private String tutorEmail;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        studentProfileRequest = new StudentProfileRequest();
        studentProfileRequest.setUserId("12345");
        studentProfileRequest.setFirstName("John");
        studentProfileRequest.setLastName("Doe");
        studentProfileRequest.setEmail("john.doe@example.com");
        studentProfileRequest.setEducationLevel("COLLEGE");
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());
        student = new Student(2L, "P1", null, Collections.emptyList());
        clazz = new Class();
        clazz.setId(1L);
        clazz.setDescription("description");
        clazz.setSubject("subject");
        clazz.setStatus("status");
        clazz.setEducationLevel("educationLevel");
        studentEmails = Arrays.asList("student1@example.com", "student2@example.com");
        studentAccountIds = Arrays.asList("account1", "account2");
        tutorEmail = "tutor@example.com";
    }

    @Test
    void givenStudent_whenCreateStudentProfile_thenReturnSuccess() {
        when(userRepository.findUserEntityByAccountId(any())).thenReturn(Optional.empty());

        StudentDto studentDto = this.studentService.updateStudentProfile(studentProfileRequest);

        assertNotNull(studentDto);
    }

    @Test
    void givenStudent_whenUpdateStudentProfile_thenReturnSuccess() {
        var userEntity = new User(
                1L,
                "user-id",
                "test@test.com",
                "first",
                "second");


        when(userRepository.findUserEntityByAccountId(any())).thenReturn(Optional.of(userEntity));
        when(studentRepository.findStudentEntityByUserEntityId(any())).thenReturn(student);

        StudentDto studentDto = this.studentService.updateStudentProfile(studentProfileRequest);

        assertNotNull(studentDto);
    }

    @Test
    void testUpdateStudentProfile_UserDoesNotExist() {
        // Mock the repository call to return an empty Optional for user
        when(userRepository.findUserEntityByAccountId(studentProfileRequest.getUserId())).thenReturn(Optional.empty());

        // Call the method
        StudentDto studentDto = studentService.updateStudentProfile(studentProfileRequest);

        // Verify that the new User and Student were created
        verify(userRepository).save(any(User.class));
        verify(studentRepository).save(any(Student.class));

        // Check response
        assertNotNull(studentDto);
    }

    @Test
    void givenStudent_whenSendInvitation_thenReturnSuccess() throws Exception {
        String tutorId = "test";

        var inputStudentProfile = new StudentInvitationDto(
                "first@first.com",
                "full name",
                1L);


        var userEntity = new User(
                1L,
                "user-id",
                "test@test.com",
                "first",
                "second"
        );

        var tutorEntity = new Tutor(2L, "P1", "centre", userEntity, Collections.emptyList());

        when(tutorRepository.findTutorByUserEntityAccountId(anyString())).thenReturn(Optional.of(tutorEntity));
        when(classRepository.findById(any())).thenReturn(Optional.of(clazz));
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
        Long classId = 1L;
        StudentInvitationDto input = new StudentInvitationDto("John Doe", "student@example.com", classId);

        Tutor tutor = new Tutor();
        when(tutorRepository.findTutorByUserEntityAccountId(tutorAccountId)).thenReturn(Optional.of(tutor));
        when(classRepository.findById(classId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.sendInvitation(input, tutorAccountId);
        });

        assertEquals("404 NOT_FOUND \"class id=1 not found\"", exception.getMessage());
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

    @Test
    void testSearchStudentProfile_Success() {
        // Mock student data
        List<Student> students = Arrays.asList(new Student("student1@example.com", "account1"), new Student("student2@example.com", "account2"));

        // Mock the repository call
        when(studentRepository.findStudentByEmailOrAccountId(anyList(), anyList(), anyString()))
                .thenReturn(students);

        // Test the method
        List<StudentDto> studentDtos = studentService.searchStudentProfile(studentEmails, studentAccountIds, tutorEmail);

        // Assert the results
        assertNotNull(studentDtos);
        assertEquals(2, studentDtos.size());
    }

    @Test
    void testSearchStudentProfile_EmptyResult() {
        // Mock the repository call to return an empty list
        when(studentRepository.findStudentByEmailOrAccountId(anyList(), anyList(), anyString()))
                .thenReturn(Arrays.asList());

        // Test the method and expect a ResponseStatusException
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.searchStudentProfile(studentEmails, studentAccountIds, tutorEmail);
        });

        // Assert the exception message and status
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("student not found"));
    }

}
