package com.laundy.laundrybackend.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewOrderForm {
    @NotNull
    private Double distance;
    @NotNull
    private Long serviceId;
    @NotNull
    private BigDecimal totalShipFee;
    @NotNull
    private BigDecimal totalServiceFee;
    @NotNull
    private List<OrderServiceDetailForm> orderServiceDetails;

    @NotBlank
    @NotNull
    private String shippingAddress;

    @NotBlank
    @NotNull
    private String shippingPersonName;

    @NotBlank
    @NotNull
    private String shippingPersonPhoneNumber;

    @NotNull
    private Double longShipping;

    @NotNull
    private Double latShipping;

    @NotNull
    private Boolean isCashPay;
}
