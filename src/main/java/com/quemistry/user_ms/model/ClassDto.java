package com.quemistry.user_ms.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassDto {

    private Long id;

    @NotBlank
    private String code;

    private String educationLevel;

    private String subject;

    private String description;

    private String userId;

    private List<ClassInvitationDto> classInvitations = new ArrayList<>();

    private List<String> tutorAccountIds = new ArrayList<>();

    private List<String> tutorEmails = new ArrayList<>();

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    private String status;
}
