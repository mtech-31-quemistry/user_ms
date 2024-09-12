package com.quemistry.user_ms.service;

import com.quemistry.user_ms.model.TutorDto;
import com.quemistry.user_ms.repository.TutorRepository;
import com.quemistry.user_ms.repository.UserRepository;
import com.quemistry.user_ms.repository.entity.Tutor;
import com.quemistry.user_ms.repository.entity.User;
import com.quemistry.user_ms.service.impl.TutorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class TutorServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TutorRepository tutorRepository;

    @InjectMocks
    private TutorServiceImpl tutorService;

    private TutorDto tutorDto;
    private User user;
    private Tutor tutor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    // Setup common test data
    private void setupTestData() {
        tutorDto = new TutorDto();
        tutorDto.setAccountId("123");
        tutorDto.setEmail("tutor@example.com");
        tutorDto.setFirstName("John");
        tutorDto.setLastName("Doe");
        tutorDto.setEducationLevel("PhD");
        tutorDto.setTuitionCentre("Top School");

        user = new User();
        user.setAccountId(tutorDto.getAccountId());
        user.setEmail(tutorDto.getEmail());
        user.setFirstName(tutorDto.getFirstName());
        user.setLastName(tutorDto.getLastName());
        user.setCreationAndModifiedDetails(OffsetDateTime.now(), tutorDto.getAccountId());

        tutor = new Tutor();
        tutor.setUserEntity(user);
        tutor.setEducationLevel(tutorDto.getEducationLevel());
        tutor.setTuitionCentre(tutorDto.getTuitionCentre());
        tutor.setCreationAndModifiedDetails(OffsetDateTime.now(), tutorDto.getAccountId());
    }

    @Test
    void saveProfile_success() {
        // When
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);
        when(tutorRepository.saveAndFlush(any(Tutor.class))).thenReturn(tutor);

        // Execute
        TutorDto result = tutorService.saveProfile(tutorDto);

        // Then
        assertTutorDto(result, tutorDto);

        // Verify
        verifyRepositoriesSaveCalls();
    }

    @Test
    void testSaveProfile_ExistingTutor() {
        // Setup: Mock finding an existing tutor
        when(tutorRepository.findTutorByUserEntityEmail(tutorDto.getEmail())).thenReturn(Optional.of(tutor));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);
        when(tutorRepository.saveAndFlush(any(Tutor.class))).thenReturn(tutor);

        // Execute: Call saveProfile with the tutorDto
        TutorDto result = tutorService.saveProfile(tutorDto);

        // Verify: Ensure the repositories were called
        verify(tutorRepository).findTutorByUserEntityEmail(tutorDto.getEmail());
        verify(userRepository).saveAndFlush(any(User.class));
        verify(tutorRepository).saveAndFlush(any(Tutor.class));

        // Assert: Check that the returned TutorDto is correct
        assertNotNull(result);
        assertEquals(tutorDto.getEmail(), result.getEmail());
    }

    @Test
    void getProfile_success() {
        // Given
        String email = "tutor@example.com";

        // When
        when(tutorRepository.findTutorByUserEntityEmail(email)).thenReturn(Optional.of(tutor));

        // Execute
        TutorDto result = tutorService.getProfile(email);

        // Then
        assertTutorDto(result, tutorDto);

        // Verify
        verify(tutorRepository, times(1)).findTutorByUserEntityEmail(email);
    }

    @Test
    void getProfile_notFound() {
        // Given
        String email = "notfound@example.com";

        // When
        when(tutorRepository.findTutorByUserEntityEmail(email)).thenReturn(Optional.empty());

        // Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tutorService.getProfile(email);
        });
        assertEquals("404 NOT_FOUND \"tutor not found for email=notfound@example.com\"", exception.getMessage());

        // Verify
        verify(tutorRepository, times(1)).findTutorByUserEntityEmail(email);
    }

    // Helper method to reduce duplication when asserting TutorDto
    private void assertTutorDto(TutorDto result, TutorDto expected) {
        assertNotNull(result);
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getFirstName(), result.getFirstName());
        assertEquals(expected.getEducationLevel(), result.getEducationLevel());
        assertEquals(expected.getTuitionCentre(), result.getTuitionCentre());
    }

    // Helper method to verify repository save calls
    private void verifyRepositoriesSaveCalls() {
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        verify(tutorRepository, times(1)).saveAndFlush(any(Tutor.class));
    }
}
