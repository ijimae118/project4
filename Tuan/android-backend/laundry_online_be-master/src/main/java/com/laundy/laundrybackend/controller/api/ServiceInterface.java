package com.laundy.laundrybackend.controller.api;

import com.laundy.laundrybackend.models.response.GeneralResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("services/")
@Validated
public interface ServiceInterface {
    @GetMapping("all")
    @Operation(description = "Load danh sách các dịch vụ hiện có", summary = "Load danh sách các dịch vụ hiện có")
    GeneralResponse<?> getAllService();

    @PostMapping("details")
    @Operation(description = "Load danh sách các dịch vụ chi tiết theo dịch vụ đã chọn", summary = "Load danh sách các dịch vụ chi tiết theo dịch vụ đã chọn")
    GeneralResponse<?> getServiceDetails(@RequestParam("serviceId") @Parameter(description = "Id của dịch vụ đã chọn",example = "1") Long serviceId);

}
