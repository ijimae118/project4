package com.laundy.laundrybackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laundy.laundrybackend.constant.UserRoleEnum;
import com.laundy.laundrybackend.models.request.RegisterNewStaffUserForm;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "staff_users", uniqueConstraints = {@UniqueConstraint(columnNames = {"phone_number", "username","email"})})
public class StaffUser {
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

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role;


    public static StaffUser staffUserFromRegisterForm(RegisterNewStaffUserForm form){
        return StaffUser.builder()
                .name(form.getName())
                .username(form.getUsername())
                .email(form.getEmail())
                .phoneNumber(form.getPhoneNumber())
                .build();
    }
}
