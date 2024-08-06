package com.quemistry.user_ms.repository.entity;

import com.quemistry.user_ms.repository.converter.AttributeEncryptor;
import com.quemistry.user_ms.repository.entity.base.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "user", schema = "qms_user")
public class User extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "account_id")
    private String accountId;

    @Convert(converter = AttributeEncryptor.class)
    private String email;

    @Column(name = "first_name")
    @Convert(converter = AttributeEncryptor.class)
    private String firstName;

    @Column(name = "last_name")
    @Convert(converter = AttributeEncryptor.class)
    private String lastName;

    @OneToOne(mappedBy = "userEntity")
    private Student studentEntity;

    @OneToOne(mappedBy = "userEntity")
    private Tutor tutorEntity;

    public String getFullName() {
        return "%s %s".formatted(this.firstName, this.lastName);
    }
}
