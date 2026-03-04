package br.com.victorvale.academymanagerbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "students")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    private Integer age;

    @Column(length = 50)
    private String belt;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(precision = 3, scale = 2)
    private BigDecimal height;

    @Builder.Default
    @Column(name = "is_active")
    private boolean isActive = true;
}
