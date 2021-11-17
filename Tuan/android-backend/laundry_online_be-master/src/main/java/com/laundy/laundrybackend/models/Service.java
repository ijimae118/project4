package com.laundy.laundrybackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laundy.laundrybackend.constant.ServiceIconEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "services", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "service",targetEntity = ServiceDetail.class)
    @JsonIgnore
    private List<ServiceDetail> serviceDetails;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ServiceIconEnum serviceIcon;

    @Column
    private String description;
}
