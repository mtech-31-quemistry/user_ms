package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.response.ClassResponseDto;
import com.quemistry.user_ms.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassController.class)
@Import(ControllerAdvice.class)
class ClassControllerTest {

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

    @Test
    void givenValidClassId_whenGetClassAndInvitations_thenReturnsResponseDto() throws Exception {
        Long classId = 1L;
        String userId = "user123";
        when(classService.getClassWithInvitations(classId, userId)).thenReturn(new ClassDto());

        // Perform the GET request and verify the response
        mockMvc.perform(get("/v1/class/{classId}", classId)
                        .header("x-user-id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMessage").value("The request has been completed."));
        // Add more expectations based on the properties of your ResponseDto
    }


    @Test
    void givenInvalidClassId_whenGetClassAndInvitations_thenReturnsException() throws Exception {
        Long classId = 1L;
        String userId = "user123";

        // Mock the service to throw an exception
        when(classService.getClassWithInvitations(classId, userId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        // Perform the GET request and verify the exception handling
        mockMvc.perform(get("/v1/class/{classId}", classId)
                        .header("x-user-id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClassController_whenGetAllClasses_thenReturnStatus200() throws Exception {
        List<ClassDto> classes = new ArrayList<>();
        classes.add(new ClassDto());

        // Mock the service to throw an exception
        when(classService.getAllClasses(anyString())).thenReturn(classes);

        // Perform the GET request and verify the exception handling
        mockMvc.perform(get("/v1/class")
                        .header("x-user-id", "userId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private ClassResponseDto setClassResponseDtoMock(boolean flag) {
        return new ClassResponseDto(flag);
    }
}
