package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.ClassInvitationDto;
import com.quemistry.user_ms.repository.ClassInvitationRepository;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.entity.Class;
import com.quemistry.user_ms.repository.entity.ClassInvitation;
import com.quemistry.user_ms.repository.entity.Tutor;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.impl.ClassServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ClassServiceImplTest {

    @InjectMocks
    ClassServiceImpl classService;

    @Mock
    ClassRepository classRepository;

    @Mock
    ClassInvitationRepository classInvitationRepository;

    ClassDto classDto;

    @BeforeEach
    void setUp() {
//        classDto = new ClassDto(1L, "test", "test", "test sub", "test", "tst", null, Collections.emptyList());
        classDto = new ClassDto();
        classDto.setId(1L);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenClass_AbleToSaveClass() {
        this.classService.saveClass(classDto);
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

    @Test
    void givenValidClassId_whenGetClassWithInvitations_thenReturnClassDto() {
        Long classId = 1L;

        // Mock class entity and its related data
        User tutorUser = new User();
        String tutorEmail = "tutor@example.com";
        tutorUser.setEmail(tutorEmail);

        Tutor tutor = new Tutor();
        tutor.setUserEntity(tutorUser);

        Class clazz = new Class();
        clazz.setTutors(Arrays.asList(tutor));
        List<ClassInvitation> classInvitations = Arrays.asList(new ClassInvitation());

        ClassDto expectedClassDto = new ClassDto();
        ClassInvitationDto classInvitationDto = new ClassInvitationDto();
        List<ClassInvitationDto> expectedClassInvitationDtos = Arrays.asList(classInvitationDto);
        expectedClassDto.setClassInvitations(expectedClassInvitationDtos);
        expectedClassDto.setTutorEmails(Collections.singletonList(tutorEmail));

        when(classRepository.findById(classId)).thenReturn(Optional.of(clazz));
//        when(classMapper.classToClassDto(clazz)).thenReturn(mockClassDto);
//        when(ClassInvitationMapper.INSTANCE.classInvitationsToClassInvitationDto(classInvitations))
//                .thenReturn(mockClassInvitationDtos);
        when(classInvitationRepository.findByClassId(classId)).thenReturn(classInvitations);

        // Call the service method
        ClassDto result = classService.getClassWithInvitations(classId);

        // Verify the results
        assertEquals(expectedClassDto, result);
        assertEquals(expectedClassInvitationDtos, result.getClassInvitations());
        assertEquals(Arrays.asList(tutorEmail), result.getTutorEmails());

        verify(classRepository).findById(classId);
        verify(classInvitationRepository).findByClassId(classId);
    }
}
