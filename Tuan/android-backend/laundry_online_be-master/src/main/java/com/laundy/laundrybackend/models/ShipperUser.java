package com.laundy.laundrybackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laundy.laundrybackend.constant.UserRoleEnum;
import com.laundy.laundrybackend.models.request.RegisterNewShipperUserForm;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "shipper_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"phone_number", "username","license_plate","email"})})
public class ShipperUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 6, max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(min = 6, max = 50)
    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotBlank
    @Column(nullable = false)
    private String vehicleType;

    @NotBlank
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Order.class, mappedBy = "shipperUser")
    @JsonBackReference
    private List<Order> orders;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role;

    @PrePersist
    void setShipperRole(){
        setRole(UserRoleEnum.ROLE_SHIPPER);
    }

    public static ShipperUser shipperUserFromRegisterForm(RegisterNewShipperUserForm form){
        return ShipperUser.builder()
                .email(form.getEmail())
                .username(form.getUsername())
                .licensePlate(form.getLicensePlate())
                .name(form.getName())
                .phoneNumber(form.getPhoneNumber())
                .vehicleType(form.getVehicleType())
                .build();
    }
}
