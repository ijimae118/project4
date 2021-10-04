package com.laundy.laundrybackend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.laundy.laundrybackend.models.request.AddressForm;
import com.laundy.laundrybackend.models.request.RegisterUserForm;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "addresses",uniqueConstraints = {@UniqueConstraint(columnNames = {"receiver_name","receiver_phone_number","city","district","ward","address"} )})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isDefaultAddress;

    @NotBlank
    @NotNull
    @Column(name = "receiver_name",nullable = false)
    private String receiverName;

    @NotBlank
    @NotNull
    @Column(name = "receiver_phone_number",nullable = false)
    private String receiverPhoneNumber;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String city;


    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String district;

    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String ward;

    @NotBlank
    @NotNull
    @Size(max = 255)
    private String address;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Address addressFromRegisterUserForm(RegisterUserForm registerUserForm){
        return Address.builder()
                .address(registerUserForm.getAddress())
                .city(registerUserForm.getCity())
                .ward(registerUserForm.getWard())
                .district((registerUserForm.getDistrict()))
                .isDefaultAddress(Boolean.TRUE)
                .receiverName(registerUserForm.getName())
                .receiverPhoneNumber(registerUserForm.getPhoneNumber())
                .build();
    }

    public static Address addressFromAddressForm(AddressForm addressForm){
        return Address.builder()
                .receiverPhoneNumber(addressForm.getReceiverPhoneNumber())
                .receiverName(addressForm.getReceiverName())
                .address(addressForm.getAddress())
                .city(addressForm.getCity())
                .ward(addressForm.getWard())
                .district((addressForm.getDistrict()))
                .build();
    }
}
