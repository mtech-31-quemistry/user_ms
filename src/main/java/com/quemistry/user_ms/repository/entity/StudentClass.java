package com.quemistry.user_ms.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
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

    @Embeddable
    public static class StudentClassKey implements Serializable {
        @Column(name = "student_id")
        Long studentId;

        @Column(name = "class_id")
        Long classId;
    }
}
