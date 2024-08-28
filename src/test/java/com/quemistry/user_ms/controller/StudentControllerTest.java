package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.model.response.StudentResponseDto;
import com.quemistry.user_ms.service.StudentService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)

public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;


    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new StudentController(studentService))
                //.setControllerAdvice(new BaseController())
                .build();
    }

    @DisplayName("Should return status 200 when can save student profile")
    @Test
    void givenStudents_whenSaveStudentProfile_thenStatus200() throws Exception {
        String expectedStatusCode = "00";
        String expectedStatusMessage = "Your profile has been updated.";
        boolean expectedSuccessFlag = true;

        when(studentService.updateStudentProfile(any())).thenReturn(setStudentResponseMock(true));

        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, HEADER_KEY_USER_ID)
                        .content("""
                                {
                                     "firstName": "Test",
                                     "lastName": "Test",
                                     "email": "test@test.com",
                                     "educationLevel": "Sec 2"
                                 }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedStatusMessage))
                .andExpect(jsonPath("$.payload.success").value(expectedSuccessFlag));

        verify(studentService, times(1)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 400 when User ID is empty in header")
    @Test
    void givenStudents_whenSaveStudentProfileWithoutUserID_thenStatus400() throws Exception {

        when(studentService.updateStudentProfile(any())).thenReturn(setStudentResponseMock(true));

        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, "")
                        .content("""
                                {
                                     "firstName": "Test",
                                     "lastName": "Test",
                                     "email": "test@test.com",
                                     "educationLevel": "Sec 2"
                                 }"""))
                .andExpect(status().isBadRequest());

        verify(studentService, times(0)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 400 when validation fails in body")
    @Test
    void givenStudents_whenSaveStudentProfileWithAnyInvalidBodyProperty_thenStatus400() throws Exception {

        when(studentService.updateStudentProfile(any())).thenReturn(setStudentResponseMock(true));

        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, "")
                        .content("""
                                {
                                     "firstName": "",
                                     "lastName": "Test",
                                     "email": "test@test.com",
                                     "educationLevel": "Sec 2"
                                 }"""))
                .andExpect(status().isBadRequest());

        verify(studentService, times(0)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 400 when request is empty in body")
    @Test
    void givenStudents_whenSaveStudentProfileWithoutBody_thenStatus400() throws Exception {

        when(studentService.updateStudentProfile(any())).thenReturn(setStudentResponseMock(false));

        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, "1234")
                        .content(new byte[0]))
                .andExpect(status().isBadRequest());

        verify(studentService, times(0)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 503 when service throw runtime exception")
    @Test
    void givenStudents_whenSaveStudentProfileAndThrowRuntimeException_thenStatus503() throws Exception {

        when(studentService.updateStudentProfile(any())).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, 12314)
                        .content("""
                                {
                                     "firstName": "Test",
                                     "lastName": "Test",
                                     "email": "test@test.com",
                                     "educationLevel": "Sec 2"
                                 }"""))
                .andExpect(status().isServiceUnavailable());

        verify(studentService, times(1)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 500 when service throw any other exception")
    @Test
    void givenStudents_whenSaveStudentProfileAndThrowGeneralException_thenStatus503() throws Exception {

        when(studentService.updateStudentProfile(any())).thenAnswer(invocation -> {
            throw new Exception("general exception");
        });


        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, 12314)
                        .content("""
                                {
                                     "firstName": "Test",
                                     "lastName": "Test",
                                     "email": "test@test.com",
                                     "educationLevel": "Sec 2"
                                 }"""))
                .andExpect(status().isInternalServerError());

        verify(studentService, times(1)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 503 when service return empty or null")
    @Test
    void givenStudents_whenSaveStudentProfile_ReturnNullOrEmpty_thenStatus503() throws Exception {

        when(studentService.updateStudentProfile(any())).thenReturn(null);


        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, 12314)
                        .content("""
                                {
                                     "firstName": "Test",
                                     "lastName": "Test",
                                     "email": "test@test.com",
                                     "educationLevel": "Sec 2"
                                 }"""))
                .andExpect(status().isServiceUnavailable());

        verify(studentService, times(1)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 400 when return status code is 01")
    @Test
    void givenStudents_whenSaveStudentProfile_ReturnStatusCode01_thenStatus400() throws Exception {

        when(studentService.updateStudentProfile(any())).thenReturn(setStudentResponseMock(false));

        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, 12314)
                        .content("""
                                {
                                     "firstName": "Test",
                                     "lastName": "Test",
                                     "email": "test@test.com",
                                     "educationLevel": "Sec 2"
                                 }"""))
                .andExpect(status().isOk());

        verify(studentService, times(1)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 503 when error object is not empty")
    @Test
    void givenStudents_whenSaveStudentProfile_ReturnStatusCode02_thenStatus400() throws Exception {

        when(studentService.updateStudentProfile(any())).thenReturn(setStudentResponseMock(false));


        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_ID, 12314)
                        .content("""
                                {
                                     "firstName": "Test",
                                     "lastName": "Test",
                                     "email": "test@test.com",
                                     "educationLevel": "Sec 2"
                                 }"""))
                .andExpect(status().isOk());

        verify(studentService, times(1)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 200 when send email invitation to student")
    @Test
    void givenStudents_whenSendInvitationToStudent_thenStatus200() throws Exception {
        String expectedStatusCode = "00";

        when(studentService.sendInvitation(any(), anyString())).thenReturn(true);

        mockMvc.perform(post("/v1/student/send-invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-ID", HEADER_KEY_USER_ID)
                        .content("""
                                {
                                      "studentEmail": "test@gmail.com",
                                      "studentFullName": "test",
                                      "classCode": "test"
                                  }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode));

        verify(studentService, times(1)).sendInvitation(any(), anyString());
    }

    private StudentResponseDto setStudentResponseMock(boolean flag) {
        return new StudentResponseDto(flag);
    }

}
