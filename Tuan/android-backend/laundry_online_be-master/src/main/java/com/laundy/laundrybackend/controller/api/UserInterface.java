package com.laundy.laundrybackend.controller.api;

import com.laundy.laundrybackend.models.request.RegisterUserForm;
import com.laundy.laundrybackend.models.request.SocialUserFirstLoginForm;
import com.laundy.laundrybackend.models.request.SocialUserLoginForm;
import com.laundy.laundrybackend.models.request.UserLoginForm;
import com.laundy.laundrybackend.models.response.GeneralResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("user/")
@Validated
public interface UserInterface {
    @PostMapping("auth/signup")
    @Operation(description = "Đăng kí tài khoản User mới", summary = "Đăng kí tài khoản User mới")
    GeneralResponse<?> registerNewUser(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form Đăng kí User mới", required = true) RegisterUserForm registerUserForm);

    @PostMapping("auth/signin")
    @Operation(description = "Đăng nhập hệ thống với username hoặc số điện thoại và password", summary = "Đăng nhập hệ thống với username hoặc số điện thoại và password")
    GeneralResponse<?> login(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form thông tin đăng nhập", required = true) UserLoginForm userLoginForm);

    @PostMapping("auth/social-signin")
    @Operation(description = "Đăng nhập hệ thống với social user", summary = "Đăng nhập hệ thống với social user")
    GeneralResponse<?> socialLogin(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form thông tin social user", required = true) SocialUserLoginForm socialUserLoginForm);

    @PostMapping("auth/social-first-signin")
    @Operation(description = "Đăng nhập hệ thống lần đầu với social user", summary = "Đăng nhập hệ thống lần đầu với social user")
    GeneralResponse<?> socialFirstLogin(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Form thông tin đăng nhập lần đầu của social user", required = true) SocialUserFirstLoginForm socialUserFirstLoginForm);

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("user-info")
    @Operation(description = "Thông tin của user", summary = "Thông tin của user")
    GeneralResponse<?> getUserInfo();
}
