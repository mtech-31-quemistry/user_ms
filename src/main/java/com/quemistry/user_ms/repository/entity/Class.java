package com.quemistry.user_ms.repository.entity;

import com.quemistry.user_ms.repository.entity.base.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "classEntity")
    private List<ClassInvitation> invitation = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tutor_class",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "tutor_id")
    )
    private List<Tutor> tutors = new ArrayList<>();

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", subject='" + subject + '\'' +
                ", educationLevel='" + educationLevel + '\'' +
                ", invitation=" + invitation +
                ", tutors=" + tutors.stream().map(Tutor::getUserEntity).map(User::getEmail).toList() +
                '}';
    }
}
