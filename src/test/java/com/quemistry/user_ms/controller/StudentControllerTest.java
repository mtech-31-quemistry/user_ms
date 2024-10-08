package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.model.AcceptInvitationDto;
import com.quemistry.user_ms.model.SearchStudentRequest;
import com.quemistry.user_ms.model.StudentDto;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_EMAIL;
import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private ObjectMapper objectMapper = new ObjectMapper();

    String studentEmail = "student@example.com";
    String studentAccountId = "12345";


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

        when(studentService.updateStudentProfile(any())).thenReturn(getStudentDtoMock());

        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_EMAIL, studentEmail)
                        .header(HEADER_KEY_USER_ID, studentAccountId)
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
                .andExpect(jsonPath("$.statusMessage").value(expectedStatusMessage));
//                .andExpect(jsonPath("$.payload.success").value(expectedSuccessFlag));

        verify(studentService, times(1)).updateStudentProfile(any());
    }

    @DisplayName("Should return status 400 when User ID is empty in header")
    @Test
    void givenStudents_whenSaveStudentProfileWithoutUserID_thenStatus400() throws Exception {

        when(studentService.updateStudentProfile(any())).thenReturn(getStudentDtoMock());

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

        when(studentService.updateStudentProfile(any())).thenReturn(getStudentDtoMock());

        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_EMAIL, studentEmail)
                        .header(HEADER_KEY_USER_ID, studentAccountId)
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

        when(studentService.updateStudentProfile(any())).thenReturn(null);

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
                        .header(HEADER_KEY_USER_EMAIL, studentEmail)
                        .header(HEADER_KEY_USER_ID, studentAccountId)
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

    @DisplayName("Should return status 500 when service throw any other exception")
    @Test
    void givenStudents_whenSaveStudentProfileAndThrowGeneralException_thenStatus503() throws Exception {

        when(studentService.updateStudentProfile(any())).thenAnswer(invocation -> {
            throw new Exception("general exception");
        });


        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_EMAIL, studentEmail)
                        .header(HEADER_KEY_USER_ID, studentAccountId)
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
                        .header(HEADER_KEY_USER_EMAIL, studentEmail)
                        .header(HEADER_KEY_USER_ID, studentAccountId)
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

    @DisplayName("Should return status 400 when return status code is 01")
    @Test
    void givenStudents_whenSaveStudentProfile_ReturnStatusCode01_thenStatus400() throws Exception {

        when(studentService.updateStudentProfile(any())).thenReturn(null);

        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_EMAIL, studentEmail)
                        .header(HEADER_KEY_USER_ID, studentAccountId)
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

    @DisplayName("Should return status 503 when error object is not empty")
    @Test
    void givenStudents_whenSaveStudentProfile_ReturnStatusCode02_thenStatus400() throws Exception {

        when(studentService.updateStudentProfile(any())).thenReturn(null);


        mockMvc.perform(post("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_KEY_USER_EMAIL, studentEmail)
                        .header(HEADER_KEY_USER_ID, studentAccountId)

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

    @Test
    void testAcceptStudentInvitation_Success() throws Exception {
        String invitationCode = "validCode";

        AcceptInvitationDto acceptInvitationDto = new AcceptInvitationDto(invitationCode);

        when(studentService.acceptInvitation(studentEmail, studentAccountId, invitationCode))
                .thenReturn(true);

        mockMvc.perform(post("/v1/student/accept-invitation")
                        .header(HEADER_KEY_USER_EMAIL, studentEmail)
                        .header(HEADER_KEY_USER_ID, studentAccountId)
                        .contentType("application/json")
                        .content("{\"invitationCode\": \"validCode\"}"))
                .andExpect(status().isOk());

        verify(studentService).acceptInvitation(studentEmail, studentAccountId, invitationCode);
    }

    @Test
    void testAcceptStudentInvitation_EmailEmpty() throws Exception {
        String studentAccountId = "12345";
        String invitationCode = "validCode";

        mockMvc.perform(post("/accept-invitation")
                        .header(HEADER_KEY_USER_EMAIL, "")
                        .header(HEADER_KEY_USER_ID, studentAccountId)
                        .contentType("application/json")
                        .content("{\"invitationCode\": \"validCode\"}"))
                .andExpect(status().isNotFound());
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
                                      "classId": "1"
                                  }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode));

        verify(studentService, times(1)).sendInvitation(any(), anyString());
    }

    @DisplayName("Should return status 200 when student accepts invitation")
    @Test
    void givenStudents_whenAcceptInvitation_thenStatus200() throws Exception {
        String expectedStatusCode = "00";

        when(studentService.acceptInvitation(anyString(), anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/v1/student/accept-invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-user-id", HEADER_KEY_USER_ID)
                        .header("x-user-email", HEADER_KEY_USER_EMAIL)
                        .content("""
                                {
                                      "invitationCode": "invitation-code"
                                  }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode));

        verify(studentService, times(1)).acceptInvitation(anyString(), anyString(), anyString());
    }

    @DisplayName("Should return status 200 when student retrieve the profile")
    @Test
    void givenStudents_whenGetProfile_thenStatus200() throws Exception {
        String expectedStatusCode = "00";

        StudentDto studentDto = new StudentDto();

        when(studentService.getStudentProfile(anyString())).thenReturn(studentDto);

        mockMvc.perform(get("/v1/student/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-user-email", HEADER_KEY_USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode));

        verify(studentService, times(1)).getStudentProfile(anyString());
    }

    private StudentDto getStudentDtoMock() {
        return new StudentDto();
    }

    @Test
    void testSearchStudent_Success() throws Exception {
        // Given
        String tutorEmail = "tutor@example.com";
        String tutorAccountId = "12345";
        String studentEmail = "student@example.com";
        String studentAccountId = "studentAccountId";
        SearchStudentRequest searchStudentRequest = new SearchStudentRequest(List.of(studentEmail), List.of(studentAccountId));

        // Mock the service to return a profile payload
        StudentDto studentProfile = new StudentDto();
        studentProfile.setEmail(studentEmail);
        studentProfile.setFirstName("John");
        studentProfile.setLastName("Doe");

        when(studentService.searchStudentProfile(List.of(studentEmail), List.of(studentAccountId), tutorEmail)).thenReturn(List.of(studentProfile));

        // Perform the HTTP POST request
        mockMvc.perform(post("/v1/student/search")
                        .header(HEADER_KEY_USER_EMAIL, tutorEmail)  // Mock headers
                        .header(HEADER_KEY_USER_ID, tutorAccountId)  // Mock headers
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchStudentRequest)))
                .andExpect(status().isOk())  // Check if the response status is 200 OK
                .andExpect(jsonPath("$.payload[0].email").value(studentEmail))  // Verify the response payload
                .andExpect(jsonPath("$.payload[0].firstName").value("John"))  // Check payload details
                .andExpect(jsonPath("$.payload[0].lastName").value("Doe"));  // Check payload details

        // Verify that the service method was called once with correct parameters
        verify(studentService, times(1)).searchStudentProfile(List.of(studentEmail), List.of(studentAccountId), tutorEmail);
    }

}
