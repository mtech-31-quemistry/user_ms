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

import java.util.Collections;
import java.util.Optional;

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

    ClassDto classDto;

    SaveClassRequest saveClassRequest;

    @BeforeEach
    void setUp() {
//        classDto = new ClassDto(1L, "test", "test", "test sub", "test", "tst", null, Collections.emptyList());
        classDto = new ClassDto();
        classDto.setId(1L);
        saveClassRequest = new SaveClassRequest();
        MockitoAnnotations.openMocks(this);
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
        var existingClass = new Class(1L, "test", "test", "test sub", "test", null, Collections.emptyList());

        when(classRepository.findById(anyLong())).thenReturn(Optional.of(existingClass));
        Assertions.assertNotNull(this.classService.updateClass(classDto));
    }

    @Test
    void givenClassNotFound_ReturnNull() {
        Assertions.assertNull(this.classService.updateClass(classDto));
    }


}
