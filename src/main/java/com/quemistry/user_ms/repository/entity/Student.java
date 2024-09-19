package com.quemistry.user_ms.repository.entity;

import com.quemistry.user_ms.repository.entity.base.Base;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
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
@NamedEntityGraph(
        name = "student-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("userEntity")
        }
)
@Table(name = "student", schema = "qms_user")
public class Student extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "education_level")
    private String educationLevel;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userEntity;

    @ManyToMany(mappedBy = "students")
    private List<Class> classes = new ArrayList<>();

    public Student(String accountId, String email) {
        this.userEntity = new User();
        userEntity.setAccountId(accountId);
        userEntity.setEmail(email);
        userEntity.setCreatedAndModifiedUser(accountId);
        setCreatedAndModifiedUser(accountId);
    }
}
