package com.laundy.laundrybackend.controller.api;

import com.laundy.laundrybackend.models.request.AddressForm;
import com.laundy.laundrybackend.models.response.GeneralResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RequestMapping("addresses/")
@CrossOrigin("*")
@Validated
public interface AddressInterface {
    @GetMapping("all")
    @Operation(description = "Lấy danh sách address của user hiện tại", summary = "Lấy danh sách address của user hiện tại")
    GeneralResponse<?> getAllAddressOfCurrentUser();

    @GetMapping("")
    @Operation(description = "Lấy thông tin address của user hiện tại bằng id", summary = "Lấy thông tin address của user hiện tại bằng id")
    GeneralResponse<?> getAddressById(@RequestParam("addressId") @Parameter(name = "addressId",description = "Id của address",required = true) @NotNull Long addressId);

    @PostMapping("new")
    @Operation(description = "Tạo address mới cho user hiện tại", summary = "Tạo address mới cho user hiện tại")
    GeneralResponse<?> createNewAddressForCurrentUser(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form thông tin address", required = true) AddressForm addressForm);

    @PutMapping("update")
    @Operation(description = "Update thông tin address của user hiện tại theo id", summary = "Update thông tin address của user hiện tại theo id")
    GeneralResponse<?> updateAddressInfo(@RequestParam("addressId") @Parameter(name = "addressId",description = "Id của address",required = true) @NotNull Long addressId, @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form thông tin address", required = true) AddressForm addressForm);

    @DeleteMapping("delete")
    @Operation(description = "Xóa thông tin address của user hiện tại theo id", summary = "Xóa thông tin address của user hiện tại theo id")
    GeneralResponse<?> deleteAddressInfo(@RequestParam("addressId") @Parameter(name = "addressId",description = "Id của address",required = true) @NotNull Long addressId);

}
