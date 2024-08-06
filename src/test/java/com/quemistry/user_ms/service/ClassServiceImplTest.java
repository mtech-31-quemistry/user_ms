package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.repository.ClassRepository;
import com.quemistry.user_ms.repository.entity.Class;
import com.quemistry.user_ms.service.impl.ClassServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ClassServiceImplTest {

    @InjectMocks
    ClassServiceImpl classService;

    @Mock
    ClassRepository classRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenClass_AbleToSaveClass() {
        var newClass = new ClassDto(1L, "test", "test", "test sub", "test", "tst");

        this.classService.saveClass(newClass);
    }

    @Test
    public void givenClass_AbleToUpdateClass() {
        var newClass = new ClassDto(1L, "test", "test", "test sub", "test", "tst");

        var existingClass = new Class(1L, "test", "test", "test sub", "test");

        when(classRepository.findById(anyLong())).thenReturn(Optional.of(existingClass));
        Assertions.assertNotNull(this.classService.updateClass(newClass));
    }

    @Test
    public void givenClassNotFound_ReturnNull() {
        var newClass = new ClassDto(1L, "test", "test", "test sub", "test", "tst");

        Assertions.assertNull(this.classService.updateClass(newClass));
    }
}
