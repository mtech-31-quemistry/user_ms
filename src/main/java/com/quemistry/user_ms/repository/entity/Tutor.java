package com.quemistry.user_ms.repository.entity;

import com.quemistry.user_ms.repository.entity.base.Base;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
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
        name = "tutor-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("userEntity")
        }
)
public class Tutor extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "education_level")
    private String educationLevel;

    @Column(name = "tuition_centre")
    private String tuitionCentre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userEntity;

    @ManyToMany(mappedBy = "tutors")
    private List<Class> classes = new ArrayList<>();
}
