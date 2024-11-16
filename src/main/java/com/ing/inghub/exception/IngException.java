package com.ing.inghub.exception;

import com.ing.inghub.constants.errorcodes.ErrorCodes;
import lombok.Getter;

@Getter
public class IngException extends Exception {
    private String code;

    public IngException(String message, String code) {
        super(message);
        this.code = code;
    }

    public IngException(String code) {
        super(ErrorCodes.BAD_REQUEST_ERROR_CODE);
        this.code = code;
    }
}
