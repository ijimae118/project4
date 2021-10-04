package com.laundy.laundrybackend.models.request;

import com.laundy.laundrybackend.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserForm {
    @NotBlank
    @Size(min=3, max = 60)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 6, max = 100)
    private String name;


    @Email
    @NotBlank
    private String email;

    @NotBlank
    @NotNull
    @Pattern(regexp = Constants.PHONE_NUMBER_REGEX)
    private String phoneNumber;

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
    @Size(max = 255)
    private String address;
}
