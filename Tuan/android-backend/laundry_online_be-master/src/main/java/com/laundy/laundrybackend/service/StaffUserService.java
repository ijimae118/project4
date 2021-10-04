package com.laundy.laundrybackend.service;

import com.laundy.laundrybackend.models.dtos.JwtResponseDTO;
import com.laundy.laundrybackend.models.request.RegisterNewShipperUserForm;
import com.laundy.laundrybackend.models.request.RegisterNewStaffUserForm;
import com.laundy.laundrybackend.models.request.UserLoginForm;

public interface StaffUserService {
    void registerNewUser(RegisterNewStaffUserForm registerNewStaffUserForm, String role);
    void registerNewShipper(RegisterNewShipperUserForm form);
    JwtResponseDTO loginUser(UserLoginForm userLoginForm);
}
