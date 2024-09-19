package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.SaveClassRequest;
import com.quemistry.user_ms.model.response.ClassResponseDto;
import com.quemistry.user_ms.repository.ClassInvitationRepository;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.entity.Class;
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
    ClassInvitationRepository classInvitationRepository;

    @Mock
    TutorRepository tutorRepository;

    private Class clazz;

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
    }

//    @Test
//    void givenClass_AbleToSaveClass() {
//        this.classService.saveClass(saveClassRequest);
//    }

    @Test
    void testSaveClassSuccess() {
        // Given
        SaveClassRequest request = new SaveClassRequest();
        request.setUserId("testUserId");
        request.setCode("classCode");
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
        Assertions.assertNotNull(this.classService.updateClass(classDto));
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


}
