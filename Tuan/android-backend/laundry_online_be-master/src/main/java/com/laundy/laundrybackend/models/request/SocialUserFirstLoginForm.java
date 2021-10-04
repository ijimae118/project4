package com.laundy.laundrybackend.models.request;

import com.laundy.laundrybackend.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserFirstLoginForm {
    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    @Email
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
