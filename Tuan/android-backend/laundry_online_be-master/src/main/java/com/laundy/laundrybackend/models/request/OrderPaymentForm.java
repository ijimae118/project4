package com.laundy.laundrybackend.models.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPaymentForm {
    @NotNull
    private Long orderId;

    @NotNull
    @NotBlank
    private String partnerCode;

    @NotNull
    @NotBlank
    private String requestId;
}
