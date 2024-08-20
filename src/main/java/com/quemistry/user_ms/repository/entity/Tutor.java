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
}
