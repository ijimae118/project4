package com.laundy.laundrybackend.controller.impl;

import com.laundy.laundrybackend.constant.Constants;
import com.laundy.laundrybackend.constant.OrderStatusEnum;
import com.laundy.laundrybackend.controller.api.StaffInterface;
import com.laundy.laundrybackend.models.request.RegisterNewShipperUserForm;
import com.laundy.laundrybackend.models.request.RegisterNewStaffUserForm;
import com.laundy.laundrybackend.models.request.UserLoginForm;
import com.laundy.laundrybackend.models.response.GeneralResponse;
import com.laundy.laundrybackend.models.response.ResponseFactory;
import com.laundy.laundrybackend.service.OrderService;
import com.laundy.laundrybackend.service.StaffUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffController implements StaffInterface {
    private final StaffUserService staffUserService;
    private final OrderService orderService;

    @Autowired
    public StaffController(StaffUserService staffUserService, OrderService orderService) {
        this.staffUserService = staffUserService;
        this.orderService = orderService;
    }

    @Override
    public GeneralResponse<?> registerNewStaffUser(RegisterNewStaffUserForm registerNewStaffUserForm, String role) {
        staffUserService.registerNewUser(registerNewStaffUserForm, role);
        return ResponseFactory.successResponse(Constants.REGISTER_NEW_USER_SUCCESS);
    }

    @Override
    public GeneralResponse<?> registerNewShipperUser(RegisterNewShipperUserForm registerNewShipperUserForm) {
        staffUserService.registerNewShipper(registerNewShipperUserForm);
        return ResponseFactory.successResponse(Constants.REGISTER_NEW_USER_SUCCESS);
    }

    @Override
    public GeneralResponse<?> receivedOrder(Long orderId) {
        orderService.recivedOrderByStaff(orderId);
        return ResponseFactory.successResponse(Constants.ORDER_RECEIVED_BY_STORE);
    }

    @Override
    public GeneralResponse<?> DoneOrder(Long orderId) {
        orderService.doneOrderByStaff(orderId);
        return ResponseFactory.successResponse(Constants.ORDER_DONE_BY_STORE);
    }

    @Override
    public GeneralResponse<?> getOrderByStatusAndShipperLoginId(String orderStatus, String shipperLoginId, int page, int size) {
        OrderStatusEnum status = orderStatus == null ? null : OrderStatusEnum.valueOf(orderStatus);
        return ResponseFactory.successResponse(orderService.getOrdersByStatusAndShipperForStaff(status,shipperLoginId,page,size));
    }

    @Override
    public GeneralResponse<?> getOrderDetail(Long orderId) {
        return ResponseFactory.successResponse(orderService.getOrderDetailForStaff(orderId));
    }

    @Override
    public GeneralResponse<?> login(UserLoginForm userLoginForm) {
        return ResponseFactory.successResponse(staffUserService.loginUser(userLoginForm));
    }
}
