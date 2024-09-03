package com.quemistry.user_ms.repository.entity;

import com.quemistry.user_ms.repository.converter.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "class_invitation", schema = "qms_user", uniqueConstraints = @UniqueConstraint(columnNames = {"user_email", "class_id"}))
public class ClassInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = AttributeEncryptor.class)
    @Column(nullable = false, name = "user_email")
    private String userEmail;

    @Column
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Class classEntity;

    @Column(name = "user_type")
    private int userType;

    private String status;

    @Override
    public String toString() {
        return "ClassInvitation{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", code='" + code + '\'' +
                ", classEntity=" + classEntity.getId() +
                ", userType=" + userType +
                ", status='" + status + '\'' +
                '}';
    }
}
