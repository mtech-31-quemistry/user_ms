package com.quemistry.user_ms.repository.entity;

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
@Table(name = "class", schema = "qms_user")
public class Class extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String description;

    private String subject;

    @Column(name = "education_level")
    private String educationLevel;

    @OneToOne(mappedBy = "classEntity")
    private ClassInvitation invitation;
}
