package com.quemistry.user_ms.repository.entity;

import com.quemistry.user_ms.repository.entity.base.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
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

    private String status;

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

    @Column(name = "class_start_ts")
    private OffsetDateTime startDate;

    @Column(name = "class_end_ts")
    private OffsetDateTime endDate;

//    @ManyToMany
//    @JoinTable(
//            name = "student_class",
//            joinColumns = @JoinColumn(name = "class_id"),
//            inverseJoinColumns = @JoinColumn(name = "student_id")
//    )
//    private List<Student> students = new ArrayList<>();

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
