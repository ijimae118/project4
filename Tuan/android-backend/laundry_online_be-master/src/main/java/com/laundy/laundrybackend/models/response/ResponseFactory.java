package com.laundy.laundrybackend.models.response;

import com.laundy.laundrybackend.constant.Constants;
import com.laundy.laundrybackend.constant.ResponseStatusCodeEnum;

public class ResponseFactory {
    public static GeneralResponse<Object> successResponse(Object data){
        return new GeneralResponse<>(ResponseStatusCodeEnum.SUCCESS.getCode(),ResponseStatusCodeEnum.SUCCESS.name(), data);
    }

    public static GeneralResponse<Object> successResponse(ResponseStatusCodeEnum statusCodeEnum, String message, Object data){
        return new GeneralResponse<>(statusCodeEnum.getCode(), message, data);
    }
}
