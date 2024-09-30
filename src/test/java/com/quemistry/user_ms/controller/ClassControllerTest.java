package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.RemoveStudentRequest;
import com.quemistry.user_ms.model.response.ClassResponseDto;
import com.quemistry.user_ms.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClassController.class)
@Import(ControllerAdvice.class)
class ClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClassService classService;

    @Mock
    private ClassDto classDto;

    private ObjectMapper objectMapper;
    private String tutorAccountId;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ClassController(classService))
                .setControllerAdvice(new BaseController())
                .build();
        objectMapper = new ObjectMapper();
        tutorAccountId = "tutor-account-id";
    }

    @DisplayName("Should return status 200 when try to save class")
    @Test
    void givenClassController_whenSaveClass_thenStatus200() throws Exception {
        String expectedStatusCode = "00";
        boolean expectedSuccessFlag = true;

        when(classService.saveClass(any())).thenReturn(setClassResponseDtoMock(true));

        mockMvc.perform(post("/v1/class")
                        .contentType(APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, tutorAccountId)
                        .content("""
                                {
                                    "code": "C001",
                                    "description": "description",
                                    "subject": "class subject",
                                    "educationLevel": "level 1"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.payload.success").value(expectedSuccessFlag));

        verify(classService, times(1)).saveClass(any());
    }

    @DisplayName("Should return status 200 when try to update class")
    @Test
    void givenClassController_whenUpdateClass_thenStatus200() throws Exception {
        String expectedStatusCode = "00";
        boolean expectedSuccessFlag = true;

        when(classService.updateClass(any())).thenReturn(classDto);

        mockMvc.perform(put("/v1/class")
                        .contentType(APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, tutorAccountId)
                        .content("""
                                {
                                     "id": 2,
                                     "code": "C001",
                                     "description": "Chemistry 101",
                                     "subject": "class subject",
                                     "educationLevel": "level 3"
                                 }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode));

        verify(classService, times(1)).updateClass(any());
    }

    @Test
    void givenValidClassId_whenGetClassAndInvitations_thenReturnsResponseDto() throws Exception {
        Long classId = 1L;
        when(classService.getClassWithInvitations(classId, tutorAccountId)).thenReturn(new ClassDto());

        // Perform the GET request and verify the response
        mockMvc.perform(get("/v1/class/{classId}", classId)
                        .header(HEADER_KEY_USER_ID, tutorAccountId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMessage").value("The request has been completed."));
        // Add more expectations based on the properties of your ResponseDto
    }


    @Test
    void givenInvalidClassId_whenGetClassAndInvitations_thenReturnsException() throws Exception {
        Long classId = 1L;

        // Mock the service to throw an exception
        when(classService.getClassWithInvitations(classId, tutorAccountId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        // Perform the GET request and verify the exception handling
        mockMvc.perform(get("/v1/class/{classId}", classId)
                        .header(HEADER_KEY_USER_ID, tutorAccountId)
                        .contentType(APPLICATION_JSON))
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
                        .header(HEADER_KEY_USER_ID, tutorAccountId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void removeStudentClass_shouldReturnOkResponse() throws Exception {
        // Arrange
        Long classId = 1L;
        Long studentId = 2L;

        when(classService.removeStudentFromClass(classId, studentId, tutorAccountId)).thenReturn(classDto);

        // Act & Assert
        mockMvc.perform(delete("/v1/class/{classId}/student/{studentId}", classId, studentId)
                        .header(HEADER_KEY_USER_ID, tutorAccountId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void removeStudents_ShouldReturnOk_WhenSuccessful() throws Exception {
        // Arrange
        RemoveStudentRequest removeStudentRequest = new RemoveStudentRequest();// Populate with necessary fields
        // Mock the service method to return expected results
        when(classService.removeStudents(eq(tutorAccountId), any(RemoveStudentRequest.class))).thenReturn(classDto);

        // Act & Assert
        mockMvc.perform(post("/v1/class/remove-student")
                        .header(HEADER_KEY_USER_ID, tutorAccountId) // Replace with actual header name
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeStudentRequest))) // Replace with actual JSON
                .andExpect(status().isOk()); // Adjust the expected response as needed

        // Verify that the service method was called
        verify(classService, times(1)).removeStudents(eq(tutorAccountId), any(RemoveStudentRequest.class));
    }

    private ClassResponseDto setClassResponseDtoMock(boolean flag) {
        return new ClassResponseDto(flag);
    }
}
