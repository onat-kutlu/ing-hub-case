package com.ing.inghub.dto.base;

import com.ing.inghub.constants.errorcodes.ErrorCodes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ServiceResult {

    private boolean success;
    private String message;
    private String code;

    public static ServiceResult getSuccessResult() {
        return new ServiceResult()
                .setSuccess(true)
                .setCode(ErrorCodes.SUCCESS_CODE)
                .setMessage(ErrorCodes.SUCCESS_MESSAGE);

    }
}
