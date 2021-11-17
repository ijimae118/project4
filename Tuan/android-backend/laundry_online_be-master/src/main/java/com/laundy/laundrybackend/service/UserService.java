package com.laundy.laundrybackend.service;

import com.laundy.laundrybackend.models.User;
import com.laundy.laundrybackend.models.dtos.JwtResponseDTO;
import com.laundy.laundrybackend.models.dtos.UserInfoDTO;
import com.laundy.laundrybackend.models.request.RegisterUserForm;
import com.laundy.laundrybackend.models.request.SocialUserFirstLoginForm;
import com.laundy.laundrybackend.models.request.SocialUserLoginForm;
import com.laundy.laundrybackend.models.request.UserLoginForm;

public interface UserService {
    void registerNewUser(RegisterUserForm registerUserForm);
    JwtResponseDTO loginUser(UserLoginForm userLoginForm);
    Object loginSocialUser(SocialUserLoginForm socialUserLoginForm);
    Object loginSocialUserFirstTime(SocialUserFirstLoginForm socialUserFirstLoginForm);
    UserInfoDTO getCurrentUserInfo();
//    User getUserByUsername()
}
