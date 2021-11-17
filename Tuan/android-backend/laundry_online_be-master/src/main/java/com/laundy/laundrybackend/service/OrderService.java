package com.laundy.laundrybackend.service;

import com.laundy.laundrybackend.constant.AcceptedShipperOrderStatusEnum;
import com.laundy.laundrybackend.constant.OrderStatusEnum;
import com.laundy.laundrybackend.models.dtos.OrderDetailResponseDTO;
import com.laundy.laundrybackend.models.dtos.OrderResponseDTO;
import com.laundy.laundrybackend.models.dtos.OrderResponseForShipperDTO;
import com.laundy.laundrybackend.models.request.NewOrderForm;
import com.laundy.laundrybackend.models.request.OrderPaymentForm;
import com.laundy.laundrybackend.models.request.OrderServiceDetailForm;
import com.laundy.laundrybackend.models.response.ServicesFeeResponse;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    OrderDetailResponseDTO createNewOrder(NewOrderForm orderForm);

    List<OrderResponseDTO> getOrdersByStatus(OrderStatusEnum status, int page, int size);

    List<OrderResponseDTO> getIncompleteOrders(int page, int size);

    OrderDetailResponseDTO getOrderDetailForUser(Long orderId);

    NewOrderForm cancelOrder(Long orderId);

    void updateOrderPayment(OrderPaymentForm orderPaymentForm);

    BigDecimal getServicesFee(Double distance);

    ServicesFeeResponse getServicesFee(List<OrderServiceDetailForm> detailFormList);

    OrderDetailResponseDTO acceptOrderByShipper(Long orderId);

    void cancelOrderByShipper(Long orderId);

    void receivedOrderByShipper(Long orderId);

    void deliverOrderByShipper(Long orderId);

    void completeOrderByShipper(Long orderId);

    OrderDetailResponseDTO getOrderDetailForShipper(Long orderId);

    List<OrderResponseForShipperDTO> getOrdersByStatusForShipper(AcceptedShipperOrderStatusEnum status, int page, int size);

    void recivedOrderByStaff(Long orderId);

    void doneOrderByStaff(Long orderId);

    List<OrderResponseDTO> getOrdersByStatusAndShipperForStaff(OrderStatusEnum status, String shipperLoginId, int page, int size);
    OrderDetailResponseDTO getOrderDetailForStaff(Long orderId);

    List<OrderResponseForShipperDTO> getAvailableOrderListForShipper(int page, int size);
}
