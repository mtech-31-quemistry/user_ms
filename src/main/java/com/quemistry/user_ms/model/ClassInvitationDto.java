package com.quemistry.user_ms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassInvitationDto {


    private Long id;

    private String userEmail;

    private String code;

//    private ClassDto classEntity;

    private int userType;

    private String status;
}
