package com.laundy.laundrybackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.laundy.laundrybackend.constant.OrderStatusEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatusEnum status;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private BigDecimal totalShipFee;

    @Column(nullable = false)
    private BigDecimal totalServiceFee;

    @Column(nullable = false)
    private BigDecimal totalBill;

    @NotBlank
    @NotNull
    private String shippingAddress;

    @NotBlank
    @NotNull
    private String shippingPersonName;

    @NotBlank
    @NotNull
    private String shippingPersonPhoneNumber;

    @NotBlank
    @NotNull
    private String pickUpAddress;

    @NotBlank
    @NotNull
    private String pickUpWard;

    @NotBlank
    @NotNull
    private String pickUpDistrict;

    @NotBlank
    @NotNull
    private String pickUpCity;

    @NotBlank
    @NotNull
    private String pickUpPersonName;

    @NotBlank
    @NotNull
    private String pickUpPersonPhoneNumber;

    private LocalDateTime pickUpDateTime;

    private LocalDateTime deliveryDateTime;

    @JsonProperty
    private Boolean isPaid;

    @JsonProperty
    private Boolean isCashPay;

    @NotNull
    private Double longShipping;

    @NotNull
    private Double latShipping;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_info_id")
    private PaymentInfo paymentInfo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "shipfee_id", nullable = false)
    private ShipFee shipFee;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "shipperUser_id")
    private ShipperUser shipperUser;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = OrderServiceDetail.class, mappedBy = "order")
    @JsonBackReference
    private List<OrderServiceDetail> orderServiceDetails;
}
