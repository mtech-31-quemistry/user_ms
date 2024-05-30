package com.quemistry.user_ms.repository.model.base;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @Column(nullable = false, name = "created_on")
    private Timestamp createdOn;

    @Column(nullable = false, name = "created_by")
    private String createdBy;

    @Column(nullable = false, name = "modified_on")
    private Timestamp modifiedOn;

    @Column(nullable = false, name = "modified_by")
    private String modifiedBy;
}
