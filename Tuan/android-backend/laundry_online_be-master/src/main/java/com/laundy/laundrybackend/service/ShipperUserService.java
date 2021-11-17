package com.laundy.laundrybackend.service;

import com.laundy.laundrybackend.models.dtos.JwtResponseDTO;
import com.laundy.laundrybackend.models.request.UserLoginForm;

public interface ShipperUserService {

    JwtResponseDTO loginUser(UserLoginForm userLoginForm);
}
