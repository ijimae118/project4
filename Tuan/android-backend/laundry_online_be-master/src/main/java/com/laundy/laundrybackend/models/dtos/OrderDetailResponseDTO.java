package com.laundy.laundrybackend.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laundy.laundrybackend.constant.OrderStatusEnum;
import com.laundy.laundrybackend.models.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponseDTO {
    private Long id;

    private Long serviceId;
    private String serviceName;

    private OrderStatusEnum status;

    private Double distance;

    private BigDecimal totalShipFee;

    private BigDecimal totalServiceFee;

    private BigDecimal totalBill;

    private String shippingAddress;

    private String shippingPersonName;

    private String shippingPersonPhoneNumber;

    private String pickUpPersonName;

    private String pickUpPersonPhoneNumber;

    private String pickUpAddress;

    private String pickUpWard;

    private String pickUpDistrict;

    private String pickUpCity;

    @JsonProperty
    private Boolean isPaid;

    @JsonProperty
    private Boolean isCashPay;

    @JsonProperty
    private PaymentInfo paymentInfo;

    private String createdDate;

    private String lastUpdatedDate;

    private String pickUpDateTime;

    private String deliveryDateTime;

    private Double longShipping;

    private Double latShipping;

    @JsonProperty("shipper")
    private ShipperUser ShipperUser;

    @JsonProperty("serviceDetails")
    private List<OrderServiceDetailDTO> orderServiceDetailDTOS;

    public static OrderDetailResponseDTO OrderDetailResponseDTOFromOrderAndService(Order order, Service service, List<OrderServiceDetail> serviceDetails) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY hh:ss");
        return OrderDetailResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalShipFee(order.getTotalShipFee())
                .totalServiceFee(order.getTotalServiceFee())
                .totalBill(order.getTotalBill())
                .shippingAddress(order.getShippingAddress())
                .shippingPersonName(order.getShippingPersonName())
                .shippingPersonPhoneNumber(order.getShippingPersonPhoneNumber())
                .pickUpAddress(order.getPickUpAddress())
                .pickUpCity(order.getPickUpCity())
                .pickUpDistrict(order.getPickUpDistrict())
                .pickUpWard(order.getPickUpWard())
                .pickUpPersonName(order.getPickUpPersonName())
                .pickUpPersonPhoneNumber(order.getPickUpPersonPhoneNumber())
                .ShipperUser(order.getShipperUser() == null ? null : order.getShipperUser())
                .createdDate(formatter.format(order.getCreatedDate()))
                .lastUpdatedDate((formatter.format(order.getLastUpdatedDate())))
                .pickUpDateTime(order.getPickUpDateTime() == null ? null : formatter.format(order.getPickUpDateTime()))
                .deliveryDateTime(order.getDeliveryDateTime() == null ? null : formatter.format(order.getDeliveryDateTime()))
                .serviceName(service.getName())
                .serviceId(service.getId())
                .distance(order.getDistance())
                .isPaid(order.getIsPaid())
                .paymentInfo(order.getPaymentInfo())
                .isCashPay(order.getIsCashPay())
                .longShipping(order.getLongShipping())
                .latShipping((order.getLatShipping()))
                .orderServiceDetailDTOS(OrderServiceDetailDTO.orderServiceDetailDTOSFromOrderServiceDetails(serviceDetails))
                .build();
    }
}
