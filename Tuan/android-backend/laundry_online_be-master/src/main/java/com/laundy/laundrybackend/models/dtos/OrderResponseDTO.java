package com.laundy.laundrybackend.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laundy.laundrybackend.constant.OrderStatusEnum;
import com.laundy.laundrybackend.models.Order;
import com.laundy.laundrybackend.models.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;

    private Long serviceId;
    private String serviceName;

    private Double distance;

    private OrderStatusEnum status;

    private BigDecimal totalShipFee;

    private BigDecimal totalServiceFee;

    private BigDecimal totalBill;

    @JsonProperty
    private Boolean isPaid;

    private Long userId;

    private String shippingAddress;

    private String shippingPersonName;

    private String shippingPersonPhoneNumber;

    private String pickUpAddress;

    private String pickUpWard;

    private String pickUpDistrict;

    private String pickUpCity;

    private String pickUpPersonName;

    private String pickUpPersonPhoneNumber;

    private Long shipperUserId;

    private String createdDate;

    private String lastUpdatedDate;

    private String pickUpDateTime;

    private String deliveryDateTime;

    public static OrderResponseDTO orderResponseDTOFromOrderAndService(Order order, Service service) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY hh:ss");
        return OrderResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalShipFee(order.getTotalShipFee())
                .totalServiceFee(order.getTotalServiceFee())
                .totalBill(order.getTotalBill())
                .userId(order.getUser().getId())
                .shippingAddress(order.getShippingAddress())
                .shippingPersonName(order.getShippingPersonName())
                .shippingPersonPhoneNumber(order.getShippingPersonPhoneNumber())
                .pickUpAddress(order.getPickUpAddress())
                .pickUpCity(order.getPickUpCity())
                .pickUpDistrict(order.getPickUpDistrict())
                .pickUpWard(order.getPickUpWard())
                .pickUpPersonName(order.getPickUpPersonName())
                .pickUpPersonPhoneNumber(order.getPickUpPersonPhoneNumber())
                .shipperUserId(order.getShipperUser() == null ? null : order.getShipperUser().getId())
                .createdDate(formatter.format(order.getCreatedDate()))
                .lastUpdatedDate((formatter.format(order.getLastUpdatedDate())))
                .pickUpDateTime(order.getPickUpDateTime() == null ? null : formatter.format(order.getPickUpDateTime()))
                .deliveryDateTime(order.getDeliveryDateTime() == null ? null : formatter.format(order.getDeliveryDateTime()))
                .serviceName(service.getName())
                .serviceId(service.getId())
                .distance(order.getDistance())
                .isPaid(order.getIsPaid())
                .build();
    }
}
