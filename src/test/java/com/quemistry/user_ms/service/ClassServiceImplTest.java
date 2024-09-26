package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.SaveClassRequest;
import com.quemistry.user_ms.model.response.ClassResponseDto;
import com.quemistry.user_ms.repository.ClassInvitationRepository;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.StudentRepository;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.entity.Class;
import com.quemistry.user_ms.repository.entity.Student;
import com.quemistry.user_ms.repository.entity.Tutor;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.impl.ClassServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClassServiceImplTest {

    @InjectMocks
    ClassServiceImpl classService;

    @Mock
    ClassRepository classRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    ClassInvitationRepository classInvitationRepository;

    @Mock
    TutorRepository tutorRepository;

    private Class clazz;
    private Student student;
    private ClassDto classDto;
    private SaveClassRequest saveClassRequest;

    @BeforeEach
    void setUp() {
//        classDto = new ClassDto(1L, "test", "test", "test sub", "test", "tst", null, Collections.emptyList());
        classDto = new ClassDto();
        classDto.setId(1L);
        saveClassRequest = new SaveClassRequest();
        MockitoAnnotations.openMocks(this);
        clazz = new Class();
        clazz.setId(1l);
        clazz.setCode("code");
        clazz.setDescription("description");
        clazz.setSubject("subject");
        clazz.setStatus("status");
        clazz.setEducationLevel("educationLevel");
        when(classRepository.save(any(Class.class))).thenReturn(clazz);
        student = new Student();
        student.setId(2L);
        List<Class> classes = new ArrayList<>();
        classes.add(clazz);
        List<Student> students = new ArrayList<>();
        students.add(student);
        student.setClasses(classes);
        clazz.setStudents(students);
    }

    @Test
    void testSaveClassSuccess() {
        // Given
        SaveClassRequest request = new SaveClassRequest();
        request.setUserId("testUserId");
        request.setCode("classId");
        request.setDescription("classDescription");
        request.setEducationLevel("level");
        request.setSubject("subject");

        Tutor tutor = new Tutor();
        tutor.setUserEntity(new User());

        when(tutorRepository.findTutorByUserEntityAccountId("testUserId")).thenReturn(Optional.of(tutor));
        when(classRepository.save(any(Class.class))).thenReturn(new Class());

        // When
        ClassResponseDto response = classService.saveClass(request);

        // Then
        assertTrue(response.isSuccess());
        verify(tutorRepository).findTutorByUserEntityAccountId("testUserId");
        verify(classRepository).save(any(Class.class));
    }

    @Test
    void givenClass_AbleToUpdateClass() {
        when(classRepository.findById(anyLong())).thenReturn(Optional.of(clazz));
        Assertions.assertNotNull(classService.updateClass(classDto));
    }

    @Test
    void testUpdateClass_ClassNotFound() {
        // Mock the repository to return an empty Optional
        when(classRepository.findById(classDto.getId())).thenReturn(Optional.empty());

        // Expect a ResponseStatusException with a NOT_FOUND status
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            classService.updateClass(classDto);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("class with id=1 not found", exception.getReason());
    }

    @Test
    void removeStudentFromClass_shouldReturnClassDto() {
        // Arrange
        when(classRepository.findByClassIdAndTutorAccountId(1L, "tutor123")).thenReturn(Optional.of(clazz));
        when(studentRepository.save(student)).thenReturn(student);
        when(classRepository.save(clazz)).thenReturn(clazz);

        // Act
        ClassDto result = classService.removeStudentFromClass(1L, 2L, "tutor123");

        // Assert
        verify(classRepository).findByClassIdAndTutorAccountId(1L, "tutor123");
        verify(studentRepository).save(student);
        verify(classRepository).save(clazz);
    }

    @Test
    public void removeStudentFromClass_shouldThrowNotFound_whenClassNotFound() {
        String tutorAccountId = "tutor1";
        Long classId = 1L;
        Long studentId = 1L;

        when(classRepository.findByClassIdAndTutorAccountId(classId, tutorAccountId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> classService.removeStudentFromClass(classId, studentId, tutorAccountId)
                ,"class not found for classId=1 and tutorAccountId=tutor1");
    }

    @Test
    public void removeStudentFromClass_shouldThrowNotFound_whenStudentNotFound() {
        String tutorAccountId = "tutor1";
        Long classId = 1L;
        Long studentId = 999L; // Invalid student ID

        when(classRepository.findByClassIdAndTutorAccountId(classId, tutorAccountId)).thenReturn(Optional.of(clazz));

        assertThrows(ResponseStatusException.class, () -> classService.removeStudentFromClass(classId, studentId, tutorAccountId)
                ,"student not found for studentId=999 and classId=1");
    }



}