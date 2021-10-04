package com.laundy.laundrybackend.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laundy.laundrybackend.models.OrderServiceDetail;
import com.laundy.laundrybackend.models.dtos.OrderServiceDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicesFeeResponse {
    private BigDecimal totalServicesFee;
    @JsonProperty("serviceDetails")
    List<OrderServiceDetailDTO> serviceDetails;
}
