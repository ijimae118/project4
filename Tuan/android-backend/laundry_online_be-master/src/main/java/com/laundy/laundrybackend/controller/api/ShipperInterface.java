package com.laundy.laundrybackend.controller.api;

import com.laundy.laundrybackend.constant.AcceptedShipperOrderStatusEnum;
import com.laundy.laundrybackend.constant.OrderStatusEnum;
import com.laundy.laundrybackend.models.request.UserLoginForm;
import com.laundy.laundrybackend.models.response.GeneralResponse;
import com.laundy.laundrybackend.validator.ValueOfEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping("shipper/")
@CrossOrigin("*")
@Validated
public interface ShipperInterface {
    @PostMapping("auth/signin")
    @Operation(description = "Đăng nhập hệ thống với username hoặc số điện thoại và password", summary = "Đăng nhập hệ thống với username hoặc số điện thoại và password")
    GeneralResponse<?> login(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form thông tin đăng nhập", required = true) UserLoginForm userLoginForm);

    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    @PutMapping("orders/accept-order")
    @Operation(description = "Nhận Order", summary = "Nhận Order")
    GeneralResponse<?> acceptOrder(@RequestParam("orderId") @Parameter(description = "Id của Order", name = "orderId",required = true) Long orderId);

    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    @PutMapping("orders/cancel-order")
    @Operation(description = "Cancel Order", summary = "Cancel Order")
    GeneralResponse<?> cancelOrder(@RequestParam("orderId") @Parameter(description = "Id của Order", name = "orderId",required = true) Long orderId);

    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    @PutMapping("orders/receive-order")
    @Operation(description = "Nhận đồ của Order", summary = "Nhận đồ của Order")
    GeneralResponse<?> receiveOrder(@RequestParam("orderId") @Parameter(description = "Id của Order", name = "orderId",required = true) Long orderId);

    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    @PutMapping("orders/deliver-order")
    @Operation(description = "Giao đồ của Order", summary = "Giao đồ của Order")
    GeneralResponse<?> deliverOrder(@RequestParam("orderId") @Parameter(description = "Id của Order", name = "orderId",required = true) Long orderId);

    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    @PutMapping("orders/complete-order")
    @Operation(description = "Complete Order", summary = "Complete Order")
    GeneralResponse<?> completeOrder(@RequestParam("orderId") @Parameter(description = "Id của Order", name = "orderId",required = true) Long orderId);

    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    @GetMapping("orders/available-order")
    @Operation(description = "Lấy list order mới có thể nhận", summary = "Lấy list order mới có thể nhận")
    GeneralResponse<?> availableOrder(@RequestParam("page") @Parameter(description = "số trang", example = "0", required = true) @NotNull int page, @RequestParam("size") @Parameter(description = "sô bản ghi của trang", example = "2", required = true) @NotNull int size);

    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    @GetMapping("orders/list")
    @Operation(description = "Get List order theo trạng thái order", summary = "Get List order theo trạng thái order")
    GeneralResponse<?> getOrderByStatus(@Parameter(name = "orderStatus", description = "Status của đơn hàng là 1 trong các giá trị sau SHIPPER_ACCEPTED_ORDER," +
            " SHIPPER_RECEIVED_ORDER, STORE_RECEIVED_ORDER, STORE_DONE_ORDER, SHIPPER_DELIVER_ORDER, COMPLETE_ORDER, INCOMPLETE_ORDER", example = "SHIPPER_ACCEPTED_ORDER")
                                        @ValueOfEnum(enumClass = AcceptedShipperOrderStatusEnum.class) @RequestParam(value = "orderStatus", required = false) String orderStatus, @RequestParam("page") @Parameter(description = "số trang", example = "0", required = true) @NotNull int page, @RequestParam("size") @Parameter(description = "sô bản ghi của trang", example = "2", required = true) @NotNull int size);

    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    @PostMapping("orders/detail")
    @Operation(description = "Chi tiết order", summary = "Chi tiết order")
    GeneralResponse<?> getOrderDetail(@Parameter(name = "orderId", description = "Id đơn hàng", required = true) @RequestParam("orderId") @NotNull Long orderId);
}
