package com.laundy.laundrybackend.models.request;

import com.laundy.laundrybackend.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterNewShipperUserForm {
    @NotBlank
    @Size(min = 3, max = 60)
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
    @Size(max = 100)
    private String vehicleType;

    @NotBlank
    @NotNull
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;
}
