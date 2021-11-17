package com.laundy.laundrybackend.models.request;

import com.laundy.laundrybackend.constant.Constants;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressForm {
    @NotBlank
    @NotNull
    private String receiverName;

    @NotBlank
    @NotNull
    @Pattern(regexp = Constants.PHONE_NUMBER_REGEX)
    private String receiverPhoneNumber;

    @NotBlank
    @NotNull
    private String city;


    @NotBlank
    @NotNull
    private String district;

    @NotBlank
    @NotNull
    private String ward;

    @NotBlank
    @NotNull
    private String address;
}
