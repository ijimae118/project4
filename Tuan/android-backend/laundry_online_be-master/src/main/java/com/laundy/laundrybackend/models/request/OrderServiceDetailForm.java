package com.laundy.laundrybackend.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceDetailForm {
    private Long serviceDetailId;
    private Integer quantity;
}
