package com.quemistry.user_ms.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
