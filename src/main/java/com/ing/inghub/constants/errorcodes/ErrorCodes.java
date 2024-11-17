package com.ing.inghub.constants.errorcodes;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorCodes {

    public static final String BAD_REQUEST_ERROR_CODE = "-1";
    public static final String SUCCESS_CODE = "0";
    public static final String SUCCESS_MESSAGE = "OK";
    public static final String E_CUSTOMER_ALREADY_EXIST = "E_CUSTOMER_ALREADY_EXIST";
    public static final String E_CUSTOMER_NOT_FOUND = "E_CUSTOMER_NOT_FOUND";
    public static final String E_INVALID_INSTALLMENT_COUNT = "E_INVALID_INSTALLMENT_COUNT";
    public static final String E_INVALID_INTEREST_RATE = "E_INVALID_INTEREST_RATE";
    public static final String E_NOT_ENOUGH_CUSTOMER_LIMIT = "E_NOT_ENOUGH_CUSTOMER_LIMIT";
    public static final String E_LOAN_NOT_FOUND = "E_LOAN_NOT_FOUND";
    public static final String E_INVALID_ACCESS = "E_INVALID_ACCESS";
}
