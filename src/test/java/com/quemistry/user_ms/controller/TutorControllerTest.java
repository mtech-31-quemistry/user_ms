package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.model.TutorDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.service.TutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_EMAIL;
import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TutorController.class)
class TutorControllerTest {


    @MockBean
    private TutorService tutorService;

    @InjectMocks
    private TutorController tutorController;

    @Autowired
    private MockMvc mockMvc;

    private TutorDto tutorDto;
    private static final String email = "test@example.com";
    private static final String accountId = "12345";

    @BeforeEach
    void setUp() {
        tutorDto = new TutorDto();
        tutorDto.setFirstName("Ray");
        tutorDto.setLastName("Htet");
        tutorDto.setEmail(email);
        tutorDto.setAccountId(accountId);
    }

    @Test
    void testGetProfile() throws Exception {
        // Mock the service
        ResponseDto mockResponse = new ResponseDto();
        mockResponse.setStatusMessage("Profile retrieved");
        when(tutorService.getProfile(email)).thenReturn(tutorDto);

        mockMvc.perform(get("/v1/users/tutor/profile")
                        .header(HEADER_KEY_USER_ID, accountId)
                        .header(HEADER_KEY_USER_EMAIL, email))
                .andExpect(status().isOk());
        verify(tutorService, times(1)).getProfile(email);
    }

    @Test
    void testSaveProfile() throws Exception {
        ResponseDto mockResponse = new ResponseDto();
        mockResponse.setStatusMessage("Profile updated");

        when(tutorService.saveProfile(any(TutorDto.class))).thenReturn(tutorDto);
        String tutorDtoJson = new ObjectMapper().writeValueAsString(tutorDto);
        mockMvc.perform(post("/v1/users/tutor/profile")
                        .header(HEADER_KEY_USER_ID, accountId)
                        .header(HEADER_KEY_USER_EMAIL, email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tutorDtoJson))
                .andExpect(status().isOk());
        verify(tutorService, times(1)).saveProfile(any(TutorDto.class));
    }
}
