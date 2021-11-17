package com.laundy.laundrybackend.controller.impl;

import com.laundy.laundrybackend.constant.AcceptedShipperOrderStatusEnum;
import com.laundy.laundrybackend.constant.Constants;
import com.laundy.laundrybackend.controller.api.ShipperInterface;
import com.laundy.laundrybackend.models.request.UserLoginForm;
import com.laundy.laundrybackend.models.response.GeneralResponse;
import com.laundy.laundrybackend.models.response.ResponseFactory;
import com.laundy.laundrybackend.service.OrderService;
import com.laundy.laundrybackend.service.ShipperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShipperController implements ShipperInterface {
    private final ShipperUserService shipperUserService;
    private final OrderService orderService;

    @Autowired
    public ShipperController(ShipperUserService shipperUserService, OrderService orderService) {
        this.shipperUserService = shipperUserService;
        this.orderService = orderService;
    }

    @Override
    public GeneralResponse<?> login(UserLoginForm userLoginForm) {
        return ResponseFactory.successResponse(shipperUserService.loginUser(userLoginForm));
    }

    @Override
    public GeneralResponse<?> acceptOrder(Long orderId) {
        return ResponseFactory.successResponse(orderService.acceptOrderByShipper(orderId));
    }

    @Override
    public GeneralResponse<?> cancelOrder(Long orderId) {
        orderService.cancelOrderByShipper(orderId);
        return ResponseFactory.successResponse(Constants.ORDER_UNACCEPTED);
    }

    @Override
    public GeneralResponse<?> receiveOrder(Long orderId) {
        orderService.receivedOrderByShipper(orderId);
        return ResponseFactory.successResponse(Constants.ORDER_RECEIVED_BY_SHIPPER);
    }

    @Override
    public GeneralResponse<?> deliverOrder(Long orderId) {
        orderService.deliverOrderByShipper(orderId);
        return ResponseFactory.successResponse(Constants.ORDER_DELIVERING);
    }

    @Override
    public GeneralResponse<?> completeOrder(Long orderId) {
        orderService.completeOrderByShipper(orderId);
        return ResponseFactory.successResponse(Constants.ORDER_COMPLETED);
    }

    @Override
    public GeneralResponse<?> availableOrder(int page, int size) {
        return ResponseFactory.successResponse(orderService.getAvailableOrderListForShipper(page,size));
    }


    @Override
    public GeneralResponse<?> getOrderByStatus(String orderStatus, int page, int size) {
        AcceptedShipperOrderStatusEnum statusEnum = orderStatus == null ? null : AcceptedShipperOrderStatusEnum.valueOf(orderStatus);
        return ResponseFactory.successResponse(orderService.getOrdersByStatusForShipper(statusEnum,page,size));
    }

    @Override
    public GeneralResponse<?> getOrderDetail(Long orderId) {
        return ResponseFactory.successResponse(orderService.getOrderDetailForShipper(orderId));
    }
}
