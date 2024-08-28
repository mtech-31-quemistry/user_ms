package com.quemistry.user_ms.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_class", schema = "qms_user", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "class_id"}))
public class StudentClass {

    @EmbeddedId
    StudentClassKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student studentEntity;

    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "class_id")
    private Class classEntity;

    private String status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class StudentClassKey implements Serializable {
        @Column(name = "student_id")
        Long studentId;

        @Column(name = "class_id")
        Long classId;
    }
}
