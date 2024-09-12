package com.quemistry.user_ms.repository.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Base {

    @CreationTimestamp
    @Column(nullable = false, name = "created_on")
    private OffsetDateTime createdOn;

    @Column(nullable = false, name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    @Column(nullable = false, name = "modified_on")
    private OffsetDateTime modifiedOn;

    @Column(nullable = false, name = "modified_by")
    @Getter
    private String modifiedBy;

    public void setCreationAndModifiedDetails(OffsetDateTime timestamp, String user) {
        this.setCreatedOn(timestamp);
        this.setCreatedBy(user);

        this.setModifiedOn(timestamp);
        this.setModifiedBy(user);
    }

    public void setCreatedAndModifiedUser(String user) {
        this.setCreatedBy(user);
        this.setModifiedBy(user);
    }

    public void setModified(String user) {
        this.setModifiedOn(OffsetDateTime.now());
        this.setModifiedBy(user);
    }

    public void setCreated(String user) {
        this.setCreatedOn(OffsetDateTime.now());
        this.setCreatedBy(user);
    }


}
