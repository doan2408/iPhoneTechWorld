package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "case_reason")
public class CaseReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reason_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "reason_name", nullable = false)
    private String reasonName;

    @Enumerated(EnumType.STRING)
    @Nationalized
    @Column(name = "case_type", nullable = false, length = 20)
    private CaseType caseType;

    @OneToMany(mappedBy = "reason")
    private Set<AfterSalesItem> afterSalesItems = new LinkedHashSet<>();

}