package com.laundy.laundrybackend.controller.impl;

import com.laundy.laundrybackend.constant.Constants;
import com.laundy.laundrybackend.constant.ResponseStatusCodeEnum;
import com.laundy.laundrybackend.controller.api.UserInterface;
import com.laundy.laundrybackend.models.dtos.JwtResponseDTO;
import com.laundy.laundrybackend.models.request.RegisterUserForm;
import com.laundy.laundrybackend.models.request.SocialUserFirstLoginForm;
import com.laundy.laundrybackend.models.request.SocialUserLoginForm;
import com.laundy.laundrybackend.models.request.UserLoginForm;
import com.laundy.laundrybackend.models.response.GeneralResponse;
import com.laundy.laundrybackend.models.response.ResponseFactory;
import com.laundy.laundrybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class UserController implements UserInterface {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public GeneralResponse<?> registerNewUser(RegisterUserForm registerUserForm) {
        userService.registerNewUser(registerUserForm);
        return ResponseFactory.successResponse(Constants.REGISTER_NEW_USER_SUCCESS);
    }

    @Override
    public GeneralResponse<?> login(UserLoginForm userLoginForm) {
        JwtResponseDTO responseDTO = userService.loginUser(userLoginForm);
        return ResponseFactory.successResponse(responseDTO);
    }

    @Override
    public GeneralResponse<?> socialLogin(SocialUserLoginForm socialUserLoginForm) {
        Object result = userService.loginSocialUser(socialUserLoginForm);
        if (result instanceof ResponseStatusCodeEnum) {
            if (result.equals(ResponseStatusCodeEnum.SOCIAL_USER_NOT_EXIST)) {
                return ResponseFactory.successResponse((ResponseStatusCodeEnum) result, Constants.SOCIAL_USER_NOT_EXIST, socialUserLoginForm);
            }
            if (result.equals(ResponseStatusCodeEnum.SOCIAL_USER_EMAIL_LINK_TO_EXISTED_USER)) {
                return ResponseFactory.successResponse((ResponseStatusCodeEnum) result, Constants.SOCIAL_USER_EMAIL_LINK_TO_EXISTED_USER, socialUserLoginForm);
            }
        } else if (result instanceof JwtResponseDTO) {
            return ResponseFactory.successResponse(result);
        }
        return null;
    }

    @Override
    public GeneralResponse<?> socialFirstLogin(SocialUserFirstLoginForm socialUserFirstLoginForm) {
        Object result = userService.loginSocialUserFirstTime(socialUserFirstLoginForm);
        if (result instanceof ResponseStatusCodeEnum) {
            if (result.equals(ResponseStatusCodeEnum.SOCIAL_USER_EMAIL_LINK_TO_EXISTED_USER)) {
                return ResponseFactory.successResponse((ResponseStatusCodeEnum) result, Constants.SOCIAL_USER_EMAIL_LINK_TO_EXISTED_USER, socialUserFirstLoginForm);
            }
        } else if (result instanceof JwtResponseDTO) {
            return ResponseFactory.successResponse(result);
        }
        return null;
    }

    @Override
    public GeneralResponse<?> getUserInfo() {
        return ResponseFactory.successResponse(userService.getCurrentUserInfo());
    }
}
