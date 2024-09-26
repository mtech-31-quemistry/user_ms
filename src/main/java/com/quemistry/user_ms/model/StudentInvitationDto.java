package com.quemistry.user_ms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentInvitationDto(
        @NotBlank String studentEmail,
        @NotBlank String studentFullName,
        @NotNull Long classId) {
}

