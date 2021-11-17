package com.laundy.laundrybackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ship_fees")
public class ShipFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_distance", nullable = false)
    @Size(max = 99999999)
    private Double minDistance;

    @Column(name = "max_distance", nullable = false)
    @Size(max = 99999999)
    private Double maxDistance;

    @Column(nullable = false)
    private BigDecimal fee;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Order.class, mappedBy = "shipFee")
    @JsonBackReference
    private List<Order> orders;

}
