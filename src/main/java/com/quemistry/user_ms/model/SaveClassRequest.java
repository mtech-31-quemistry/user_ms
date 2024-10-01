package com.quemistry.user_ms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveClassRequest {

    private Long id;

    private String educationLevel;

    private String subject;

    private String description;

    private String userId;

    private Date startDate;

    private Date endDate;

    private List<String> tutorAccountIds = new ArrayList<>();

    private List<String> tutorEmails = new ArrayList<>();
}
