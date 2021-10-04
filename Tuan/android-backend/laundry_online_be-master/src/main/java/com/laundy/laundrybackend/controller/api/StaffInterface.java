package com.laundy.laundrybackend.controller.api;

import com.laundy.laundrybackend.constant.OrderStatusEnum;
import com.laundy.laundrybackend.constant.StaffUserRoleEnum;
import com.laundy.laundrybackend.models.request.RegisterNewShipperUserForm;
import com.laundy.laundrybackend.models.request.RegisterNewStaffUserForm;
import com.laundy.laundrybackend.models.request.UserLoginForm;
import com.laundy.laundrybackend.models.response.GeneralResponse;
import com.laundy.laundrybackend.validator.ValueOfEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RequestMapping("staff/")
@CrossOrigin("*")
@Validated
public interface StaffInterface {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("auth/signup")
    @Operation(description = "Đăng kí tài khoản nhân viên mới", summary = "Đăng kí tài khoản nhân viên mới")
    GeneralResponse<?> registerNewStaffUser(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form Đăng kí Staff User mới", required = true) RegisterNewStaffUserForm registerNewStaffUserForm, @Parameter(name = "role", description = "Role của Staff User" +
            " ROLE_ADMIN" +
            ", ROLE_STAFF,", example = "ROLE_STAFF")
    @ValueOfEnum(enumClass = StaffUserRoleEnum.class) @RequestParam(value = "role") String role);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("auth/signup-new-shipper")
    @Operation(description = "Đăng kí tài khoản shipper mới", summary = "Đăng kí tài khoản shipper mới")
    GeneralResponse<?> registerNewShipperUser(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form Đăng kí Shipper User mới", required = true) RegisterNewShipperUserForm registerNewShipperUserForm);

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF')")
    @PutMapping("orders/received-order")
    @Operation(description = "Nhận đồ của Order", summary = "Nhận đồ của Order")
    GeneralResponse<?> receivedOrder(@RequestParam("orderId") @Parameter(description = "Id của Order", name = "orderId",required = true) Long orderId);

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF')")
    @PutMapping("orders/done-order")
    @Operation(description = "Done giặt đồ của Order", summary = "Done giặt đồ của Order")
    GeneralResponse<?> DoneOrder(@RequestParam("orderId") @Parameter(description = "Id của Order", name = "orderId",required = true) Long orderId);

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF')")
    @GetMapping("orders/list")
    @Operation(description = "Get List order theo trạng thái order và thông tin đăng nhập của shipper", summary = "Get List order theo trạng thái order và thông tin đăng nhập của shipper")
    GeneralResponse<?> getOrderByStatusAndShipperLoginId(@Parameter(name = "orderStatus", description = "Status của đơn hàng là 1 trong các giá trị sau NEW, SHIPPER_ACCEPTED_ORDER," +
            " SHIPPER_RECEIVED_ORDER, STORE_RECEIVED_ORDER, STORE_DONE_ORDER, SHIPPER_DELIVER_ORDER, COMPLETE_ORDER, CANCEL", example = "NEW")
                                        @ValueOfEnum(enumClass = OrderStatusEnum.class) @RequestParam(value = "orderStatus", required = false) String orderStatus, @RequestParam(value = "shipperLoginId",required = false) @Parameter(description = "Thông tin đăng nhập của shipper(username hoặc số điện thoại hoặc email)", example = "0") String shipperLoginId, @RequestParam("page") @Parameter(description = "số trang", example = "0", required = true) @NotNull int page, @RequestParam("size") @Parameter(description = "sô bản ghi của trang", example = "2", required = true) @NotNull int size);

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF')")
    @GetMapping("orders/detail")
    @Operation(description = "Chi tiết order", summary = "Chi tiết order")
    GeneralResponse<?> getOrderDetail(@Parameter(name = "orderId", description = "Id đơn hàng", required = true) @RequestParam("orderId") @NotNull Long orderId);

    @PostMapping("auth/signin")
    @Operation(description = "Đăng nhập hệ thống với username hoặc số điện thoại và password", summary = "Đăng nhập hệ thống với username hoặc số điện thoại và password")
    GeneralResponse<?> login(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form thông tin đăng nhập", required = true) UserLoginForm userLoginForm);
}
