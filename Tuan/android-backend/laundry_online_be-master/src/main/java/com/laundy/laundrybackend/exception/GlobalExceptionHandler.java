package com.laundy.laundrybackend.exception;

import com.laundy.laundrybackend.constant.Constants;
import com.laundy.laundrybackend.constant.ResponseStatusCodeEnum;
import com.laundy.laundrybackend.models.response.GeneralResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.NoResultException;
import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class})
    public final ResponseEntity<Object> handleAllException(Exception e) {
        return createResponse(ResponseStatusCodeEnum.INTERNAL_SERVER_ERROR,"Server Errors");
    }

    @ExceptionHandler({UnauthorizedException.class})
    public final ResponseEntity<Object> handleUnauthorizedException(Exception e) {
        return createResponse(ResponseStatusCodeEnum.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler({NoResultException.class})
    public final ResponseEntity<Object> handleNoResultException(Exception e) {
        return createResponse(ResponseStatusCodeEnum.NO_RESULT_ERROR, e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    public final ResponseEntity<Object> handleValidationException(Exception e) {
        if (e.getMessage().equals(Constants.USER_EXIST_ERROR_MESS)) {
            return createResponse(ResponseStatusCodeEnum.REGISTER_EXISTED_USER_ERROR, Constants.USER_EXIST_ERROR_MESS
            );
        } else if (e.getMessage().equals(Constants.USER_NOT_EXISTED_ERROR)) {
            return createResponse(ResponseStatusCodeEnum.USER_NOT_EXISTED_ERROR, Constants.USER_NOT_EXISTED_ERROR
            );
        }
        return createResponse(ResponseStatusCodeEnum.VALIDATE_DATA_ERROR);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<Object> handleMethodArgumentNotValidException(Exception e) {
        String[] invalidFields = StringUtils.substringsBetween(e.getMessage(), "'", "'");
        List<String> test = Arrays.stream(invalidFields).collect(Collectors.groupingBy(Function.identity())).entrySet().stream().filter(s -> s.getValue().get(0).contains("Form")).map(Map.Entry::getKey).collect(Collectors.toList());
        for (String s : test) invalidFields = ArrayUtils.removeAllOccurrences(invalidFields, s);
        String message = Constants.INVALID_FIELDS + String.join(",", new HashSet<>(Arrays.asList(invalidFields)));
        return createResponse(ResponseStatusCodeEnum.INVALID_FIELDS, message);
    }

    @ExceptionHandler({OrderCannotBeCancelException.class})
    public final ResponseEntity<Object> handleOrderCannotBeCancelException(Exception e) {
        return createResponse(ResponseStatusCodeEnum.ORDER_CANNOT_BE_CANCEL, Constants.ORDER_CANNOT_BE_CANCEL);
    }

    @ExceptionHandler({AddressCannotBeDeleteException.class})
    public final ResponseEntity<Object> handleAddressCannotBeDeleteException(Exception e) {
        return createResponse(ResponseStatusCodeEnum.ADDRESS_CANNOT_BE_DELETE, Constants.ADDRESS_CANNOT_BE_DELETE);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public final ResponseEntity<Object> handleMissingServletRequestParameterException(Exception e) {
        String[] missingParams = StringUtils.substringsBetween(e.getMessage(), "'", "'");
        String message = Constants.MISSING_PARAMETER + StringUtils.substringsBetween(Arrays.toString(missingParams), "[", "]")[0];
        return createResponse(ResponseStatusCodeEnum.MISSING_PARAMS, message);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public final ResponseEntity<Object> handleHttpMessageNotReadableException(Exception e) {
        return createResponse(ResponseStatusCodeEnum.REQUEST_DATA_NOT_READABLE, Constants.REQUEST_DATA_NOT_READABLE);
    }

    @ExceptionHandler({SocialLoginException.class})
    public final ResponseEntity<Object> handleSocialLoginException(Exception e) {
        return createResponse(ResponseStatusCodeEnum.INTERNAL_SERVER_ERROR,"Server Errors");
    }

    private ResponseEntity<Object> createResponse(ResponseStatusCodeEnum statusCodeEnum) {
        GeneralResponse<Object> responseObject = new GeneralResponse<>();
        responseObject.setStatus(statusCodeEnum.getCode());
        return new ResponseEntity<>(responseObject, HttpStatus.valueOf(statusCodeEnum.getHttpCode()));
    }

    private ResponseEntity<Object> createResponse(ResponseStatusCodeEnum statusCodeEnum, String message) {
        GeneralResponse<Object> responseObject = new GeneralResponse<>();
        responseObject.setStatus(statusCodeEnum.getCode());
        responseObject.setMessage(message);
        responseObject.setData(null);
        return new ResponseEntity<>(responseObject, HttpStatus.valueOf(statusCodeEnum.getHttpCode()));
    }
}
