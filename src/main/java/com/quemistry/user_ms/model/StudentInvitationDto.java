package com.quemistry.user_ms.model;

import jakarta.validation.constraints.NotBlank;

public record StudentInvitationDto(
        @NotBlank String studentEmail,
        @NotBlank String studentFullName,
        @NotBlank String classCode) {
}

