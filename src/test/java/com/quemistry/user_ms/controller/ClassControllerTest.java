package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.model.response.ClassResponseDto;
import com.quemistry.user_ms.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassController.class)
public class ClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClassService classService;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ClassController(classService))
                .setControllerAdvice(new BaseController())
                .build();
    }

    @DisplayName("Should return status 200 when try to save class")
    @Test
    void givenClassController_whenSaveClass_thenStatus200() throws Exception {
        String expectedStatusCode = "00";
        boolean expectedSuccessFlag = true;

        when(classService.saveClass(any())).thenReturn(setClassResponseDtoMock(true));

        mockMvc.perform(post("/v1/class")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-ID", HEADER_KEY_USER_ID)
                        .content("""
                                {
                                    "code": "C001",
                                    "description": "description",
                                    "subject": "class subject",
                                    "educationLevel": "level 1"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.payload.success").value(expectedSuccessFlag));

        verify(classService, times(1)).saveClass(any());
    }

    @DisplayName("Should return status 200 when try to update class")
    @Test
    void givenClassController_whenUpdateClass_thenStatus200() throws Exception {
        String expectedStatusCode = "00";
        boolean expectedSuccessFlag = true;

        when(classService.updateClass(any())).thenReturn(setClassResponseDtoMock(true));

        mockMvc.perform(put("/v1/class")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-ID", HEADER_KEY_USER_ID)
                        .content("""
                                {
                                     "id": 2,
                                     "code": "C001",
                                     "description": "Chemistry 101",
                                     "subject": "class subject",
                                     "educationLevel": "level 3"
                                 }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.payload.success").value(expectedSuccessFlag));

        verify(classService, times(1)).updateClass(any());
    }

    private ClassResponseDto setClassResponseDtoMock(boolean flag) {
        return new ClassResponseDto(flag);
    }
}
