package com.laundy.laundrybackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.laundy.laundrybackend.constant.UserRoleEnum;
import com.laundy.laundrybackend.models.request.RegisterUserForm;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"phone_number", "username", "email"})})
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonProperty
    @Column(nullable = false)
    private Boolean isSocialUser;

    @NotBlank
    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Order.class, mappedBy = "user")
    @JsonBackReference
    private List<Order> orders;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Address.class, mappedBy = "address")
    @JsonBackReference
    private List<Address> addresses;

    @PrePersist
    void setUserRole(){
        setRole(UserRoleEnum.ROLE_USER);
    }
    public static User getUserFromRegisterForm(RegisterUserForm registerUserForm) {
        return User.builder()
                .username(registerUserForm.getUsername())
                .password(registerUserForm.getPassword())
                .name(registerUserForm.getName())
                .phoneNumber(registerUserForm.getPhoneNumber())
                .email(registerUserForm.getEmail())
                .isSocialUser(Boolean.FALSE)
                .build();
    }
}
