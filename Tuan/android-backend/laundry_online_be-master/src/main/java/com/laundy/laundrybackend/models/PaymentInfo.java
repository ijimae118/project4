package com.laundy.laundrybackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laundy.laundrybackend.models.request.OrderPaymentForm;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payment_info")
public class PaymentInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String partnerCode;

    @NotNull
    @NotBlank
    private String requestId;


    @JsonIgnore
    @OneToOne(mappedBy = "paymentInfo")
    private Order order;

    public static PaymentInfo paymentInfoFromOrderPaymentForm(OrderPaymentForm orderPaymentForm){
        return PaymentInfo.builder()
                .partnerCode(orderPaymentForm.getPartnerCode())
                .requestId(orderPaymentForm.getRequestId())
                .build();
    }
}
