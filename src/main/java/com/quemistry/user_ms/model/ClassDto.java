package com.quemistry.user_ms.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassDto {

    private Long id;

    private String educationLevel;

    private String subject;

    private String description;

    private String userId;

    private String status;

    private Date startDate;

    private Date endDate;

    private List<ClassInvitationDto> classInvitations = new ArrayList<>();

    private List<String> tutorEmails = new ArrayList<>();

    private List<TutorDto> tutors = new ArrayList<>();

    private HashSet<StudentDto> students = new HashSet();


}
