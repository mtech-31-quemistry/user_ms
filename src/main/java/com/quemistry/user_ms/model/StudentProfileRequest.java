package com.quemistry.user_ms.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String email;

    private String userId;

    private String educationLevel;

    private String tuitionCentre;

}
