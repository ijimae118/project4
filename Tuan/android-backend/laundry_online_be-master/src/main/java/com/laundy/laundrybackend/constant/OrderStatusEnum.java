package com.laundy.laundrybackend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    NEW(0,"New","Mới tạo"),
    SHIPPER_ACCEPTED_ORDER(1,"Shipper Accepted Order", "Shipper nhận đơn"),
    SHIPPER_RECEIVED_ORDER(2,"Shipper Received Order", "Shipper đã nhận đồ"),
    STORE_RECEIVED_ORDER(3, "Store Received Order", "Cửa hàng đã nhận đơn"),
    STORE_DONE_ORDER(4, "Store Done Order", "Cửa hàng đã giặt xong đơn"),
    SHIPPER_DELIVER_ORDER(5, "Shipper Deliver Order", "Shipper đang giao đơn"),
    COMPLETE_ORDER(6, "Completed Order","Đơn hàng hoàn thành"),
    CANCEL(7,"CANCEL","Hủy đơn");

    private final int code;
    private final String value;
    private final String meaning;
}
