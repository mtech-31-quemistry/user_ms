package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(studentService))
                .build();
    }

    @Test
    void givenStudents_whenSaveStudentProfile_thenStatus200() throws Exception{
        String expectedStatusCode = "00";
        String expectedStatusMessage = "Successfully updated your profile";
        boolean expectedSuccessFlag = true;

        mockMvc.perform(post("/v1/student/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"firstName\": \"first name\",\n" +
                                "    \"lastName\": \"last name\",\n" +
                                "    \"educationLevel\": \"Sec 2\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedStatusMessage))
                .andExpect(jsonPath("$.payload.success").value(expectedSuccessFlag));

        verify(studentService, times(1)).updateStudentProfile(any());
    }
}
