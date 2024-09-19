package com.quemistry.user_ms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class TutorDto {

    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String email;

    @JsonIgnore
    private String accountId;

    private String educationLevel;

    private String tuitionCentre;
}
