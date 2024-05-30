package com.quemistry.user_ms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto extends UserDto {
    private String educationLevel;

    public StudentDto(String firstName,
                      String lastName,
                      String email,
                      String userId,
                      String educationLevel) {
        super(firstName, lastName, email, userId);
        this.educationLevel = educationLevel;
    }
}
