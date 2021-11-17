package com.laundy.laundrybackend.constant;

import java.math.BigDecimal;

public class Constants {
    public static final String PHONE_NUMBER_REGEX = "(\\+[0-9]+[\\- \\.]*)?(\\([0-9]+\\)[\\- \\.]*)?([0-9][0-9\\- \\.]+[0-9])";

    public static final String REGISTER_NEW_USER_SUCCESS = "Register new user successfully";
    public static final String USER_EXIST_ERROR_MESS = "User infos existed";
    public static final String USER_NOT_EXISTED_ERROR = "User not existed";
    public static final String ORDER_CANCELED = "Order canceled";
    public static final String ORDER_UNACCEPTED = "Order unaccepted";
    public static final String ORDER_RECEIVED_BY_SHIPPER = "Order received by Shipper";
    public static final String ORDER_RECEIVED_BY_STORE = "Order received by Store";
    public static final String ORDER_DONE_BY_STORE = "Order done by Store";
    public static final String ORDER_DELIVERING = "Order delivering";
    public static final String ORDER_COMPLETED= "Order completed";
    public static final String ORDER_PAYMENT_UPDATED = "Order payment info updated";
    public static final String NEW_ADDRESS_CREATED = "New Address created for User";
    public static final String ADDRESS_INFO_UPDATED = "Address info updated";
    public static final String MISSING_PARAMETER = "Missing parameters: ";
    public static final String INVALID_FIELDS = "Please check those invalid fields: ";
    public static final String REQUEST_DATA_NOT_READABLE = "Request data not readable please check it";
    public static final String ADDRESS_CANNOT_BE_DELETE = "Default address can't be deleted";
    public static final String ORDER_CANNOT_BE_CANCEL = "This Order can't be cancel";
    public static final String SOCIAL_USER_NOT_EXIST = "Social user not exist in system";
    public static final String SOCIAL_USER_EMAIL_LINK_TO_EXISTED_USER = "Social email already link to another system account! Please login using that account";
    public static int MAX_SHIPPER_CONCURRENT_ACCEPT_ORDERS = 5;

    public static final BigDecimal VAT_VALUES = BigDecimal.valueOf(1.1);
    private Constants(){

    }
}
